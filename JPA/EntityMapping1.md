# 엔티티 매핑

### 엔티티 매핑
1. 객체와 테이블 매핑: @Entity, @Table
2. 필드와 컬럼 매핑: @Column
3. 기본키 매핑: @Id
4. 연관관계 매핑: @ManyToOne, @JoinColumn

#### @Entity(name="text")
JPA에서 사용할 엔티티 이름을 지정한다.
기본값으로 Class 이름을 사용한다.

#### @Table
엔티티와 매핑할 테이블을 지정한다.
| 속성  | 기능  | 기본값 |
| :-- | :---- | :--- |
| name | 매핑할 테이블 이름 | 엔티티 이름을 사용|
| catalog | 데이터베이스 catalog 매핑 ||
| schema |데이터베이스 schema 매핑 ||
| uniqueConstraints(DDL) | DDL 생성 시 유니크 제약 조건 생성 ||
```JAVA
@Entity
@Table(name = "BOX") // 테이블이 BOX로 만들어 진다.
public class Item{}
```

---

## 데이터베이스 스키마 자동 생성
개발 초기단계에는 **create, update, create-drop** 을 사용한다.
테스트 서버에는 **update, validate** 를 사용한다.
운영 서버에는 **validate, none** 을 사용한다.
| 옵션 | 설명 |
| :-- | :-- |
| create | 기존 테이블 삭제 후 다시 생성(DROP and CREATE)|
| create-drop | 종료 시점에 테이블 DROP |
| update | 변경분만 반영된다. |
| validate | 엔티티와 테이블이 정상 매핑 되었는지 확인한다. |
| none | 아무 효과 없음 |

### DDL 생성 기능
제약 조건을 추가하여 DDL 생성시 조건을 넣을 수 있다.
DDL 생성시에만 영향을 미치고 JPA 실행로직에는 영향이 없다.
```JAVA
@Column(nullable = false, length = 10)
private String name;
```

---


## 필드와 칼럼 매핑
| Annotation | 설명 |
| :--------- | :--- |
| @Column | 칼럼 매핑 |
| @Temporal | 날짜 타입 매핑 |
| @Enumerated | enum 타입 매핑 |
| @Lob | BLOB, CLOB 매핑 |
| @Transient | 특정 필드를 컬럼에 매핑하지 않는다. 매핑 무시 |

#### @Enumerated
| 속성 | 설명 | 기본값 |
| :--- | :--- | :--- |
| value | ORDINAL: enum 순서를 DB에 저장 <br> STRING: enum 이름을 DB에 저장 | ORDINAL

enum을 사용할 땐 ORDINAL을 사용하면 안된다.(블로그 사진 참조)

#### @Teamporal
날짜 타입을 매핑할 때 사용된다.
LocalDate, LocalDateTime은 생략이 가능하다.

```JAVA
private LocalDate localDate;

private LocalDateTime localDateTime;

@Temporal(TemporalType.TIMESTAMP) // DATE, TIME 속성도 있다.
private Date createDate;
```

#### @Lob
따로 지정할 수 잇는 속성 없음
매핑이 필드타입이 **문자면 CLOB 매핑**, 나머진 BLOB 매핑

#### @Transient
필드 매핑, DB 저장 조회를 하지 않는다.

---

## 기본키 매핑

#### 기본키 매핑 방법
직접할당: @Id 만 사용

자동 생성: @GeneratedValue
| 속성 | 설명 |
| :--- | :--- |
| IDENTITY | DB에 위임 |
| SEQUENCE | DB 시퀀스 오브젝트 사용 <br> @SequenceGenerator 필요|
| TABLE | 키 생성용 테이블 사용하여 모든 DB에서 사용한다.<br> @TableGenerator 필요|
| AUTO |  방언에 따라 자동 지정한다. **기본값** |

#### IDENTITY
기본 키 생성을 DB에 위임한다.

AUTO_INCREMENT는 JPA는 보통 트랜잭션 커밋 시점에 INSERT SQL을 실행하므로 그 이후에 ID값을 알 수 있다.

허나 IDENTITY는 em.persist() 시점에 INSERT SQL을 실행하고 DB에서 식별자를 조회한다.
PK 값이 DB에 들어가기 전까지  null이 되는데 persist로 영속성 컨텍스트에 넣은 값의 id가 null이 되기 때문에 persist 호출 시 DB에 값이 날라가서 ID값을 가져오게 된다.
```JAVA
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```
