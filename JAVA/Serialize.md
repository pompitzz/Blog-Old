# 자바 직렬화([참고 자료](https://woowabros.github.io/experience/2017/10/17/java-serialize2.html))
- 자바 시스템에서 사용되는 객체들을 외부의 자바 시스템에서도 사용할 수 있데 byte 형태로 데이터를 변환하고, byte 형태의 데이터를 다시 객체로 변환하는 기술
- 즉 JVM의 메모리에 상주되어있는 객체 데이터들을 byte 형태로 변환해서 JVM 외부에서 사용할 수 있게하고, byte 형태의 데이터를 객체로 변환해서 JVM의 메모리 영역에 상주시킬 수 있게 변환하는 기술

### 직렬화
- 해당 객체가 Serializable을 구현하고 있으면 직렬화가 가능하다.
- ObjectOutputStream를 통해 직렬화를 할 수 있다.

```java
// 직렬화
final Member dexter = new Member("Dexter", "dongmyeong.lee22@gmail.com", 10);

byte[] serializedMember = new byte[0];
try(final ByteArrayOutputStream baos = new ByteArrayOutputStream()){
    try(final ObjectOutputStream oos = new ObjectOutputStream(baos)) {
        oos.writeObject(dexter);
        serializedMember = baos.toByteArray();
    }
} catch (IOException e) {
    e.printStackTrace();
}

// 직렬화된 byte를 string으로 변환
final String base64Member = Base64.getEncoder().encodeToString(serializedMember);
```

### 역직렬화
- 역직렬화시 우선 직렬화 대상의 객체의 클래스가 존재해야하며 import되어 있어야 한다.
- 자바 직렬화 대상 객체는 동일한 serialVersionUID를 가지고 있어야 한다.
  - serialVersionUID는 따로 명시하지 않아도 알아서 필드 멤버의 값을 해싱하여 구해준다.
  - **하지만 그렇게하면 역 직렬화시 멤버 변수가 완벽하게 일치해야 하므로 따로 설정하는게 좋다.**
  - private static final long serialVersionUID = 1L;

```java
// 직렬화된 byte를 string으로 변환
final String base64Member = Base64.getEncoder().encodeToString(serializedMember);

// 역 직렬화
final byte[] decodedSerialziedMember = Base64.getDecoder().decode(base64Member);
try(final ByteArrayInputStream bais = new ByteArrayInputStream(decodedSerialziedMember)){
    try(final ObjectInputStream ois = new ObjectInputStream(bais)){
        final Object obejctMember = ois.readObject();
        final Member deserializedMember = (Member) obejctMember;
        System.out.println("deserializedMember = " + deserializedMember);
    }
} catch (IOException | ClassNotFoundException e) {
    e.printStackTrace();
}
```

### 자바 직렬화와 문자형 형태의 직렬화
- 사실 보통 JSON과 같은 문자형 형태의 직렬화로 직렬화를 수행한다.
- 보통 자바 직렬화는 **자바 시스템 간의 데이터 교환을 위해** 사용한다.
- 문자열 형태의 직렬화 방식은 **모든 시스템 간의 데이터 교환을 위해** 사용한다.

### 자바 직렬화를 굳이 쓰는 이유
- 자바 직렬화는 복잡한 데이터 구조의 클래스 객체라도 Serializable만 구현한다면 간단하게 직렬화, 역직렬화가 가능하다.
- 그렇기 때문에 **서블릿 세션을 쓸때** 메모리가 아닌 DB에 저장해야할 일이 있다면 자바 직렬화를 사용한다.
- 그리고 **캐시 시스템을 사용할 때** 간편하게 자바 직렬화로 구현할 수 있다.

### 자바 직렬화의 단점
- serialVersionUID를 따로 설정하지 않았다면 멤버 변수와 완벽하게 일치하지 않을 시 예외가 발생한다.
- serialVersionUID를 따로 설정하였다면 멤버 변수가 사라져도 기존의 멤버의 값은 추가되고, 새로운 변수가 추가되어도 null값으로 초기화되지만 int -> long 같은 **타입 변화에도** 예외를 발생 시킨다.
- 즉 **자주 변경되는 객체일 경우 자바 직렬화는 문제를 발생시킬 여지가 많다.**
- 자바 직렬화와 문자열 형태(JSON)의 직렬화의 바이트 수를 비교해보면 기본 타입만 존재하는 단순한 객체에도 약 두배가량 차이가 발생한다. 그러므로 **용량이 중요할 때** 캐시 시스템등이 자바 직렬화로 구현되어있는지 확인하는게 좋다.
- 자바 직렬화는 **다른 프로그래밍 언어 시스템에서 역직렬화하기 어렵다**

#### 용량 차이 확인
```java
public static void main(String[] args) throws IOException {
        final Member dexter = new Member("Dexter", "dongmyeong.lee22@gmail.com", 10);

        // 직렬화
        byte[] serializedMember = new byte[0];
        try(final ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            try(final ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(dexter);
                serializedMember = baos.toByteArray();
            }
        }

        System.out.println("Java Serizalize Byte Length = " + serializedMember.length);

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("Json Serialize Byte Length = " + objectMapper.writeValueAsBytes(dexter).length);
}

// 출력
Java Serizalize Byte Length = 116
Json Serialize Byte Length = 63
```
