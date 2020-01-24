# JPA의 값 타입
> JPA는 데이터 타입을 엔티티 타입, 값 타입으로 분류한다.

엔티티 타입
- @Entity로 정의한 객체로 데이터가 변해도 식별자로 지속해서 추적이 가능하다.
- 즉 아이템 엔티티의 값을 변경하면 식별자를 통해 인식이 가능하다.

값 타입
- int, String 등과 같이 단순히 값으로 사용하는 자바의 기본 타입이나 객체이다.
- 식별자가 없고 값만 존재하므로 값이 변경되면 추적이 불가능하고 다른 값으로 교체된다.

> 값 타입은 기본 값 타입과 임베디드 타입으로 나뉜다.

### 기본 값 타입

```java
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    private int age;
}
```

- Member Entity에 존재하는 name, age가 기본 값 타입이다.
- Member를 삭제하면 그에 대한 name, age도 같이 삭제되므로 생명주기를 엔티티에 의존한다고 할 수 있다.

### 임베디드 타입
