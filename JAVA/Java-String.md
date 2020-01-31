# JAVA의 String
> String의 두 가지 생성 방법과 그 차이에 대해 알아보고 플라이 웨이트 패턴에 대해 알아본다.



- String을 생성하는 방법은 대표적으로 두가지가 존재한다. ""을 통해 리터럴로 생성할 수 있고 new String을 통해 생성할 수 있다.

<br>

```java
@Test
public void test() throws Exception {

    String str1 = "Hello";
    String str2 = "Hello";
    String str3 = new String("Hello");


    assertThat(str1).isSameAs(str2);

    // 서로 다르다!
    assertThat(str1).isNotSameAs(str3);
}
```
- 위와 같이 테스트를 작성해보면 마지막 테스트 코드는 isNotSameAs이나 테스트가 통과 된다.

<br>

#### 이유는 JVM의 constant pool에의해 발생하는 것이다.
- 클래스가 JVM에 로드되면 모든 리터럴은 constant pool에 위치하게 된다.
- 그리고 리터럴을 통해 같은 문자를 생성한다면 풀안의 같은 상수를 참조하게되는데 이를 String interning이라고 한다.

<br>

```JAVA
String str1 = "Hello";
String str2 = "Hello";
```

- String을 리터럴로 생성될 때 intern()이라는 메서드가 호출되고 이 intern() 메서드는 constant pool에 같은 문자가 존재하는지 확인 후 존재 한다면 그 참조 값을 가지게 된다.

- 즉 위와 같이 Hello를 두번 생성하면 inter()에 의해 str1과 str2는 같은 참조 값을 가지게 되는 것이다.

<br>

#### intern() 직접 호출하기
```JAVA
@Test
public void test() throws Exception {

    String str1 = "Hello";
    String str2 = "Hello";
    String str3 = new String("Hello");

    assertThat(str1).isSameAs(str2);
    assertThat(str1).isNotSameAs(str3);

    String intern = str3.intern();
    assertThat(str1).isSameAs(intern);
}
```
- new String으로 생성한 str3에 intern()을 호출하고 반환된 값과 str1을 비교해보면 같은 참조값을 가지게 된다.


> String의 constant pool은 플라이 웨이트 패턴을 구현한 것이다.


#### 플라이웨이트 패턴
- 플라이 웨이트 패턴은 동일하거나 유사한 객체들 사이에 가능한 많은 데이터를 공유하여 메모리 사용을 최소화 하는 디자인 패턴이다.
- Integer.valueOf도 플라이 웨이트 패턴을 이용하여 -128 ~ 127 사이의 값은 캐시하여 같은 참조 값을 가지게 만들어 져있다.
- 아래와 같이 123일때는 같으나 322일때는 다른 참조값을 가지게 되는 것을 알 수 있고 Interger valueOf 메서드를 확인 해보면 캐시를 이용하여 값을 반환하는 것을 알 수 있다.

```JAVA
@Test
public void test() throws Exception {
    Integer integer1 = Integer.valueOf("123");
    Integer integer2 = Integer.valueOf("123");
    assertThat(integer1).isSameAs(integer2);


    Integer integer3 = Integer.valueOf("322");
    Integer integer4 = Integer.valueOf("322");
    assertThat(integer3).isNotSameAs(integer4);
}



// Integer의 valueOf 메서드
public static Integer valueOf(int i) {
     if (i >= IntegerCache.low && i <= IntegerCache.high)
         return IntegerCache.cache[i + (-IntegerCache.low)];
     return new Integer(i);
}
```
