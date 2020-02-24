> [Mockito Docs](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)를 공부하면서 정리한 내용을 작성하는 **두 번째** 게시글입니다.

> [첫 번째 게시글 바로가기](https://sun-22.tistory.com/93)

### 11\. ArgumentCaptor

-   보통 verify시 아규먼트 값을 직접 지정하지만 ArgumentCaptor를 사용하면 capture를 통해 유연하게 아규먼트 값을 넘길 수 있습니다.

```java
@Test
void capture() throws Exception{
    // stubbing
    final List<String> mockedList = mock(List.class);
    when(mockedList.get(1)).thenReturn("A");
    when(mockedList.get(2)).thenReturn("B");
    when(mockedList.get(3)).thenReturn("C");

    // 1.
    ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

    assertThat(mockedList.get(1)).isEqualTo("A");
    assertThat(mockedList.get(3)).isEqualTo("C");
    assertThat(mockedList.get(2)).isEqualTo("B");

    // 2.
    verify(mockedList, times(3)).get(integerArgumentCaptor.capture());
    // verify(mockedList).get(1);
    // verify(mockedList).get(2);
    // verify(mockedList).get(3);

    // 3.
    final List<Integer> allValues = integerArgumentCaptor.getAllValues();
    assertThat(allValues).isEqualTo(Arrays.asList(1, 3, 2));
}
```

1.  원하는 타입의 ArgumentCaptor를 생성합니다.
2.  원래라면 주석 처리된 verify처럼 검증을 해야 하지만 capture()를 통해 한번에 검증할 수 있습니다.
3.  capture 된 값들을 가져와 정확히 동작하였는지 테스트할 수 있습니다.

### 12\. Resetting Mocks

-   mock의 스터빙 정보들을 리셋할 수 있는 방법이 존재합니다.
-   하지만 리셋 후 또 테스트를 한다는 것은 한 테스트에 너무 많은 테스트를 하고 있다는 증거가 될 수 있습니다.
-   가능한 사용하지 않는 게 좋다고 설명되어 있습니다.

```java
@Test
void reset_mock() throws Exception{
    final Product mock = mock(Product.class);

    when(mock.getName()).thenReturn("Dexter");
    assertThat(mock.getName()).isEqualTo("Dexter");

    reset(mock);
    assertThat(mock.getName()).isNull();
}
```

-   reset(mock) 이후는 mock 스터빙 정보가 초기화되어 name이 null이 되는 것을 확인할 수 있습니다.

### 13\. Behavior Driven Development 스타일로 테스트하기

-   BDD는 보통 given, when, then 구조로 테스트를 진행하는 것으로 알고 있습니다.
-   하지만 현재 mock은 BDD의 given 구역에서 스터빙시 when().thenReturn()을 사용하여 혼란을 줄 수 있습니다.
-   BDDMockito를 사용하면 스터빙을 given().willReturn()으로 할 수 있습니다.

```java
import org.junit.jupiter.api.*;
import org.mockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

public class EX2_Mockito_BDD {

    @Mock Product product;

    @BeforeEach
    void init(){
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void BDD_test() throws Exception{
        //given
        given(product.getName()).willReturn("Dexter");

        //when
        final String name = product.getName();

        //then
        assertThat(name).isEqualTo("Dexter");

        then(product).should(times(1)).getName(); // verify랑 같은 기능!
    }
}
```

-   given().willReturn()으로 스터빙을 하기 때문에 BDD 개발 시 혼란의 여지가 줄고 가독성 있게 개발이 가능합니다.

### 14\. @Captor, @Spy, @InjectMocks

-   위에서 @Mock을 사용하여 Mock 객체를 생성해 본 적이 있습니다.
-   이외에도 Captor, Spy, InjectMocks Annotation을 추가로 사용할 수 있습니다.
-   이 Annotation도 @Mock과 같이 MockitoAnnotations.initMocks(Object)로 초기화됩니다.

#### **@Captor**

```java
@Captor
ArgumentCaptor<Integer> integerArgumentCaptor;

@Test
void capture() throws Exception {
    MockitoAnnotations.initMocks(this);

    final List<String> mockedList = mock(List.class);
    when(mockedList.get(1)).thenReturn("A");
    when(mockedList.get(2)).thenReturn("B");
    when(mockedList.get(3)).thenReturn("C");

    assertThat(mockedList.get(1)).isEqualTo("A");
    assertThat(mockedList.get(3)).isEqualTo("C");
    assertThat(mockedList.get(2)).isEqualTo("B");

    verify(mockedList, times(3)).get(integerArgumentCaptor.capture());

    final List<Integer> allValues = integerArgumentCaptor.getAllValues();
    assertThat(allValues).isEqualTo(Arrays.asList(1, 3, 2));
}
```

-   11\. ArgumentCaptor에서 사용한 ArgumentCaptor를 @Captor로 생성하여도 동일하게 동작함을 확인할 수 있습니다.

#### **@Spy**

```java
@Spy
Product productSpy = new Product(1L, "Dexter", 1000);

@Spy
Product productSpyWithNoArgCon; // new Product();

@Test
void spy_annotation() throws Exception {
    MockitoAnnotations.initMocks(this);

    assertThat(productSpy.getName()).isEqualTo("Dexter");
    assertThat(productSpyWithNoArgCon.getName()).isNull();

    when(productSpy.getName()).thenReturn("James");
    when(productSpyWithNoArgCon.getName()).thenReturn("James");

    assertThat(productSpy.getName()).isEqualTo("James");
    assertThat(productSpyWithNoArgCon.getName()).isEqualTo("James");
}
```

-   위에서 살펴본 spy(Object)로 생성하였던 spy mock 객체를 @Spy로 간단하게 생성할 수 있습니다.
-   따로 복사할 인스턴스를 지정하지 않으면 기본 생성자로 생성된 인스턴스를 복사하여 mock 객체로 만들게 됩니다.

#### **@InjectMocks**

-   InjectMocks은 위에서 따로 다루지 않았던 기능입니다.
-   InjectMocks Annotation으로 객체를 생성하면 해당 객체의 멤버 변수로 존재하는 의존된 다른 객체들이 mock혹은 spy로 생성된 객체라면 의존성 주입을 해주는 기능을 제공합니다.
-   간단한 예를 통해 기능을 알아보겠습니다.

```java
class Order{
    private Item item;

    public Order(Item item) {
        this.item = item;
    }

    public Order() {
    }

    public String itemCheck(final String name){
        final boolean sameName = item.isSameName(name);

        if (sameName) return "해당 아이템이 주문되었습니다.";

        return "주문된 아이템이 아닙니다.";
    }
}


class Item{
    private String name;

    public boolean isSameName(final String name){
        return this.name.equals(name);
    }
}
```

-   Order는 파라미터로 name을 가지는 itemCheck method를 가지고 있습니다.
-   itemCheck가 호출되면 Order는 Item의 isSameName()을 호출하고 해당 호출 결과에 따라 리턴 값이 달라지게 됩니다.
-   먼저 InjectMocks Annotation을 사용하지 않고 테스트를 작성해보겠습니다.

```java
public class EX3_Mockito_InjectMocks {

    @Mock
    Item item;

    @Test
    void non_injectMocks_Annotation() throws Exception{
        MockitoAnnotations.initMocks(this);

        final Order order = new Order(item);

        when(item.isSameName("Book")).thenReturn(true);

        assertThat(order.itemCheck("Book")).isEqualTo("해당 아이템이 주문되었습니다.");
        assertThat(order.itemCheck("Movie")).isEqualTo("주문된 아이템이 아닙니다.");

    }
}
```

-   item을 mock 객체로 생성하고 order를 생성할 때 item을 넣어 생성한 후 테스트를 진행할 수 있을 것입니다.
-   이제 InjectMocks Annotation을 사용해보겠습니다.

```java
public class EX3_Mockito_InjectMocks {

    @Mock
    Item item;

    @InjectMocks
    Order order;

    @Test
    void injectMocks_Annotation() throws Exception{
        MockitoAnnotations.initMocks(this);

        when(item.isSameName("Book")).thenReturn(true);

        assertThat(order.itemCheck("Book")).isEqualTo("해당 아이템이 주문되었습니다.");
        assertThat(order.itemCheck("Movie")).isEqualTo("주문된 아이템이 아닙니다.");
    }
}
```

-   InjectMocks Annotation을 사용하면 따로 Order를 생성할 때 의존성을 주입하지 않아도 mock이나 spy 객체로 생성된 객체가 있다면 의존성을 주입해 줍니다.
-   그러므로 위와 같이 테스트를 진행할 수 있습니다.

### 15\. Ignore Stubbing

-   더 이상 verify 할 mock이 없을 때를 확인할 수 있는 verifyNoMoreInteractions()가 존재합니다.
-   ignoresubs()을 이용하면 stubbing 된 값들을 따로 verify하지 않아도 ignore 되므로 verifyNoMoreInteractions()를 통과시킬 수 있습니다.

```java
@Test
void ignore_stubbing() throws Exception{
    final List<String> mock = mock(List.class);

    when(mock.get(0)).thenReturn("Hello");
    final String s = mock.get(0);

    ignoreStubs(mock);
    // verify(mock).get(0); 하지 않아도 통과!

    verifyNoMoreInteractions(mock);
}
```

-   ignoreStubs를 사용하면 stubbing 된 값을 검증하지 않고도 verifyNoMoreInteractions를 사용할 수 있습니다.

### 16\. Mocking details

-   Mock 객체의 세부사항들을 테스트할 수 있는 MockingDetails를 제공합니다.

```java
@Mock
Product mockedProduct;

@Test
void mocking_details() throws Exception {
    MockitoAnnotations.initMocks(this);
    final MockingDetails mockingDetails = mockingDetails(mockedProduct);

    // 1.
    final boolean isMock = mockingDetails.isMock();
    assertThat(isMock).isTrue();
    final boolean isSpy = mockingDetails.isSpy();
    assertThat(isSpy).isFalse();

    // 2.
    final MockName mockName = mockingDetails.getMockCreationSettings().getMockName();
    assertThat(mockName.toString()).isEqualTo("mockedProduct");
    final Class<?> typeToMock = mockingDetails.getMockCreationSettings().getTypeToMock();
    assertThat(typeToMock).isSameAs(Product.class);

    // 3.
    mockedProduct.setPrice(10000);
    final Method setPrice = typeToMock.getMethod("setPrice", int.class);
    mockingDetails.getInvocations().forEach(
            invocation -> assertThat(invocation.getMethod()).isEqualTo(setPrice)
    );

    // 4.
    when(mockedProduct.getName()).thenReturn("Dexter");
    final Method getName = typeToMock.getMethod("getName");
    mockingDetails.getStubbings().forEach(stubbing -> {
                final Method method = stubbing.getInvocation().getMethod();
                assertThat(method).isEqualTo(getName);
            }
    );
}
```

1.  해당 객체가 mock or spy인지 확인할 수 있습니다.
2.  해당 객체가 mock일 경우 MockName, Type 등의 정보를 확인할 수 있습니다.
3.  해당 객체의 invocation 정보를 가져올 수 있습니다.
4.  해당 객체의 stubbing 정보를 가져올 수 있습니다.

### 17\. ArgumentMatchers

-   아규먼트를 검증할 수 있는 Matchers를 통해 verify에서 유용하게 사용할 수 있습니다.

```java
@Test
void matcherSupport() throws Exception {
    final List<String> mockedList = mock(List.class);
    mockedList.add("Hello");
    mockedList.add("Dexter");
    mockedList.add("James");

    verify(mockedList, times(3))
            .add(
                    argThat(string -> (string.length() < 8) && (string.length() > 3))
            );
}
```

-   verify에서 argThat() 메서드의 아규먼트로 람다식을 이용해 ArgumentMatcher를 생성하여 검증을 진행할 수 있습니다.
-   작성한 ArgumentMatcher는 add 된 String 전체가 길이가 3 초과 8 미만이라는 것을 검증합니다.

### 18\. Custom Verify Fail Message

-   verify 실패 시 원하는 메시지를 커스텀할 수 있습니다.

```java
@Test
void custom_verify_failure_message() throws Exception{
    final List<String> mockedList = mock(List.class);

    verify(mockedList, times(2).description("해당 메서드는 두번 호출되어야 한다.")).get(0);
}
```

-   description 메서드로 간단하게 실패 메시지를 설정할 수 있습니다.
