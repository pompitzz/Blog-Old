> [Mockito Docs](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)를 공부하면서 정리한 내용을 작성하는 **첫 번째** 게시글입니다.

### Mockito란?

-   Mockito는 Mock Object를 creation, verification, stubbing 해주는 JAVA에서 사용되는 라이브러리입니다.

#### Mock Object

-   객체 지향 프로그래밍으로 개발한 프로그램을 테스트할 때 테스트를 수행할 모듈과 연결되는 외부의 다른 모듈을 흉내 내는 가짜 모듈을 생성하여 테스트의 효용성을 높이는 데 사용하는 객체입니다.

### 의존성 추가

```java
plugins {
    id 'java'
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation('org.junit.jupiter:junit-jupiter:5.6.0')
    testImplementation('org.assertj:assertj-core:3.11.1')
    testImplementation('org.mockito:mockito-core:3.3.0')
}

test {
    useJUnitPlatform()
}
```

-   JUnit5 기반으로 테스트를 진행하므로 jupiter 엔진과 JunitPlatfrom을 추가하였습니다.
-   Assert 작업은 AssertJ로 진행하므로 AssertJ 의존성을 추가하였습니다.
-   마지막으로 mockito 의존성을 추가하였습니다.

---

### 1\. 기본 동작 방식 확인하기

-   Mock Object를 생성, 검증, 스터빙을 해보도록 하겠습니다.

```java
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class EX1_Mockito {
    @Test
    void stubbing() throws Exception{
        // 1.
        List<String> mockedList = mock(ArrayList.class);

        // 2. stubbing
        when(mockedList.get(0)).thenReturn("First");
        when(mockedList.get(1)).thenThrow(new IndexOutOfBoundsException());

        // 3.
        assertThat(mockedList.get(0)).isEqualTo("First");
        assertThatThrownBy(() -> mockedList.get(1))
                .isInstanceOf(IndexOutOfBoundsException.class);

        // 4.
        assertThat(mockedList.get(10)).isNull();

        // 5.
        verify(mockedList).get(0);
        // verify(mockedList).get(2); 실패!
    }
}

```

1.  mock을 통해 Mock Object를 생성할 수 있습니다.
2.  when을 이용하여 stubbing을 할 수 있습니다. 첫 번째를 보면 mockedList.get(0)이 호출될 때 해당 값은 Frist를 리턴하게 됩니다.
3.  AssertJ를 통해 제대로 동작하는지 확인합니다.
4.  Mock Object는 기본값으로 모두 null 혹은 프리미티브 타입을 리턴하게 됩니다. 그렇기 때문에 10번째 값에 접근해도 예외가 발생하는 게 아닌 null을 리턴하게 됩니다.
5.  verify를 통해 해당 mock object가 사용되었는지 확인할 수 있습니다.

### 2\. Argument Matchers

```java
@Test
void argument_matchers() throws Exception{
    List<String> mockedList = mock(ArrayList.class);

    when(mockedList.get(anyInt())).thenReturn("AnyInt");

    assertThat(mockedList.get(1010)).isEqualTo("AnyInt");

    verify(mockedList).get(anyInt());
}
```

-   ArgumentMatchers를 통해 다양한 방식으로 스터빙을 할 수 있습니다.
-   이 경우 anyInt()를 통해 어떤 인덱스에 접근하여도 AnyInt를 리턴하도록 설정하였습니다.
-   verify시에도 동일하게 사용하여 검증할 수 있습니다.

### 3\. verify 추가 기능

```java
@Test
void verify_mockito() throws Exception{
    List<String> mockedList = mock(ArrayList.class);

    mockedList.add("once");

    mockedList.add("twice");
    mockedList.add("twice");

    mockedList.add("three times");
    mockedList.add("three times");
    mockedList.add("three times");

    // 1.
    verify(mockedList).add("once");
    verify(mockedList, times(2)).add("twice");
    verify(mockedList, times(3)).add("three times");

    // 2.
    verify(mockedList, atLeastOnce()).add("once");
    verify(mockedList, atMostOnce()).add("once");
    verify(mockedList, atLeast(1)).add("twice");
    verify(mockedList, atMost(10)).add("three times");
    verify(mockedList, never()).add("mockito");
}
```

-   verify는 보통 해당 기능이 한번 사용되었는지를 검증합니다.
-   추가 아규먼트로 해당 기능이 몇 번 사용되었는지, 한 번도 사용되지 않았는지 등에 대한 검증을 할 수 있습니다.

1.  times를 통해 몇 번 호출되었는지 검증할 수 있습니다.
2.  atLeast, atMost를 통해 최소, 최대 사용 수를 지정할 수도 있으며 never로 한 번도 사용되지 않음을 검증할 수 있습니다.

### 4\. void mehthod 예외 스터빙

```java
@Test
void doThrow_mockito() throws Exception {
    List<String> mockedList = mock(ArrayList.class);

    doThrow(new RuntimeException("Boom!")).when(mockedList).clear();

    assertThatThrownBy(() -> mockedList.clear())
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Boom!");
}
```

-   void 메서드가 호출될 때 예외를 던지도록 스터빙할 수 있습니다.
-   list.clear()가 사용될 때 RuntimeException을 던지도록 하였습니다.

### 5\. 순서대로 검증하기

```java
@Test
void verify_in_order() throws Exception {
    List<String> firstMockedList = mock(ArrayList.class);
    List<String> secondMockedList = mock(ArrayList.class);

    firstMockedList.add("firstMockList add first");
    firstMockedList.add("firstMockList add second");

    secondMockedList.add("secondMockList add first");
    secondMockedList.add("secondMockList add second");

    // 1.
    final InOrder inOrder = inOrder(firstMockedList, secondMockedList);

    // 2.
    inOrder.verify(firstMockedList).add("firstMockList add first");
    inOrder.verify(firstMockedList).add("firstMockList add second");
    inOrder.verify(secondMockedList).add("secondMockList add first");
    inOrder.verify(secondMockedList).add("secondMockList add second");
}
```

-   기능들이 호출된 순서대로 검증을 진행할 수 있습니다.
-   InOrder Object를 통해 순서대로 검증을 합니다.

1.  순서대로 검증할 대상들을 아규먼트로 하여 inOrder 인스턴스를 생성합니다.
2.  inOrder를 통해 verify를 실행하여 순서대로 검증할 수 있습니다.

### 6\. Mock Annotation

-   Mock Annotation으로 Mock Object를 간편하게 생성할 수 있습니다.

```java
// Product.java
public class Product {
    private Long id;
    private String name;
    private int price;

    public Product() {
    }

    public Product(Long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // getters, setters...
}

// test
@Mock private Product product;

@Test
void mockAnnotation() throws Exception{
    // 1.
    MockitoAnnotations.initMocks(this);

    when(product.getId()).thenReturn(3L);

    assertThat(product.getId()).isEqualTo(3L);

    verify(product).getId();
}
```

-   MockitoAnnotations.initMocks를 통해 애노테이션이 붙은 객체들을 초기화해주어야 합니다.
-   그 후에 해당 객체들은 mock 객체로 사용할 수 있게 됩니다.
-   Product.class는 아래에서 계속해서 사용됩니다.

### 7\. 연속적으로 스터빙하기

-   스터빙시 하나의 호출에 대해 여러 개를 연속적으로 스터빙할 수 있습니다.

```java
@Test
void consecutive_call() throws Exception{

    // 1.
    List<String> mockedList = mock(List.class);
    when(mockedList.get(0))
            .thenReturn("Hello")
            .thenReturn("Dexter")
            .thenThrow(new RuntimeException("Boom!"));

    assertThat(mockedList.get(0)).isEqualTo("Hello");
    assertThat(mockedList.get(0)).isEqualTo("Dexter");
    assertThatThrownBy(() -> mockedList.get(0))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Boom!");

    // 2.
    List<String> mockedList2 = mock(List.class);
    when(mockedList2.get(0))
            .thenReturn("Hello", "Dexter");

    assertThat(mockedList2.get(0)).isEqualTo("Hello");
    assertThat(mockedList2.get(0)).isEqualTo("Dexter");

    // 3.
    List<String> mockedList3 = mock(List.class);
    when(mockedList3.get(0))
            .thenReturn("Hello");
    when(mockedList3.get(0))
            .thenReturn("Dexter");

    assertThat(mockedList2.get(0)).isEqualTo("Dexter");
    assertThat(mockedList2.get(0)).isEqualTo("Dexter");
}
```

**1.**

-   mockList.get(0)이 호출될 때 체이닝을 통해 스터빙 하였습니다.
-   해당 값들은 호출될 때마다 순서대로 정의된 스터빙으로 리턴됩니다.

**2.**

-   스터빙할 리턴 타입이 같을 경우 하나의 메서드에 여러 개의 아규먼트를 전달하여 스터빙을할 수 있습니다.

**3.**

-   체이닝 혹은 여러 아규먼트를 넘기는 게 아닌 when 메서드를 여러 번 호출하면 해당 스터빙은 오버라이드 되어 마지막에 정의된 스터빙만 적용됩니다.

### 8\. 스터빙 커스텀하기

-   thenReturn, thenThrow로 충분히 간단하게 스터빙할 수 있지만 invocation 된 정보들을 가지고 스터빙을 커스텀할 수 있습니다.

```java
@Test
void answer_callback() throws Exception {
    List<String> mockedList = mock(List.class);

    when(mockedList.get(123))
            .thenAnswer(
                    invocation -> {
                        final Object[] arguments = invocation.getArguments();
                        return "전달된 아규먼트들: " + Arrays.toString(arguments);
                    }
            );

    assertThat(mockedList.get(123)).isEqualTo("전달된 아규먼트들: [123]");
}
```

-   thenAnswer로 스터빙을 진행하고 invocation을 파라미터로 가지는 Answer Interface의 익명 클래스를 생성하거나, 위와 같이 람다 표현식으로 스터빙이 가능합니다.

### 9\. 스파이 목 객체

-   spy 메서드를 통해 실제 객체에 대한 목 객체를 생성할 수 있습니다.

```java
@Test
void spy_mock() throws Exception{
    final List<String> nameList = new ArrayList<>();
    final List<String> spy = spy(nameList);

    // 1. IndexOutOfBoundsException 발생!
    // when(spy.get(0)).thenReturn("Dexter");

    // 2.
    doReturn("Dexter").when(spy).get(0);
    assertThat(spy.get(0)).isEqualTo("Dexter");

    // 3.
    assertThatThrownBy(() -> nameList.get(0))
            .isInstanceOf(IndexOutOfBoundsException.class);
}
```

-   nameList에 대한 spy mock object를 생성하였습니다.
-   1\. 과 같이 when을 먼저 사용하는 방식으로 스터빙을하면 예외가 발생합니다. spy는 일반 mock object처럼 기본값이 지정되어 있지 않기 때문입니다.
-   그러므로 2. 와 같이 doReturn을 먼저 사용하는 스터빙방식을 사용하여야 합니다.
-   3\. 을 보면 실제 객체는 스터빙이 되지 않을 것을 알 수 있습니다. 이를 통해 spy object는 실제 객체를 복사하는 또 다른 객체를 만든 것을 알 수 있습니다.

### 10\. mock 객체 생성 전략

-   mock 객체를 생성하면 기본전략일 경우 객체의 멤버들의 데이터들이 프리미티브 타입을 제외하면 null을 반환하는 것을 위에서 확인하였습니다.
-   이러한 생성 전략은 따로 Answer Interface를 통해 생성할 수 있으며 혹은 mockito에서 제공해주는 다양한 전략을 사용할 수도 있습니다.
-   SMART\_NULLS 전략을 사용해보면서 해당 전략에 대해 알아보겠습니다.

```java
@Test
void default_returnMock() throws Exception {
    // 1.
    final Product mock = mock(Product.class);
    assertThat(mock.getName()).isNull();
    assertThatThrownBy(() -> mock.getName().length())
            .isInstanceOf(NullPointerException.class);

    // 2.
    final Product mockWithSmartNulls = mock(Product.class, RETURNS_SMART_NULLS);
    assertThat(mockWithSmartNulls.getName()).isNotNull();
    assertThat(mockWithSmartNulls.getName().length()).isEqualTo(0);
}
```

1.  기본 전략으로 목 객체를 생성할 경우 필드의 값들은 null이 되기 때문에 NullPointerException이 발생할 수 있습니다.
2.  SMART\_NULLS 전략으로 목 객체를 생성하면 null을 반환하지 않고 좀 더 안전하게 사용할 수 있는 방법을 제공하여 NPE이 발생하지 않는 것을 알 수 있습니다.

> 이외에도 많은 전략들이 존재하지만 레거시 시스템 테스트가 아닐 경우 필요 없을 것이라고 알려져 있습니다.
