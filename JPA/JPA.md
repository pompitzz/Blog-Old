# JPA

> Java Persistence API로 자바 진영의 **ORM** 기술 표준이다.  
> 객체와 관계형 데이터베이스 간의 패러다임의 불일치를 해결해준다.

### ORM

Object-relational mapping의 약자로 말 그대로 객체 관계 매핑 기술이다.

객체와 관계형 DB를 중간에서 매핑해주는 기술이다.

### JPA 동작

JPA는 JAVA Application과 JDBC 사이에서 동작하여 Entity 분석, SQL 생성, 패러다임 불일치 등의 작업을 수행해준다.

### JDBC

자바에서 데이터베이스에 접속할 수 있도록 하는 API이다.

JPA를 사용하면 객체 중심적인 개발이 가능하고, 생산성, 유지보수 측면에서도 유리하며 패러다임의 불일치를 해결해준다.

---

# 영속성 관리

## 영속성 콘텍스트

JPA에서 가장 중요한 2가지는 ORM과 영속성 콘텍스트이다.

**엔티티를 영구 저장하는 환경이라는** 뜻으로 논리적인 개념이어서 눈에 보이진 않으며 엔티티 매니저를 통해 영속성 콘텍스트에 접근한다.

### 영속성 생명주기

**1\. 비영속:** 영속성 콘텍스트와 전혀 관계가 없는 새로운 상태이다.

**2\. 영속(managed):** 영속성 콘텍스트에 관리되는 상태이다. em.persist를 하게되면 item이 영속성 컨텍스트에 들어간다.

**3\. 준영속:** 영속성 콘텍스트에 저장되었다가 분리된 상태이다. em.detach를 하게 되면 item이 영속성 콘텍스트에서 분리된다.

**4\. 삭제:** 영속성 콘텍스트에 아예 삭제되는 것이다.

```java
try{

    Item item = new Item();
    item.setId(1L);
    item.setName("item1"); // 1. 여기까진 비영속 상태이다.

    em.persist(item); // 2. 영속

    em.detach(item); // 3. 준영속

    em.remove(item); // 4. 삭제

    tx.commit();

}
```

## 플러시와 준영속 상태

### 플러시

영속성 콘텍스트의 변경 내용을 데이터베이스 반영

**플러시 발생**

1.  변경 감지
2.  수정된 엔티티 쓰기 지연 SQL 저장소에 등록
3.  쓰기 지연 SQL 저장소의 쿼리를 DB에 전송

### 영속성 콘텍스트를 플러시 하기

1.em.flush()로 직접 호출하기

2\. 트랜잭션 commit으로 flush 자동 호출

3.JPQL 쿼리를 통해 flush 자동 호출

```java
try{
  Item item = new Item();
  item.setId(1L);
  item.setName("item1");

  em.flush(); // 1. flush를 직접 호출할 수 있다.

  tx.commit(); // 2. 혹은 tx.commit을 하게되면 flush가 자동 호출된다.

  em.createQuery("select i from Item i", Item.class);
  // 3. JPQL 쿼리를 날리면 flush가 자동호출 된다.
}
```

#### flush 특징

영속성 콘텍스트를 비우지 않는다.  
영속성 콘텍스트의 **변경 내용을 DB에 동기화한다.**

### 준영속 상태

준영속 상태가 되면 **영속성 콘텍스트가 제공하는 기능을 사용하지 못한다**

```java
try {

    Item item = new Item();
    item.setId(1L);
    item.setName("item1");

    em.detach(item); // detach로 준영속 성태를 만들 수 있다.

    em.clear();// 영속성 컨텍스트를 초기화 한다.

} catch (Exception e) {
    tx.rollback();
    e.printStackTrace();
} finally {
    em.close();// em.close시 영속성 컨텍스트가 종료된다.
}
```

---

[참고 자료](https://www.inflearn.com/course/ORM-JPA-Basic/#)
