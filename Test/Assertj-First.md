```gradle
testImplementation("org.assertj:assertj-core:3.11.1")
```

### 테스트 실패 메시지
- assertj에서는 테스트 실패시 나타낼 메시지를 as로 표현할 수 있습니다.
- as는 검증문 앞에 작성해야하며 뒤에 작성 시 호출되지 않습니다.

```java
@Test
void test() throws Exception {
    String str = "name";
    assertThat(str).as("값을 확인해주세요. 현재 값: %s", str)
            .isEqualTo("name2");
}
```

---

### 테스트 시 필터링 기능 지원
- assertj를 사용하면 간단하게 테스트할 데이터들을 필터링할 수 있습니다.
- 간단한 예를 통해 알아보겠습니다.

```java
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Member {
    private String name;
    private int age;
    private MemberType type;
}

enum MemberType{
    ADMIN, USER
}
```
- 우선 테스트할 대상인 Member를 정의하였습니다.
- Member는 이름, 나이, 타입을 가지고 있습니다.
- 아래에서 해당 데이터로 테스트를 진행해 보겠습니다.

<br>


```java
import org.junit.jupiter.api.Test;

import java.util.List;

import static assertj.MemberType.ADMIN;
import static assertj.MemberType.USER;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.Lists.newArrayList;

class MemberTest {
    private Member dexter = new Member("dexter", 12, ADMIN);
    private Member james = new Member("james", 30, ADMIN);
    private Member park = new Member("park", 23, USER);
    private Member lee = new Member("lee", 33, USER);

    private List<Member> members = newArrayList(dexter, james, park, lee);

    @Test
    void sample() throws Exception {
        // 1
        assertThat(members)
                .filteredOn("type", ADMIN)
                .containsOnly(dexter, james);

        // 2
        assertThat(members)
                .filteredOn(m -> m.getType() == USER)
                .containsOnly(park, lee);

        // 3
        assertThat(members).
                filteredOn("type", in(ADMIN, USER))
                .containsOnly(dexter, james, park, lee);

        // 4
        assertThat(members)
                .filteredOn("type", not(ADMIN))
                .containsOnly(park, lee);

        // 5
        assertThat(members)
                .filteredOn("type", ADMIN)
                .filteredOn(m -> m.getAge() > 20)
                .containsOnly(james);
    }

}
```
- ADMIN, USER Member를 각 두명씩 생성하였습니다. 해당 값들을 List에 넣어 테스트해보겠습니다.
- 해당 데이터는 아래의 테스트에서 계속 사용됩니다.
- filteredOn을 이용하여 간단하게 필터링할 수있습니다.
- 첫번째 아규먼트에는 해당 데이터의 필드 파라미터명을 입력하면 됩니다.
- 두번째 아규먼트에는 필터할 상태를 정의합니다.
- 1\. type 파라미터에서 ADMIN Member만 필터링합니다.
- 2\. 람다식을 활용하여 필터링이 가능합니다.
- 3, 4\. assertj에서 지원해주는 메서드를 통해 다양한 조건을 넣을 수 있습니다.
- 5\. 필터링은 연쇄적으로 진행할 수 있습니다.

---


### 데이터 추출
- 만약 Member List에서 해당 Member들의 이름을 테스트하려면 어떻게 해야할까요?

```java
@Test
void no_extracting() throws Exception{
    List<String> names = new ArrayList<>();
    for (Member member : members) {
        names.add(member.getName());
    }

    assertThat(names).containsOnly("dexter", "james", "park", "lee");
}
```
- 따로 Name List를 만들고 for each를 통해 테스트할 수 있습니다.
- 하지만 assertj에서는  extracting을 지원해주며 해당 메서드를 통해 간결하게 테스트할 수 있습니다.

<br>

```java
@Test
void extracting() throws Exception{
    assertThat(members)
            .extracting("name")
            .containsOnly("dexter", "james", "park", "lee");
}
```
- 위의 테스트보다 훨씬 간결해진것을 확인할 수 있습니다.
- 추가적으로 extracting에서 활용할 수 있는 기능 몇개에 대해 알아보겠습니다.

<br>

```java
@Test
void extracting_more() throws Exception {
    // 1
    assertThat(members)
            .extracting("name", String.class)
            .contains("dexter", "james", "park", "lee");

    // 2
    assertThat(members)
            .extracting("name", "age")
            .contains(
                    tuple("dexter", 12),
                    tuple("james", 30),
                    tuple("park", 23),
                    tuple("lee", 33)
            );
}
```
- 1\. 추출할 데이터가 하나라면 타입 지정이 가능합니다.
- 2\. 여러 데이터를 추출한 후 assertj에서 지원해주는 tuple로 테스트를 할 수 있습니다.

---

### Soft Assertions
- 보통 테스트를 진행할 때 앞의 assertThat이 실패하면 해당 테스트는 중지됩니다.
- Soft Assertions을 이용하면 우선 모든 assertThat을 실행하고 해당 실패내역을 확인할 수 있습니다.

```java
class MemberTest {
    private Member dexter = new Member("dexter", 12, ADMIN);
    private Member james = new Member("james", 30, ADMIN);
    private Member park = new Member("park", 23, USER);
    private Member lee = new Member("lee", 33, USER);

    private List<Member> members = newArrayList(dexter, james, park, lee);


    @Test
    void no_softAssertion() throws Exception{
        assertThat(dexter.getAge()).as("Dexter Age Test").isEqualTo(11);
        assertThat(james.getAge()).as("James Age Test").isEqualTo(31);
        assertThat(park.getAge()).as("Park Age Test").isEqualTo(24);
        assertThat(lee.getAge()).as("Lee Age Test").isEqualTo(32);
    }
}

// 출력
[Dexter Age Test]
Expecting:
 <12>
to be equal to:
 <11>
but was not.
```
- 해당 테스트는 첫번째 assertThat이 실패하므로 Dexter Age Test에서 테스트가 중지됩니다.
- soft assertThat을 이용하여 테스트를 작성해 보겠습니다.

<br>

```java
@Test
void softAssertion() throws Exception{
    SoftAssertions softAssertions = new SoftAssertions();
    softAssertions.assertThat(dexter.getAge()).as("Dexter Age Test").isEqualTo(11);
    softAssertions.assertThat(james.getAge()).as("James Age Test").isEqualTo(31);
    softAssertions.assertThat(park.getAge()).as("Park Age Test").isEqualTo(24);
    softAssertions.assertThat(lee.getAge()).as("Lee Age Test").isEqualTo(32);

    softAssertions.assertAll();
}

// 출력
Multiple Failures (4 failures)
	org.opentest4j.AssertionFailedError: [Dexter Age Test]
Expecting:
 <12>
to be equal to:
 <11>
but was not.
at MemberTest.softAssertion(MemberTest.java:35)
	org.opentest4j.AssertionFailedError: [James Age Test]
Expecting:
 <30>
to be equal to:
 <31>
but was not.
at MemberTest.softAssertion(MemberTest.java:36)
	org.opentest4j.AssertionFailedError: [Park Age Test]
Expecting:
 <23>
to be equal to:
 <24>
but was not.
at MemberTest.softAssertion(MemberTest.java:37)
	org.opentest4j.AssertionFailedError: [Lee Age Test]
Expecting:
 <33>
to be equal to:
 <32>
but was not.
```
- 사용법은 간단합니다. Assertj의 SoftAssertions 인스턴스를 만듭니다.
- assertThat앞에 해당 인스턴스를 추가합니다.
- 마지막에 softAssertions.assertAll()을 호출하면 테스트가 진행됩니다.
- 해당 테스트의 출력을 보시면 모든 assertThat이 진행된 것을 확인할 수 있습니다.

<br>

```java
@Test
void softAssertion_JUnitSoft() throws Exception{
    SoftAssertions.assertSoftly(softAssertions -> {
        softAssertions.assertThat(dexter.getAge()).as("Dexter Age Test").isEqualTo(11);
        softAssertions.assertThat(james.getAge()).as("James Age Test").isEqualTo(31);
        softAssertions.assertThat(park.getAge()).as("Park Age Test").isEqualTo(24);
        softAssertions.assertThat(lee.getAge()).as("Lee Age Test").isEqualTo(32);
    });
}
```
- assertSoftly를 사용하면 따로 assertAll을 호출할 필요 없이 사용할 수 있습니다.

---

### File Assertions
- AssertJ에서는 파일에 대한 테스트를 제공합니다.
- 해당 데이터가 파일인지, 존재하는 지등에 대한 테스트를 진행할 수 있고 해당 파일 내용을 읽어 테스트를 진행할 수 있습니다.

```java
@Test
void file() throws Exception{
    File file = writeFile("Temp", "You Know Nothing Jon Snow");

    // 1
    assertThat(file).exists().isFile().isRelative();

    // 2
    assertThat(contentOf(file))
            .startsWith("You")
            .contains("Know Nothing")
            .endsWith("Jon Snow");
}

private File writeFile(String fileName, String fileContent) throws Exception {
    File file = new File(fileName);
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.defaultCharset()));
    writer.write(fileContent);
    writer.close();
    return file;
}
```
- 1\. 해당 파일이 존재하는지, 파일이 맞는지, 상대경로인지 테스트합니다.
- 2\. 해당 파일의 내용을 contentOf로 가져와 테스트를 진행합니다.
- 해당 값의 시작, 끝 값과 포함하는 값등을 테스트할 수 있습니다.

---

### Exception Assertions
