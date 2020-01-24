# 영속성 전이(CASCADE)
> 특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속 상태로 만들 때 사용하는 방법

- 부모 엔티티를 저장할 때 자식 엔티티를 저장하거나 , 부모 엔티티를 저장할 때 자식 엔티티도 같이 삭제하는 등의 방식으로 사용된다.

### 영속성 전이가 없을 때
```JAVA
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "child_id")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;

    public Child(String name) {
        this.name = name;
    }
}

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parent_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "parent")
    private List<Child> children = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addChild(Child child){
        children.add(child);
        child.setParent(this);
    }

    public Parent(String name) {
        this.name = name;
    }
}
```
- 부모가 자식들을 추가할 때 연관관계 편의 메서드로 자식의 부모도 세팅해주었다.

<br>

```JAVA
// Main
Child child1 = new Child("child1");
Child child2 = new Child("child2");

Parent parent = new Parent("parent");
parent.addChild(child1);
parent.addChild(child2);

// em.persist(child1);
// em.persist(child2);

em.persist(parent);


em.flush();
em.clear();

Parent findParent = em.find(Parent.class, parent.getId());
System.out.println("findParent.getName() = " + findParent.getName());

List<Child> children = findParent.getChildren();

for (Child child : children) {
    System.out.println("child.getName() = " + child.getName());
}
```
- child1, 2를 생성하고 따로 entityManager로 persist를 해주지 않고 바로 parent에게 추가 시켜주었다.

</br>

```sql
Hibernate:
    /* insert blogJpa.Parent
        */ insert
        into
            Parent
            (parent_id, name)
        values
            (null, ?)

Hibernate:
    select
        생략...
    from
        Parent parent0_
    where
        parent0_.parent_id=?
findParent.getName() = parent

Hibernate:
    select
        생략 ...
    from
        Child children0_
    where
        children0_.parent_id=?
children.size() = 0

1월 23, 2020 2:59:51 오후 org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl stop
INFO: HHH10001008: Cleaning up connection pool [jdbc:h2:tcp://localhost/~/test]
```
- 출력 결과를 보면 child는 insert 되지 않고 조회하면 개수가 0이다.
- 즉 child도 각각 entityManager를 통해 persist를 해주어야 한다.



### 영속성 전이 사용
```JAVA
// Parent.class
@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
private List<Child> children = new ArrayList<>();
```
- Parent의 chidren에 cascade = CascadeType.ALL를 설정해주면 영속성 전이 설정이 완료된다.

<br>

```SQL
Hibernate:
        insert
        into
            Parent
            (parent_id, name)
        values
            (null, ?)
Hibernate:
        insert
        into
            Child
            (child_id, name, parent_id)
        values
            (null, ?, ?)
Hibernate:
        insert
        into
            Child
            (child_id, name, parent_id)
        values
            (null, ?, ?)
Hibernate:
    select
        생략...
    from
        Parent parent0_
    where
        parent0_.parent_id=?
findParent.getName() = parent

Hibernate:
    select
        생략...
    from
        Child children0_
    where
        children0_.parent_id=?
children.size() = 2
child.getName() = child1
child.getName() = child2
```
- 위의 예제와 똑같이 parent만 persist 해주고 SQL을 확인해보면 child도 같이 insert 된것을 알 수 있다.

> 영속성 전이는 연관관계를 매핑하는것과는 아무 연관이 없고 단지 연관된 엔티티를 영속화 시켜주는 편리함을 제공할 뿐이다.

<br>

### 영속성 전이 종류
- cascade = CascadeType.ALL, REMOVE, PERSIST, DETACH 등 다양한 종류들이 존재한다.
- ALL을 제와하고는 설정한 상황에만 cascade가 적용된다.

### 영속성 전이를 사용할 수 있는 경우
서로 라이프 사이클이 유사할 때
- 말그대로 라이프 사이클이 같은 경우이다.

어떤 엔티티를 오직 자신만이 소유하고 있을 때
- 게시글의 댓글 같은 경우가 여기에 속한다고 볼 수 있다.

---
### 고아객체 제거
고아 객체
- 부모 엔티티가 사라진 자신 엔티티

#### 고아 객체 제거
- 참조가 제거된 엔티티를 제거하는 기능이다.

```JAVA
// Parent.class
@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Child> children = new ArrayList<>();

// Main
Child child1 = new Child("child1");
Child child2 = new Child("child2");

Parent parent = new Parent("parent");
parent.addChild(child1);
parent.addChild(child2);
em.persist(parent);


em.flush();
em.clear();

Parent findParent = em.find(Parent.class, parent.getId());
List<Child> children = findParent.getChildren();

System.out.println("============== 첫번째 자식 참조 제거 ==============");
children.remove(0);
```
- 우선 Prent에서 orphanRemoval를 true로 추가해준다.
- 그리고 findParent에서 첫번째 자식만 참조를 제거한다.

<br>

```SQL
============== 첫번째 자식 참조 제거 ==============
Hibernate:
    select
        children0_.parent_id as parent_i3_0_0_,
        children0_.child_id as child_id1_0_0_,
        children0_.child_id as child_id1_0_1_,
        children0_.name as name2_0_1_,
        children0_.parent_id as parent_i3_0_1_
    from
        Child children0_
    where
        children0_.parent_id=?
Hibernate:
    /* delete blogJpa.Child */ delete
        from
            Child
        where
            child_id=?
```
- 고아 객체 제거 설정으로인해 참조가 제거된 자식은 삭제 쿼리가 날라가는 것을 알 수 있다.
- 영속성 전이의 ALL로 인해 부모 객체 자체를 삭제하면 자식 객체가 같이 제거는 된다.
- 하지만 고아 객체 제거설정을 하지 않으면 children.remove(0)은 적용되지 않는다.
- 즉 영속성 전이와 고아 객체 제거를 설정하면 부모 엔티티에서 자식의 생명주기를 관리할 수 있다.

> 이 방법 또한 영속성 전이와 같이 참조하는 곳이 하나이고 특정 엔티티가 개인이 소유할 때 사용해야 한다.

---

[참고도서](http://www.yes24.com/Product/Goods/19040233)
