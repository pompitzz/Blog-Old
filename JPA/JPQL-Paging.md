# JPQL 페이징, 조인과 조인  ON 절
> JPQL 페이징방법과 다양한 조인들에 대해서 알아본다

### 페이징
```JAVA
IntStream.rangeClosed(1, 50).forEach(i
        -> em.persist(new Member("member" + 1, i)));

List<Member> result = em.createQuery
        ("select m from Member m order by m.age desc", Member.class)
        .setFirstResult(10)
        .setMaxResults(10)
        .getResultList();
```
- order by로 정렬이 가능하다. 나이를 내림차순으로 지정하였다.
- setFirstResult로 조회 시작 위치를 지정할 수 있다. 0부터 시작이므로 11번째부터 가져올 것이다.
- setMaxResults로 데이터 수를 지정할 수 있다. 10을 지정하였으므로 10개를 가져올 것이다.

```SQL
Hibernate:
        select
          ... 생략
        from
            Member member0_
        order by
            member0_.age desc limit ? offset ?

Member(id=40, name=member1, age=40)
Member(id=39, name=member1, age=39)
Member(id=38, name=member1, age=38)
Member(id=37, name=member1, age=37)
Member(id=36, name=member1, age=36)
Member(id=35, name=member1, age=35)
Member(id=34, name=member1, age=34)
Member(id=33, name=member1, age=33)
Member(id=32, name=member1, age=32)
Member(id=31, name=member1, age=31)
```
- 출력을 확인해보면 나이를 내림차순으로 조회하고 조회 시작위치와 데이터 수가 정상적으로 지정된 것을 확인할 수 있다.
- 현재는 DB를 H2로 설정하였으므로 H2에 맞는 적절한 SQL로 조회를 한다.


#### 방언 변경
- 현재는 H2에 맞는 SQL을 날려주지만 dialect 설정을 통해 간단하게 다른 DBMS의 SQL로 변경이 가능하다.

```xml
<property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
>>>> 아래로 변경 <<<<
<property name="hibernate.dialect" value="org.hibernate.dialect.Oracle8iDialect"/>
```

<br>

```SQL
Hibernate:
        select
            *
        from
            ( select
                row_.*,
                rownum rownum_
            from
                ( select
                    생력...
                from
                    Member member0_
                order by
                    member0_.age desc ) row_ )
            where
                rownum_ <= ?
                and rownum_ > ?
```
- hibernate.dialect을 H2에서 Oracle로 변경 후 실행을 해보면 JPA에서 알아서 Oracle에 맞는 SQL을 날려준다.

---

### 조인
- SQL과 문법이 거의 비슷하다. 하지만 JPQL은 엔티티로 조인을 한다
#### Inner, Outer Join

```JAVA
List<Member> innerJoin = em.createQuery
        ("select m from Member m inner join m.team t", Member.class)
        .getResultList();

List<Member> leftOuterJoin = em.createQuery
        ("select m from Member m left join m.team t", Member.class)
        .getResultList();
```

<br>

```SQL
Hibernate:
        select
            생략 ..
        from
            Member member0_
        inner join
            Team team1_
                on member0_.team_id=team1_.team_id
Hibernate:
        select
            생략 ..
        from
            Member member0_
        left outer join
            Team team1_
                on member0_.team_id=team1_.team_id
```
- SQL을 확인해보면 정상적으로 조인이 동작한다.

#### Theta Join
- 세타 조인을 통해 연관관계가 없는 엔티티를 조회할 수 있다.
- 세타 조인을 하게되면 각 행과 상대방 테이블의 행을 모두 조인하는  Cartesian product를 수행하게 된다.

```JAVA
em.persist(new Member("A", 10));
em.persist(new Member("B", 10));
em.persist(new Member("C", 10));

em.persist(new User("A"));
em.persist(new User("B"));

List<Member> resultList = em.createQuery
        ("select m from Member m, User u where m.name = u.name", Member.class)
        .getResultList();

resultList.forEach(System.out::println);
```
- Member name과 User name이 같은 Member들을 조회한다.

```SQL
Hibernate:
        select
          생략...
        from
            Member member0_
        cross join
            User user1_
        where
            member0_.name=user1_.name

Member(id=1, name=A, age=10)
Member(id=2, name=B, age=10)
```
- cross join을 한 후 같은 이름의 member가 정상적으로 조회가 되는것을 확인할 수 있다.

---

### 조인 - ON
- JPA 2.1 부터 지원하는 기능이다.
- 조인 대상을 필터링 할수 있다.
- 연관관계 없는 엔티티를 외부 조인할 수 있다.

### 조인대상 필터링
- 조인을 하기 전 조인할 대상을 미리 필터링 할 수 있다.

#### where로 조회할 시
```JAVA
Team team1 = new Team("teamMember1");
em.persist(team1);
Team team2 = new Team("teamMember2");
em.persist(team2);

em.persist(new Member("teamMember1", 10, team1));
em.persist(new Member("teamMember2", 13, team2));
em.persist(new Member("teamMember3", 15, team2));

em.flush(); em.clear();


List<Member> resultList = em.createQuery
        ("select m from Member m left join m.team t where t.teamName = m.name", Member.class)
        .getResultList();

for (Member member : resultList) {
    System.out.println(member);
}
```
- 우선 on절이 아닌 where절로 team의 이름과 member의 이름이 같은 member를 조회해보자.
- 이때는 조인 후 where로 필터링 하기 때문에 아래와 같이 두개의 멤버만 조회된다.

```SQL
Hibernate:
        select
            생략...
        from
            Member member0_
        left outer join
            Team team1_
                on member0_.team_id=team1_.team_id
        where
            team1_.teamName=member0_.name

Member(id=3, name=teamMember1, age=10)
Member(id=4, name=teamMember2, age=13)
```

#### on으로 조회할 시
```JAVA
List<Member> resultList = em.createQuery
        ("select m from Member m left join m.team t on t.teamName = m.name", Member.class)
        .getResultList();

for (Member member : resultList) {
    System.out.println(member);
}
```
- 이렇게 on절로 조회를 하게되면 조인을 하기전 team을 필터링하게 된다.
- team을 필터링 후 left 조인을 하여도 left 조인 특성상 member가 값을 가지고 있다면 member는 조회가 될 것이다.
- 그러므로 아래와 같이 모든 member가 출력되는 것을 알 수 있다.

```SQL
Hibernate:
        select
            생략...
        from
            Member member0_
        left outer join
            Team team1_
                on member0_.team_id=team1_.team_id
                and (
                    team1_.teamName=member0_.name
                )

Member(id=3, name=teamMember1, age=10)
Member(id=4, name=teamMember2, age=13)
Member(id=5, name=teamMember3, age=15)
```
- sql을 확인해보면 조인할 때 and 연산을 통해 같은 name을 가지고 있는 대상을 필터링 하는것을 알 수 있다.

### 연관관계가 없는 엔티티 조인
- 내부조인은 원래 가능하지만 외부 조인은 Hibernate 5.1부터 가능하다.

#### 내부 조인
```JAVA
em.persist(new User("A"));
em.persist(new User("B"));

em.persist(new Member("A", 10));
em.persist(new Member("B", 10));
em.persist(new Member("C", 10));

em.flush();
em.clear();

List<Member> result = em.createQuery
        ("select m from Member m inner join User u on m.name = u.name", Member.class)
        .getResultList();

result.forEach(System.out::println);
```
- 연관관계가 없는 User와 Member를 on을 통해 내부 조인하였다.

```sql
Hibernate:
        select
            생략...
        from
            Member member0_
        inner join
            User user1_
                on (
                    member0_.name=user1_.name
                )

Member(id=3, name=A, age=10)
Member(id=4, name=B, age=10)
```
- sql을 확인해보면 name으로 내부 조인이 되는것을 확인할 수있고 User와 name이 같은 A, B만 조회되는 것을 알 수 있다.

#### 외부 조인
```JAVA
List<Member> result = em.createQuery
        ("select m from Member m left join User u on m.name = u.name", Member.class)
        .getResultList();

result.forEach(System.out::println);
```
- left (outer) join 으로 변경하여 실행 해본다.

```sql
Hibernate:
        select
            생략...
        from
            Member member0_
        left outer join
            User user1_
                on (
                    member0_.name=user1_.name
                )

Member(id=3, name=A, age=10)
Member(id=4, name=B, age=10)
Member(id=5, name=C, age=10)
```
- left outer join이므로 Member가 모두 조회된 것을 확인할 수 있다.

---
[참고도서](http://www.yes24.com/Product/Goods/19040233)
