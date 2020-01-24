# JPQL
> SQL을 추상화한 객체 지향 쿼리 언어로 엔티티 객체 중심으로 쿼리를 작성할 수 있다.

- SQL은 데이터베이스 테이블을 대상으로 쿼리를 날리지만 JPQL은 엔티티 객체를 대상으로 쿼리를 날린다
- JPQL로 작성하면 결국은 SQL로 변환되어 데이터베이스에 쿼리를 날리게 된다.

### JPQL 문법
```java
em.createQuery("select m from Member m where m.age > 15", Member.class)

```
- Member 테이블 전체를 조회하는 JPQL 쿼리이다.
- select, from, where과 같은 키워드들은 대소문자를 구분하지 않는다.
- Member, m.age 과 같이 엔티티와 속성은 대소문자를 구분하여야 한다.
- 테이블 이름이 아닌 엔티티 이름을 써야한다.
- Member (as) m  과 같이 별칭은 필수이며 as는 생략 가능함

### TypeQuery, Query
```java
Query query = em
        .createQuery("select m from Member m where m.age = 15");

TypedQuery<Member> query1 = em
        .createQuery("select m from Member m where m.age > 15", Member.class);
```
- 반환 타입이 명확하지 않아 createQuery에 타입을 작성하지 않으면 Query가 된다.
- 반환 타입이 명확하여 createQuery에 타입을 넣어주면 TypeQuery가 된다.

### 결과 값, 파라미터 바인딩
```java
Member member = em
        .createQuery("select m from Member m where m.age = 15", Member.class)
        .getSingleResult();

List<Member> memberList = em
        .createQuery("select m from Member m where m.age > 15", Member.class)
        .getResultList();


Member memberParam = em
        .createQuery("select m from Member m where m.age = :age", Member.class)
        .setParameter("age", 15)
        .getSingleResult();
```
getSingleResult
- 결과를 하나만 조회할 수 있다.
- 단 조회된 결과가 둘 이상이면 NonUniqueResultException이 발생한다.
- 그리고 결과가 하나라면 NoResultException이 발생한다.

getResultList
- 결과를 컬렉션으로 받아올 수 있다. 결과가 없으면 빈 리스트를 반환한다.

setParameter
- JPQL에 :age로 작성하고 setParameter를 통해 파라미터 바인딩이 가능하다.
- SQL 인젝션을 방지할 수 있으므로 항상 사용하자.

### 프로젝션(select)

#### 엔티티 조회
```java
List<Member> members = em.createQuery("select m from Member m where m.age > :age", Member.class)
        .setParameter("age", 15)
        .getResultList();

Member firstMember = members.get(0);
firstMember.setName("Dexter");
```
- 엔티티 프로젝션으로 엔티티를 조회한다.
- 값을 받아오면 영속성 컨텍스트에 관리되므로 인스턴스의 값을 바꿔도 아래와같이 자동으로 update 쿼리가 날라간다.

```SQL
Hibernate:
    /* update
        blogJpa.Member */ update
            Member
        set
            createTime=?,
            lastModifiedDate=?,
            age=?,
            name=?
        where
            member_id=?
```

#### 연관관계 엔티티 조회
```java
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
}

@Entity
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String teamName;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();
}

// Main
List<Team> resultList = em.createQuery("select m.team from Member m", Team.class)
        .getResultList();

System.out.println("======================================================");

List<Team> resultList2 = em.createQuery("select t from Member m join m.team t", Team.class)
        .getResultList();
```

<br>

```SQL
Hibernate:
  select
      team1_.team_id as team_id1_3_,
      team1_.teamName as teamName2_3_
  from
      Member member0_
  inner join
      Team team1_
          on member0_.team_id=team1_.team_id
======================================================
Hibernate:
  select
      team1_.team_id as team_id1_3_,
      team1_.teamName as teamName2_3_
  from
      Member member0_
  inner join
      Team team1_
          on member0_.team_id=team1_.team_id
```

- 위에 방법 처럼 m.team으로 바도 받아와도 실제 쿼리를 보면 join을 하는 것을 알 수 있다.
- 하지만 명시적으로 아래 방법으로 하는게 좋다.

#### 스칼라 타입 조회
```java
Member member = new Member("dexter", 19, team);
em.persist(member);

List resultList = em.createQuery("select m.name, m.age from Member m")
        .getResultList();

Object[] objects = (Object[]) resultList.get(0);
System.out.println("name = " + objects[0]);
System.out.println("age = " + objects[1]);
```

<br>

```SQL
Hibernate:
    /* select
        m.name,
        m.age
    from
        Member m */ select
            member0_.name as col_0_0_,
            member0_.age as col_1_0_
        from
            Member member0_
name = dexter
age = 19
```
- Object[] 타입으로 여러 타입 값을 받아 사용이 가능하다.

```java
@Getter
@Setter
@NoArgsConstructor
public class MemberDto {

    private String name;

    private int age;

    public MemberDto(String name, int age) {
        this.name = name;
        this.age = age;
    }
}


// Main
List<MemberDto> resultList = em
        .createQuery("select new blogJpa.MemberDto(m.name, m.age) from Member m", MemberDto.class)
        .getResultList();

MemberDto memberDto = resultList.get(0);

System.out.println("memberDto.getName() = " + memberDto.getName());
System.out.println("memberDto.getAge() = " + memberDto.getAge());
```

<br>

```sql
Hibernate:
    /* select
        new blogJpa.MemberDto(m.name,
        m.age)
    from
        Member m */ select
            member0_.name as col_0_0_,
            member0_.age as col_1_0_
        from
            Member member0_
memberDto.getName() = dexter
memberDto.getAge() = 19
```
- new 명령어를 통해 DTO에 바로 값을 담을 수도 있다.
- 하지만 패키지명을 포함한 전체 클래스명이 필요하며 생성자의 파라미터 순서가 일치하여야 한다.
