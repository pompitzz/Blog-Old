# 토비의 봄

### Method Signature
- name, parameter types
- return type은 Method Signature가 아니다.
- Signature가 같다면 한 클래스내에 똑같이 정의될 수 없다.
- return은 이것을 통해 아니라는 것을 알 수 있다.

### Method Type
- return type, method type parameter, method argument types, exception
- 이 네가지(Method Type)를 만족하면 Method Reference가 된다.

```java
svc.forEach( s -> s.run());
svc.forEach(Service::run);
```

### 제네릭과 가변성(variance)
[참고]](http://happinessoncode.com/2017/05/21/java-generic-and-variance-1/#%EC%B0%B8%EA%B3%A0-%EB%AC%B8%EC%84%9C)

### 메서드 오버로딩
- 메서드 오버로딩은 스태틱 디스패칭(컴파일 시점에 파라미터 타입을 정한다)을 한다.
- dispath(s)에서 s가 인터페이스인데 실제 인터페이스의 dispath메서드에는 s를 받는 메서드는 없고 s의 자식들을 받는 메서드만 있으면 컴파일 자체가 안된다.
- 자바는 싱글 디스패치 언어 즉 리시버가 하나 뿐이다.
- 어떤 메서드를 고를까 결정이되는 조건은 리시버 파라미터 하나 뿐이다.
- 그러므로 파라미터가 컴파일 시점에 결정되어 있지 않으면 컴파일에러가 나타난다.
