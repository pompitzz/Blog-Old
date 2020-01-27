# JPQL 서브쿼리, 조건식, 기본함수
> JPQL의 서브쿼리와 조건식, 기본함수에 대해 알아본다.

### JPQL서브 쿼리
```java
em.persist(new Member("A", 10));
em.persist(new Member("D", 15));
em.persist(new Member("B", 20));
em.persist(new Member("C", 30));


List<Member> result = em.createQuery
        ("select m from Member m" +
                " where m.age > (select avg(subM.age) from Member subM)", Member.class)
        .getResultList();

result.forEach(System.out::println);
```

<br>

```sql
Hibernate:
        select
            생략...
        from
            Member member0_
        where
            member0_.age>(
                select
                    avg(cast(member1_.age as double))
                from
                    Member member1_
            )

Member(id=3, name=B, age=20)
Member(id=4, name=C, age=30)
```
- JPQL도 서브쿼리를 지원한다.
- 위와 같이 일반적인 서브쿼리 작성과 똑같다.
- SQL을 확인해보면 서브쿼리가 제대로 동작하는 것을 알 수 있다.

#### 서브쿼리 지원 함수
**Exists**
```java
Team teamA = new Team("teamA");
em.persist(teamA);

Team teamB = new Team("teamB");
em.persist(teamB);

em.persist(new Member("A", 10, teamA));
em.persist(new Member("D", 15, teamA));
em.persist(new Member("B", 20, teamB));
em.persist(new Member("C", 30, teamB));

List<Member> result = em.createQuery
        ("select m from Member m" +
                " where exists (select t from Team t where t.teamName = 'teamA')", Member.class)
        .getResultList();

result.forEach(System.out::println);
```

<br>

```SQL
Hibernate:
        select    
            생략...
        from
            Member member0_
        where
            exists (
                select
                    team1_.team_id
                from
                    Team team1_
                where
                    team1_.teamName='teamA'
            )

Member(id=3, name=A, age=10)
Member(id=4, name=D, age=15)
Member(id=5, name=B, age=20)
Member(id=6, name=C, age=30)
```
- exists의 결과가 하나라도 존재하면 참이되는 함수이다.
- teamName이 teamA인 결과가 존재하므로 멤버가 출력된다.

**ALL, ANY**
- ALL은 결과가 모두 만족했을 경우 참이다.
- ANY는 하나라도 만족하면 참이다.

```java
em.persist(new User("User1", 20));
em.persist(new User("User2", 17));

em.persist(new Member("A", 10));
em.persist(new Member("D", 15));
em.persist(new Member("B", 20));
em.persist(new Member("C", 30));

List<Member> allQuery = em.createQuery
        ("select m from Member m" +
                " where m.age > ALL(select u.age From User u)", Member.class)
        .getResultList();

System.out.println("================================= allQuery =================================");
allQuery.forEach(System.out::println);


List<Member> anyQuery = em.createQuery
        ("select m from Member m" +
                " where m.age > ANY(select u.age From User u)", Member.class)
        .getResultList();

System.out.println("================================= anyQuery =================================");
anyQuery.forEach(System.out::println);
```
- User의 age보다 큰 Member를 조회하는 쿼리를 ALL, ANY 함수를 이용하여 구현하였다.
- ALL함수는 User age인 17, 20보다 큰 Member age는 30만 존재하므로 Member C만 출력 될 것이다.
- ANY함수는 User age인 17 or 20 보다 큰 Member age를 조회하므로 Member B, C가 출력 될 것이다.

<br>

```SQL
Hibernate:
        select
            생략..
        from
            Member member0_
        where
            member0_.age>all (
                select
                    user1_.age
                from
                    User user1_
            )
================================= allQuery =================================
Member(id=6, name=C, age=30)


Hibernate:
        select    
          생략..
        from
            Member member0_
        where
            member0_.age>any (
                select
                    user1_.age
                from
                    User user1_
            )
================================= anyQuery =================================
Member(id=5, name=B, age=20)
Member(id=6, name=C, age=30)
```
- 정상적으로 출력이 되는것을 알 수 있다.

**In**
- 결과중 하나라도 같은 값이 있으면 참이된다.

```java
em.persist(new User("User1", 20));
em.persist(new User("User2", 15));

em.persist(new Member("A", 10));
em.persist(new Member("D", 15));
em.persist(new Member("B", 20));
em.persist(new Member("C", 30));

List<Member> inQuery = em.createQuery
        ("select m from Member m" +
                " where m.age in (select u.age From User u)", Member.class)
        .getResultList();

System.out.println("================================= inQuery =================================");
inQuery.forEach(System.out::println);
```
- User age의 20, 15와 같은 Member는 D, B이므로 D, B가 출력될 것이다.

<br>

```SQL
Hibernate:
        select
            생략...
        from
            Member member0_
        where
            member0_.age in (
                select
                    user1_.age
                from
                    User user1_
            )
================================= inQuery =================================

Member(id=4, name=D, age=15)
Member(id=5, name=B, age=20)
```

#### JPA 서브쿼리의 한계
- JPA자체로는 WHERE, HAVING절에서만 서브쿼리가 사용이 가능하다.
- 하이버네이트가 지원을 해줘 SELECT절 까지 서브쿼리를 쓸 수 있다.
- 하지만 FROM 절에서는 서브쿼리가 불가능하므로 정말 성능이 중요하다면 네이티브 SQL로 작성하자.
- FROM절은 JOIN으로 해결할수 있는 가능성이 높으니 JOIN절을 사용하던지 쿼리 두번날리는것도 하나의 방법이다.

---
### JPQL 조건식과 기본함수
#### 조건식
- 기본적인 조건식 대부분 JPQL에서 사용이 가능하다.

**기본, 단순 CASE**
```java
em.persist(new Member("A", 10));
em.persist(new Member("D", 15));
em.persist(new Member("B", 20));
em.persist(new Member("C", 30));

List<String> normalCase = em.createQuery
        ("select case" +
                "       when m.age > 10 then '10대'" +
                "       when m.age > 20 then '20대'" +
                "       else '30대 이상'" +
                " end" +
                " from Member m", String.class)
        .getResultList();

normalCase.forEach(System.out::println);

List<String> simpleCase = em.createQuery
        ("select case m.name" +
                "       when 'A' then '에이'" +
                "       when 'B' then '비'" +
                "       else '나머지'" +
                " end" +
                " from Member m", String.class)
        .getResultList();

simpleCase.forEach(System.out::println);
```

<br>

```SQL
Hibernate:
        select
            case
                when member0_.age>10 then '10대'
                when member0_.age>20 then '20대'
                else '30대 이상'
            end as col_0_0_
        from
            Member member0_
30대 이상
10대
10대
10대

Hibernate:
        select
            case member0_.name
                when 'A' then '에이'
                when 'B' then '비'
                else '나머지'
            end as col_0_0_
        from
            Member member0_
에이
나머지
비
나머지
```
- SQL의 기본, 단순 CASE문과 똑같이 사용할 수 있다.

**COALESCE**
```java
em.persist(new Member("A", 10));
em.persist(new Member(null, 15));
em.persist(new Member("B", 20));
em.persist(new Member(null, 30));

List<String> coalesce = em.createQuery
    ("select coalesce(m.name, '이름 없음') from Member m", String.class)
    .getResultList();

coalesce.forEach(System.out::println);
```

```SQL
Hibernate:
        select
            coalesce(member0_.name,
            '이름 없음') as col_0_0_
        from
            Member member0_
A
이름 없음
B
이름 없음
```
- 조회값이 null이면 오른쪽 값으로 대체하는 coalesce도 사용 가능하다.

**NULLIF**
```java
em.persist(new Member("A", 10));
em.persist(new Member("B", 20));

List<String> nullif = em.createQuery
        ("select nullif(m.name, 'A') from Member m", String.class)
        .getResultList();

nullif.forEach(System.out::println);

```

<br>

```SQL
Hibernate:
        select
            nullif(member0_.name,
            'A') as col_0_0_
        from
            Member member0_

null
B
```
- 오른쪽 값과 같으면 null을 반환하는 nullif도 사용할 수 있다.

#### JPQL 기본함수
- JPQL의 기본함수들로 DB에 상관없이 사용할 수 있다.

```java
em.persist(new Member("MemberA", 10));

System.out.println("=========================== concat ===========================");
em.createQuery("select concat('a', 'b') from Member m", String.class)
        .getResultList().forEach(System.out::println);


System.out.println("=========================== substring ===========================");
em.createQuery("select substring(m.name, 3, 5) from Member m", String.class)
        .getResultList().forEach(System.out::println);

System.out.println("=========================== TRIM ===========================");
em.createQuery("select trim(' | ^ _ ^ | ') from Member m", String.class)
        .getResultList().forEach(System.out::println);

System.out.println("=========================== Upper(Lower) ===========================");
em.createQuery("select upper(m.name) from Member m", String.class)
        .getResultList().forEach(System.out::println);

System.out.println("=========================== locate ===========================");
// cd가 abcd중 몇번째에 있는지 출력해준다.
em.createQuery("select locate('cd', 'abcd') from Member m", Integer.class)
        .getResultList().forEach(System.out::println);

System.out.println("=========================== length ===========================");
em.createQuery("select length(m.name) from Member m", Integer.class)
        .getResultList().forEach(System.out::println);

System.out.println("=========================== abs ===========================");
em.createQuery("select abs(-12) from Member m", Integer.class)
        .getResultList().forEach(System.out::println);

System.out.println("=========================== mod ===========================");
em.createQuery("select mod(3 , 2) from Member m", Integer.class)
        .getResultList().forEach(System.out::println);

System.out.println("=========================== sqrt ===========================");
em.createQuery("select sqrt(16) from Member m", Double.class)
        .getResultList().forEach(System.out::println);
```

<br>

```SQL
=========================== concat ===========================
ab

=========================== substring ===========================
mberA

=========================== TRIM ===========================
| ^ _ ^ |

=========================== Upper(Lower) ===========================
MEMBERA

=========================== locate ===========================
3

=========================== length ===========================
7

=========================== abs ===========================
12

=========================== mod ===========================
1

=========================== sqrt ===========================
4.0
```
- 다양한 함수들을 제공한다.

---

[참고도서](http://www.yes24.com/Product/Goods/19040233)
