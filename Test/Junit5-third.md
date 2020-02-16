# JUnit5 #3

> [Junit5 User Guide](https://junit.org/junit5/docs/current/user-guide/#overview)를 읽어보면서 Junit5를 공부하는 게시글 입니다.

> 이번 게시글에서는 JUnit5 User Guide의 Writing Tests중 나머지 부분들에 대해 알아보겠습니다.

### 1. Dependency Injection for Constructors and Methods
- JUnit5 이전까지는 테스트 생성자와 메서드에 매개변수를 가질 수 없었습니다.
- Junit5에서는 테스트 생성자와 메서드에 매개변수를 가질 수 있고 이는 Dependency Injection을 가능하게 합니다.
- ParameterResolver는 런타임 시 매개변수를 정의하는 특징이 있습니다.
- JUnit5 Jupiter에서는 등록된 ParameterResolver에 의해 매개변수를 사용할 수 있습니다.
- Jupiter에 등록된 ParameterResolver는 총 3가지로 TestInfo, RepetitionInfo, TestReporter ParameterResolver가 존재합니다.
- 해당 ParameterResolver에 의해 TestInfo, RepetitionInfo, TestReporter를 매개변수로 가질 수 있습니다.

#### 1) TestInfo ParameterResolver
- TestInfo에는 해당 테스트의 DisplayName, Tags, Class, Method에 대한 정보를 가지고 있습니다.

```Java
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("테스트 인포")
public class TestInfoTest {
    TestInfoTest(TestInfo testInfo){
        assertEquals("테스트 인포", testInfo.getDisplayName());
    }

    @BeforeEach
    void beforeEach(TestInfo testInfo){
        String displayName = testInfo.getDisplayName();
        assertTrue(displayName.equals("First Test") || displayName.equals("secondTest()"));
    }

    @Test
    @DisplayName("First Test")
    @Tag("tag-Info")
    void firstTest(TestInfo testInfo){
        String displayName = testInfo.getDisplayName();
        Set<String> tags = testInfo.getTags();
        assertTrue(displayName.equals("First Test") && tags.contains("tag-Info"));
    }

    @Test
    void secondTest(TestInfo testInfo){
    }

}
```
- 생성자에 TestInfo를 통해 클래스의 DisplayName 혹은 Tag들을 테스트할 수 있습니다.
- 각 테스트에서 혹은 LifeCycle메서드에서 TestInfo를 사용하여 각 테스드들의 Tag와 DisplayName을 테스트할 수 있습니다.
- 이외에도 TestInfo에는 해당 테스트의 Class, Method들의 정보를 가져올 수 있습니다.

#### 2. TestReporter ParameterResolver
- 테스트시 추가적인 데이터들을 entry에 추가할 수 있는 기능같아보입니다.
- 보고서 같은곳에 추가할데이터를 추가시킬 수 있는거 같습니다.

#### 3. RepetitionInfo ParameterResolver
- RepetitionInfo는 Repetition테스트에서 사용할 수 있는 매개변수입니다.
- 자세한 내용은 Repetition테스트 기능을 살펴볼 때 알아보도록 하겠습니다.

---

### 2. Test Interfaces and Default Methods
- Interface의 Default Method를 이용하여 Test Lifecycle Method들을 정의할 수 있습니다.

```Java
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
interface InterfaceTest {
    @BeforeAll
    default void beforeAll(){
        System.out.println("============== InterfaceTest Before All =============== ");
    }

    @BeforeEach
    default void beforeEach(){
        System.out.println("============== InterfaceTest Before Each =============== ");
    }

    @AfterEach
    default void afterEach(){
        System.out.println("============== InterfaceTest After Each =============== ");
    }

    @AfterAll
    default void afterAll(){
        System.out.println("============== InterfaceTest After All =============== ");
    }
}
```
- Interface로 정의된 테스트는 @Test LifeCycle을 Per_Class로 설정해주어야 합니다.
- 이제 해당 테스트를 구현하는 다른 테스트는 해당 인터페이스 테스트의 라이프사이클을 물려받습니다.

<br>

```Java
import org.junit.jupiter.api.*;

class SampleTest implements InterfaceTest {
    @BeforeAll
    void BeforeAll() {
        System.out.println("------------- SampleTest Before All -------------");
    }

    @BeforeEach
    void sample_BeforeEach() {
        System.out.println("------------- SampleTest Before Each -------------");
    }

    @Test
    void sampleTest(){
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>> Sample Test <<<<<<<<<<<<<<<<<<<<<<");
    }

    @AfterEach
    void sample_AfterEach() {
        System.out.println("------------- SampleTest After Each -------------");
    }

    @AfterAll
    void sample_AfterAll() {
        System.out.println("------------- SampleTest After All -------------");
    }
}

// ========== 출력 ==========
============== InterfaceTest Before All ===============
------------- SampleTest Before All -------------
============== InterfaceTest Before Each ===============
------------- SampleTest Before Each -------------
>>>>>>>>>>>>>>>>>>>>>>> Sample Test <<<<<<<<<<<<<<<<<<<<<<
------------- SampleTest After Each -------------
============== InterfaceTest After Each ===============
------------- SampleTest After All -------------
============== InterfaceTest After All ===============
```
- 인터페이스 테스트를 구현하고 해당 테스트들을 정의한 후 실행해보면 위와같이 출력이 나타나는것을 확인할 수 있습니다.

---

### 3. Repeated Tests
- JUnut5 Jupiter는 RepeatedTest Annotation을 통해 해당 테스트의 반복횟수를 지정하여 반복되는 테스트를 정의할 수 있습니다.
- 해당 테스트들은 일반 Test Annotation과 생명주기가 동일합니다.

```Java
package repe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

class MyRepeatedTest {
    @BeforeEach
    void beforeEach(){
        System.out.println("====== BeforeEach ======");
    }

    @RepeatedTest(3)
    void sampleTest(){
        System.out.println("------ SampleTest ------");
    }
}

// ============ 출력 ============
====== BeforeEach ======
------ SampleTest ------
====== BeforeEach ======
------ SampleTest ------
====== BeforeEach ======
------ SampleTest ------
```
- RepeatedTest Annotation으로 테스트를 반복 한 후 출력을 확인해보면 각 반복되는 테스트마닥 Before Each가 적용되는 것을 통해 생명주기가 같다는 것을 확인할 수 있습니다.

#### RepetitionInfo 사용하기
- RepetitionInfo를 통해 RepeatedTest의 현재, 총 테스트의 수를 가져올 수 있습니다.

```Java
package repe;

import org.junit.jupiter.api.*;

class MyRepeatedTest {
    @BeforeEach
    void beforeEach(RepetitionInfo repetitionInfo, TestInfo testInfo){
        int currentRepetition = repetitionInfo.getCurrentRepetition();
        int totalRepetitions = repetitionInfo.getTotalRepetitions();
        String methodName = testInfo.getTestMethod().get().getName();

        System.out.printf("%s 테스트 실행 %d 번째 중 %d 번째 \n", methodName, totalRepetitions, currentRepetition);

    }

    @RepeatedTest(3)
    void sampleTest(){
        System.out.println("------- 테스트 수행 중 -------\n");
    }

}

// ================ 출력 =================
sampleTest 테스트 실행 3 번째 중 1 번째
------- 테스트 수행 중 -------

sampleTest 테스트 실행 3 번째 중 2 번째
------- 테스트 수행 중 -------

sampleTest 테스트 실행 3 번째 중 3 번째
------- 테스트 수행 중 -------
```
- BoforeEach에서 RepetitionInfo와 TestInfo를 통해 각 반복테스트의 현재 테스트 수, 총 테스트 수, 테스트 메서드의 이름을 가져옵니다.
- 그리고 해당 테스트에 맞게 출력을 표현해주면 위와같이 출력이 나타나는 것을 알 수 있습니다.

---

### 4. Parameterized Tests
- ParameterizedTest Annotation을 통해 파라미터가 있는 테스트를 진행할 수 있습니다.
- ValueSource를 통해 해당 파라미터의 값을 바인딩시킬 수 있습니다.

```Java
package repe;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ParamTest {

    @ParameterizedTest
    @ValueSource(strings = {"A", "B", "C"})
    void stringTest(String str){
        System.out.print(str + " "); // A B C
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void intsTest(int i){
        System.out.print(i + " "); // 1 2 3
    }
}
```
- 위와같이 테스트의 파라미터에 값을 바인딩 시킬 수 있습니다.
- 모든 기본타입 뿐만아니라 String, Class계열의 데이터도 가능합니다.

---

### 5. Timeouts
- 테스트의 제한시간을 설정할 수 있습니다.
- 테스트시간은 초 뿐만아니라 나노시간, 마이크로시간, 분, 시, 일 등 다양하게 설정이 가능합니다.

```Java
package repe;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

class TimeoutTest {

    @Test
    @Timeout(3) // 3초
    void timeoutTest() throws InterruptedException {
        Thread.sleep(2000);
    }

    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void sampleTest() throws InterruptedException {
        Thread.sleep(99);
    }

    @Test
    @Timeout(3) // 3초
    void fail_timeoutTest() throws InterruptedException {
        Thread.sleep(4000);
    }

    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void fail_sampleTest() throws InterruptedException {
        Thread.sleep(101);
    }
}
```
- 따로 unit을 설정하지 않으면 초로 계산됩니다.
- unit을 통해 다양한 시간 타입을 설정할 수 있습니다.
- 해당 테스트에서 위쪽에 있는 두개의 테스트는 시간내에 동작하므로 통과되지만 아랫쪽은 실패하게 됩니다.

---

### 6. Parallel Execution
- JUnit5 Jupiter는 기본적으로 테스트를 단일 스레드에서 순차적으로 실행합니다.
- 필요에 따라 병렬로 실행하도록 설정할 수 있습니다.
- 해당 기능을 사용할 일이 없을꺼 같아 따로 알아보지는 않고 아래와 같이 공유 데이터 사용시 Lock을 걸 수 있다는 정도만 이해하고 넘어가겠습니다.

```Java
@Test
@ResourceLock(value = "TEMP", mode = ResourceAccessMode.READ_WRITE)
void resourceLockTest(){

}
```

---
### 마무리

- JUnit5 User Guide의 Writing Tests Section에 대해 알아보았습니다.
- 뒷 부분의 내용들은 보통 테스트 작성 시 사용되지 않을거라 생각이되지만 언젠가 필요해질 때 유용하게 사용할 수 있을꺼 같습니다.
- 다음 게시글에서는 AssertJ 문서를 읽어보면서 기능들에 대해 알아보겠습니다.
