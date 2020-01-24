# 즉시 로딩과 지연 로딩

> 즉시 로딩이란 객체 A를 조회할 때 A와 연관된 객체들을 한번에 가져오는것이다.
지연 로딩이란 객체A를 조회할 때는 A만 가져오고 연관된 애들은 저번 게시글에서 본 프록시 초기화 방법으로 가져온다.

### EAGER, LAZY
- 즉시로딩(EAGER)와 지연로딩(LAZY)은 연관관계의 다중성에 따라 기본값이 달라진다.
- @ManyToOne, @OneToOne 처럼 One으로 끝나는 것들은 기본값이 즉시로딩이다.
- @OneToMany, @ManyToMany 처럼 Many로 끝나는 것들은 기본값이 지연로딩이다.


### 즉시 로딩

```JAVA
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    // 명시적 즉시 로딩 설정
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;
}

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String teamName;

    @OneToMany(mappedBy = "team")
    private List<Member> members;
}


// Main
Member m = new Member();
m.setName("Dexter");
em.persist(m);

em.flush();
em.clear();

Member findMember = em.find(Member.class, m.getId());
```
- Member class를 보면 Team을 즉시로딩으로 설정해주었다.
- 사실 기본값이 즉시로딩이라 따로 설정하지 않아도 즉시 로딩이 적용된다.

<br>

```SQL
Hibernate:
    select
        생략
    from
        Member member0_
    left outer join
        Team team1_
            on member0_.team_id=team1_.team_id
    where
        member0_.member_id=?
```
- SQL을 확인해보면 member만 조회하였지만 team까지 한번에 가져오는 것을 알 수 있다.

### 지연 로딩
- 위의 예제에서 Member class의 team을 LAZY(지연 로딩)으로 변경 하였다.

```JAVA
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "team_id")
private Team team;
```

<br>

```JAVA
Team team = new Team();
team.setTeamName("team");
em.persist(team);

Member m = new Member();
m.setName("Dexter");
m.setTeam(team);
em.persist(m);

em.flush();
em.clear();

Member findMember = em.find(Member.class, m.getId());
System.out.println(findMember);
System.out.println("================ member 가져옴 ================");

Team getTeam = findMember.getTeam();
System.out.println(getTeam);
System.out.println("================ team 가져옴 ================");
```
- 우선 member만 조회한 후 member.getTeam()을 통해 team을 가져왔다.

<br>

```SQL
Hibernate:
    select
        생략..
    from
        Member member0_
    where
        member0_.member_id=?
blogJpa.Member@6826c41e
================ member 가져옴 ================
Hibernate:
    select
        생략..
    from
        Team team0_
    where
        team0_.team_id=?
blogJpa.Team@7ea4d397
================ team 가져옴 ================
```
- SQL을 확인해보면 우선 Member만 조회하고 그 후에 team을 가져올 때 Team을 조회하는것을 알 수 있다.


### 성능과 문제점
- 위의 예제를 보면 즉시로딩이 정말 간편한거 처럼 느껴진다.
- 하지만 실제 엔티티의 연관관계가 깊어지면 즉시로딩이 연관된 모든 엔티티들을 조회하게 된다.
- 그리고 그 엔티티들을 조회할 때 따로따로 조회하므로 **N + 1문제** 가 발생한다.
- **그러므로 가급적 모든 관계는 지연 로딩으로 설정해야 한다**

#### **N+1 문제**
```JAVA
// Member.class
@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "team_id")
private Team team;

// Main
Team team1 = new Team();
team1.setTeamName("team1");
em.persist(team1);

Member m1 = new Member();
m1.setName("Dexter");
m1.setTeam(team1);
em.persist(m1);


Team team2 = new Team();
team2.setTeamName("team2");
em.persist(team2);

Member m2 = new Member();
m2.setName("James");
m2.setTeam(team2);
em.persist(m2);

em.flush();
em.clear();

List<Member> members = em.createQuery("select m from Member m", Member.class)
        .getResultList();
```
- 우선 Member에서 team을 즉시로딩으로 설정한다.
- m1에는 team1을 넣고 m2에는 team2를 넣었다.
- 그 후 모든 member를 조회하였다.

<br>

```SQL
Hibernate:
    select
        생략...
    from
        Member member0_
Hibernate:
    select
        생략 ...
    from
        Team team0_
    where
        team0_.team_id=?
Hibernate:
    select
        생략 ...
    from
        Team team0_
    where
        team0_.team_id=?
```
- SQL을 보면 멤버는 한번에 가져오지만 team을 가져올 때 쿼리를 따로 따로 날리게 된다.
- 물론 지연로딩도 N + 1문제는 똑같을 것이다.
- 하지만 지연로딩으로 설정하고 엔티티를 조회할때 fetch join을 사용하면 한번에 데이터를 가져올 수가 있다.

### **지연로딩과 페치조인**
```JAVA
// Member.class
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "team_id")
private Team team;

// Main
List<Member> members = em.createQuery("select m from Member m join fetch m.team", Member.class).getResultList();

for (Member member : members) {
    System.out.println(member.getTeam().getTeamName());
}
```
- 우선 지연로딩으로 변경 후 query에서 fetch join을 사용하여 조인한다

<br>

```SQL
Hibernate:
    select
        member0_.member_id as member_i1_0_0_,
        team1_.team_id as team_id1_1_1_,
        member0_.createTime as createTi2_0_0_,
        member0_.lastModifiedDate as lastModi3_0_0_,
        member0_.name as name4_0_0_,
        member0_.team_id as team_id5_0_0_,
        team1_.teamName as teamName2_1_1_
    from
        Member member0_
    inner join
        Team team1_
            on member0_.team_id=team1_.team_id
team1
team2
```
- SQL을 보면 한번에 Member, team을 조회하고 team을 출력해봐도 이미 fetch join으로 조회하였으므로 따로 SQL을 날리지 않는다.

> 페치 조인을 사용하더라고 몇가지 문제점 들이 존재한다.
컬렉션을 페치조인 할때의 데이터 중복 문제 등이 있다. 이 문제들은 JPQL fetch join 게시글을 작성할 때 다루도록 한다.

### 결론
- 즉시 로딩은 가급적 피하고 지연 로딩을 사용한다.
- 한번에 연관된 엔티티들의 데이터를 조회해야 할 때는 페치 조인을 이용한다.

---

[참고도서](http://www.yes24.com/Product/Goods/19040233)
