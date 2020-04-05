# Vavr User Guide

## Vavr
- Vavr는 함수 제어 구조와 영구적인 데이터 타입을 제공해주는 JAVA 8+를 위한 함수형 라이브러리입니다.
- Vavr는 함수 패턴을 기반으로한 람다를 활용하여 다양한 새로운 기능들을 제공해줍니다.
- 그 중 하나는 JAVA 표준 컬렉션 라이브러리를 대체하기 위한 함수 컬렉션 라이브러리입니다.

### 1. 함수형 프로그래밍
- vavr에 대해 알아보기 전에 함수형 프로그래밍의 기초에 대해 알아보면서 왜 자바 컬렉션 라이브러리를 대체할 수 있는 vavr를 만들었는지에 대해 알아보겠습니다.

#### 1) Side-Effects(부수 효과)
- 자바는 전형적으로 Side-Effects가 많이 존재합니다.
- 흔히 Side-Effects는 객체나 변수들을 변경시키고 만약 Side-Effects로 인해 의도하지 않은 변경들이 일어나는 경우 이는 시스템에 피해를 줄 수 있습니다..

```JAVA
int divide(int dividend, int divisor){
  return a / b;
}
```
- 이 함수는 divisor가 0일 때 예외를 발생시킵니다.
- 그리고 이런 예외는 프로그램에 영향을 끼칠 수 있으며 정상적인 흐름으로 동작되지 않게 됩니다.
- 아래와 같이 vavr의 Try를 이용하면 예외가 발생하더라도 예외가 던져지지 않으며 개발자가 원하는 흐름으로 제어할 수 있습니다.

```JAVA
Try<Integer> divide(Integer dividend, Integer divisor){
  return Try.of(() -> dividend / divisor);
}
```

#### 2) Referential Transparency(참조 투명성)
- 참조 투명성이란 간단히 말하자면 같은 입력이 들어왔을 때 항상 같은 출력이 나오는 성질이라고 할 수 있습니다.
- 어떤 함수가 외부의 영향을 받지 않고 해당 함수의 파라미터만을 사용한다면 참조 투명성을 가질 수 있을 것입니다.
- 함수의 모든 표현식이 참조 투명성을 가진다면 이러한 함수를 pure하다라고 할 수 있으며 pure한 함수는 외부의 영향을 받지 않기때문에 테스트가 쉬워집니다.

```JAVA
Math.random();
// random()은 같은 입력이 들어오더라고 다른 값들이 나오기 때문에 참조 투명성이 없습니다.

Math.max(1, 2);
// max()는 파라미터만을 사용하여 항상 같은 입력에 대해 같은 출력을 반환하기 때문에 참조 투명성을 가진다고 할 수 있습니다.
```

#### 3) immutable values(불변 값)
- 불변값들은 본질적으로 Thread-safe하기 때문에 동기화가 필요 없습니다.
- 불변하므로 equals와 hashCode가 안정적이므로 hashkey를 신뢰할 수 있습니다.
- 복사될 필요가 없으며 타입 변환 시 안정적으로 동작될 수 있습니다.

> 그러므로 자바에서 참조 투명성을 가지는 함수와 불변 값을 사용하는 것이 좋을 것이고 Vavr는 이러한 조건들을 만족시키기 위해 필요한 Controls, Collections들을 제공합니다.

### 2. 데이터 구조
- Vavr의 컬렉션 라이브버리는 람다로 구축 된 다양한 함수 데이터 구조를 제공합니다.
- 자바의 컬렉션 라이브러리와 동일한 인터페이스는 Iterable만 존재하며 그 이유는 자바 컬렉션 인터페이스들의 메서드가 변화를 예측할 수 없는 void 타입을 반환하는 데이터들을 제공하기 때문입니다.

#### 1) Mutable Data Structures(가변 데이터 구조)
- 자바는 객체지향 언어로 데이터를 숨기기위해 객체의 상태를 캡슐화하고 해당 상태를 제어할 수 있는 퍼블릭 인터페이스인 메서드들을 제공합니다.
- void clear()와 같은 메서드들은 부수 효과를 발생시키고 변화를 예측할 수 없게 만듭니다.

#### 2) Immutable Data Structures(불변 데이터 구조)
- 불변 데이터 구조는 한번 생성된 후 수정이 불가능합니다.

#### 3) Persistent Data Structures(영구적인 데이터 구조)
- 영구적인 데이터 구조는 변경이 일어나더라도 이전 상태를 보관하기 때문에 사실상 불변하다고 할 수 있습니다.
- 모든 영구적인 데이터들은 변경 및 조회를 할 수 있습니다.
- 작은 변화에도 많은 동작이 일어나야하기 때문에 메모리와 시간을 효율적으로 관리하기 위해선 필요한 데이터를 최대한 공유해야 합니다.

#### 4) Functional Data Structures(함수형 데이터 구조)
- 함수형 데이터 구조는 불변성, 영구적인 데이터 구조와 참조 투명성을 가지고 있습니다.
- Vavr는 가장 일반적으로 사용되는 함수형 데이터 구조를 특징으로 구성됩니다.

---

## Vavr 사용해보기

- Vavr는 그림과 같이 Tuple, Lambda, Value를 기반으로 만들어져 있습니다.

```groovy
dependencies {
    implementation('io.vavr:vavr:0.9.3')

    testImplementation('org.junit.jupiter:junit-jupiter:5.6.0')
    testImplementation('org.assertj:assertj-core:3.11.1')
}

test {
    useJUnitPlatform()
}
```
- gradle 기준으로 의존성을 추가해줍니다. 테스트 작성을 위해 테스트 의존성도 함께 추가해줍니다.

### 1. Tuples(튜플)
- 자바에는 일반적인 튜플의 개념이 존재하지 않습니다.
- 배열, 리스트와 달리 튜플은 다른 타입의 객체들을 가질 수 있으며 해당 값들은 불변합니다.


#### 1) Create a tuple
```JAVA
@Test
void tuple() throws Exception{
    Tuple2<String, Integer> hello10 = Tuple.of("Hello", 10);

    String hello = hello10._1;
    Integer ten = hello10._2;

    assertThat(hello).isEqualTo("Hello");
    assertThat(ten).isEqualTo(10);
}
```
- Tuple.of로 튜플을 생성할 수 있고 tuple.\_1, \_2으로 튜플내부에 있는 객체를 꺼내올 수 있습니다.

#### 2) Map and Transfrom a tuple

```JAVA
@Test
void tuple() throws Exception{
    Tuple2<String, Integer> hello10 = Tuple.of("Hello", 10);

    // 각 요소별로 매핑하기
    Tuple2<String, Integer> helloMap15 =
            hello10.map(
                    s -> s + " Map",
                    i -> i + 5
            );

    assertThat(helloMap15._1).isEqualTo("Hello Map");
    assertThat(helloMap15._2).isEqualTo(15);

    // 한번에 매핑하기
    Tuple2<String, Integer> helloMap15V2 =
            hello10.map((s, i) -> Tuple.of(s + " Map", i + 5));
    assertThat(helloMap15V2._1).isEqualTo("Hello Map");
    assertThat(helloMap15V2._2).isEqualTo(15);

    // 새로운 타입으로 변환하기
    String hello = hello10.apply((s, i) -> s + i);
    assertThat(hello).isEqualTo("Hello10");
}
```
- 각 요소별, 혹은 한번에 매핑하는 것도 가능합니다.
- 뿐만아니라 apply를 통해 새로운 타입으로 변환도 가능합니다.

### 2. Functions
- 자바 Function에서는 두 개의 매개변수를 사용할 수 있는 BiFunction을 제공하지만 Vavr에서는 최대 8개의 매개 변수를 사용할 수 있는 기능을 제공합니다.
- Function0, 1, 2등으로 사용할 수 있고 예외 처리가 필요할 경우는 CheckedFunction1, 2 등을 제공합니다.


#### 1) Composition
- 여러 Function들을 하나의 Function으로 조합시키는 기능이 존재합니다.

```JAVA
@Test
void function() throws Exception{
    Function1<Integer, Integer> addOne = i -> i + 1;
    Function1<Integer, Integer> multiplyByTwo = i -> i * 2;

    // andThen은 호출하는 객체가 먼저 적용된다.
    Function1<Integer, Integer> add1AndMultiplyBy2 = addOne.andThen(multiplyByTwo);
    Integer answer = add1AndMultiplyBy2.apply(3);
    assertThat(answer).isEqualTo(8);
    assertThat(add1AndMultiplyBy2.apply(4)).isEqualTo(10);

    // compose는 넘겨주는 전달인자부터 적용된다.
    Function1<Integer, Integer> add1AndMultiplyBy2V2 = multiplyByTwo.compose(addOne);
    Integer answer2 = add1AndMultiplyBy2V2.apply(3);
    assertThat(answer2).isEqualTo(8);
    assertThat(add1AndMultiplyBy2V2.apply(4)).isEqualTo(10);
}
```
- andThen, compose 메서드를 이용하면 Function들을 합칠 수 있습니다.

#### 2) Lifting
- Function의 반환 타입을 Option타입으로 감싸여 반환하게 할 수 있습니다.
- 이는 자바 Optional과 같이 유연한 예외, Null처리가 가능해질 것입니다.

```JAVA
@Test
void function2() throws Exception{
    Function2<Integer, Integer, Integer> divide = (a, b) -> a / b;
    Function2<Integer, Integer, Option<Integer>> safeDivide = Function2.lift(divide);

    // lift함수를 이용하여 만들어진 Function의 반환값은 안전하다.
    Option<Integer> divide0 = safeDivide.apply(1, 0);
    Integer isNull = divide0.getOrNull();
    assertThat(isNull).isNull();
}
```

#### 3) Partial Apply(부분 적용 함수), Currying(커링 함수)
- Function으로 부분 적용 함수와 커링 함수를 간단하게 만들 수 있습니다.
```JAVA
@Test
void function3() throws Exception{
    Function4<Integer, Integer, Integer, Integer, Integer> sum
            = (a, b, c, d) -> a + b + c + d;

    // 부분 적용 함수
    Function2<Integer, Integer, Integer> sum3 = sum.apply(1, 2);
    assertThat(sum3.apply(3, 4)).isEqualTo(10);

    // 커링 함수
    Function1<Integer, Function1<Integer, Integer>> currying = sum.curried().apply(1).apply(2);
    assertThat(currying.apply(3).apply(4)).isEqualTo(10);
}
```
- 부분 적용 함수는 별다른 기능 호출없이 apply를 통해 즉시 만들 수 있습니다.
- Function의 모든 매개변수를 넘겨주지 않으면 넘겨준 매개변수가 적용된 Function을 반환해 줍니다.
- curried() 메서드를 이용하면 커링함수로 만들 수 있습니다.
- 커링함수는 부분 적용 함수와는 다르게 매번 하나의 매개변수만 넘길 수 있습니다.

#### 4) Memorization
- 캐싱 역할을하는 기능도 제공됩니다.

```JAVA
@Test
void function4() throws Exception{
    Function0<Double> cachedValue = Function0.of(Math::random).memoized();
    Double random1 = cachedValue.apply();
    Double random2 = cachedValue.apply();
    assertThat(random1).isEqualTo(random2);
}
```
- memoized 메서드이용하면 해당 결과는 캐싱됩니다.
- 그러므로 random1, 2가 동일한 값을 가지고 있습니다.

### 3. Values
- Vavr의 Value들은 불변하므로 메모리에서 공유하여 사용하더라도 Thread-safe하게 사용할 수 있습니다.

#### 1) Option
- Option은 자바의 Optional과 유사하지만 약간의 차이가 존재합니다.
- Option은 Some(값이 존재) 혹은 None을(값이 없음) 가지게 됩니다.

```JAVA
@Test
void option() throws Exception {
    Optional<String> foo = Optional.of("foo");

    // Optional은 NULL이 되면 notpresent가 된다.  
    Optional<String> maybeBar = foo
                                    .map(s -> (String) null)
                                    .map(s -> s.toUpperCase() + "bar");
    assertThat(maybeBar.isPresent()).isFalse();

    Option<String> foo2 = Option.of("foo");

    // Option은 NULL이여도 Some(Null)과 같이 값으로 취급하기때문에 NPE가 발생한다.
    assertThatThrownBy(() -> {
        Option<String> maybeBar2 = foo2
                .map(s -> (String) null)
                .map(s -> s.toUpperCase() + "bar");
    }).isInstanceOf(NullPointerException.class);
}
```
- Option의 경우 예외가 발생하는 것을 알 수 있습니다.
- Optional에 비해 쓸모없다고 생각할 수 있지만 null처리는 의식적으로 처리하는게 좋기때문에 flatMap을 통해 null처리를 할 수 있습니다.

```JAVA
@Test
void option_flatMap() throws Exception{
    Option<String> foo = Option.of("foo");

    Option<String> maybeBar = foo.map(s -> (String) null)
                                 .flatMap(s -> Option.of(s))
                                 .map(s -> s.toUpperCase() + "bar");

    Option<String> maybeBar2 = foo.flatMap(s -> Option.of((String) null))
                                  .map(s -> s.toUpperCase() + "bar");

    assertThat(maybeBar.isEmpty()).isTrue();
    assertThat(maybeBar2.isEmpty()).isTrue();
}
```
- flatMap을 이용하면 NPE를 발생시키지 않게할 수 있습니다.

#### 2) Try
- Try는 정상적인 반환 값 혹은 예외 발생에 대한 계산을 나타내는 모나드 컨테이너 유형입니다.

```java
@Test
void try_vavr() throws Exception {
    String exception_2 = Try.of(this::throwException)
                            // x는 Throwble로 try에서 던져지는 예외를 가진다.
                            // x를 Match와 Case를 이용하여 각 예외별로 다른 처리를 진행항 수 잇다.
                            .recover(x -> Match(x).of(
                                    Case($(instanceOf(Exception_1.class)), this::somethingWithException),
                                    Case($(instanceOf(Exception_2.class)), this::somethingWithException),
                                    Case($(instanceOf(Exception_3.class)), this::somethingWithException)
                            ))
                            .getOrElse("Else");

    assertThat(exception_2).isEqualTo(Exception_2.class.getSimpleName());
}

private String somethingWithException(Exception t) {
    return t.getClass().getSimpleName();
}


private String throwException() {
    if (true) throw new Exception_2();
    return "Str";
}


static class Exception_1 extends RuntimeException { }

static class Exception_2 extends RuntimeException { }

static class Exception_3 extends RuntimeException { }
```
- Try.of에 잇는 throwException은 Exception_2를 던지기 때문에 String은 somethingWitException 메서드에의해 Excpetion_2의 이름을 가지는 것을 알 수 있습니다.

#### 3) Lazy
- Lazy는 Supplier와 같이 지연 계산을 하는 기능을 가지고 있습니다.
- 추가적으로 결과를 캐싱하는 성질을 가집니다.


```JAVA
@Test
void lazy() throws Exception{
    Lazy<Double> lazy = Lazy.of(Math::random);

    // 호출이 한 번도 되지 않았으니 fasle이다.
    assertThat(lazy.isEvaluated()).isFalse();
    Double first = lazy.get();

    // 호출되었으니 true가 된다.
    assertThat(lazy.isEvaluated()).isTrue();
    Double second = lazy.get();

    // 값을 캐싱하기 때문에 동일한 값을 가지고 있다.
    assertThat(first).isEqualTo(second);
}
```
