# 데이터 바인딩 추상화
> 기술적인 관점: 프로퍼티 값을 타깃 객체에 선정하는 기능

> 사용자 관점: 사용자가 입력한 값을 애플리케이션 도메인 모델에 **동적으로 변환해 넣어주는 기능.**

- 입력값은 대부분 **문자열** 이다.
- 그 값을 int, log, Boolean 등 심지어 Event, Book 같은 도메인 타입으로도 변환해서 넣어주는 기능이다.

### EventEditor
[##_ImageGrid|kage@bgJZ0q/btqAvkcnZpw/oLPohXjNkDo6OVC5tIh3p1/img.png,kage@IzcOm/btqAxgz684i/AgURLub45XKpOjqyU4jZkK/img.png|data-origin-width="0" data-origin-height="0" style="width: 32.3649%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 66.4723%;"|_##][##_Image|kage@c9CdsN/btqAxh6Sw2d/I4AHSKTjqELcyz4f5QghkK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]
- Event 도메인을 생성한다.
- EventController에서 @PathVariable로 Event를 파라미터로 받아온다.
[##_Image|kage@b08R7T/btqAwlBR0PL/XYCYmoVHOrmp6vNfWTlVwk/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]
- test에서 mock객체를 이용하여 url에 event/1로 요청을 보낸다.
- editor를 매칭 하지 못한다는 에러가 발생하는 것을 알 수 있다
- event/1에서 1을 event로 매핑해야 하지만 이 매핑은 Editor가 필요하다.

#### Editor

[##_Image|kage@duFeBw/btqAtcs14rI/NFSsTDNo9iyKYG1L5EXlyK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@bgyDNp/btqAwj49vBx/PUoRg6MUCaYvxuPVDCeVzK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]
- PropertyEditorSupport를 상속받아 Editor를 설정할 수 있다.
- get요청으로 날라온 1을 Event의 id로 변환해줘야 한다.
- setAsText에서 매개변수인 text가 get요청으로 날아오는 1이다.
- 이것을 Integer로 바꾸고 Event 생성자에 넣어주면 된다.

[##_Image|kage@bJCM4I/btqAt7rkL6x/Znw6WqvcIOXAfobDwSXTdK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]
- Editor를 Controller에서 @InitBinder를 통해 추가할 수 있다.
- 테스트를 돌려보면 통과한 것을 알 수 있다.
- 즉 1을 Editor에서 Integer로 변환하여 이벤트를 생성자에 넣고 setValue를 하였기 때문이다.

#### PropertyEditor의 한계
- 이 에디터의 get, set Value 한 Value 들은 PropertyEditor가 가지고 있는 값으로 다른 Thread에 공유된다.
- **Thread-safe 하지 않으므로** 혹여나 빈으로 등록할 때는 **Thread Scope 빈으로 만들어야 한다.**
- Object와 String 간의 변환만 할 수 있어, 사용 범위가 제한적이다.

> 이러한 Editor의 단점을 보완한 Converter와 Formatter가 있다.

---

### Converter
- S 타입을 T 타입으로 변환할 수 있는 매우 일반적인 변환기
- 상태 정보가 없어 **Stateless 하기 때문에 Thread-safe** 하다. (**빈으로 등록해도 된다)**
- ConverterRegistry에 등록하여 사용한다.

[##_Image|kage@bltw2N/btqAvZ0bfRV/JEVm9zXYHOl29wPOVpGmz1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]
- EventConverter를 만들고 그 안에 정적 클래스로 StringToEvent , EvnetToString을 Converter를 상속받아 만든다.
- 그 후 StringToEventConverter에서 Editor에서와 똑같이 Interger로 변환 후 Event를 생성하고 리턴한다.

<br>

[##_Image|kage@bVmvaJ/btqAwjD5lG4/eQ01IVCQ7Z0olSu6FmVNN1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- Converter는 WebMvcConfigurer를 통해 등록이 가능하다.
- 오바라이드된 메소드의 파라미터를 보면 FomatterRegistry이다.
- FomatterRegistry는 ConverterRegistry를 상속받아 만들어졌기 떄문에 addConverter를 할 수 있다.
- 그 후 위의 테스트를 진행시키면 아래와 같이 잘 통과되는 것을 알 수 있다.

[##_Image|kage@Gd2wC/btqAt7rk0x2/8RehGoFhj31FSP4uVvTCkk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

---

### Formatter
- Object와 String 간의 변환을 담당한다.
- **문자열을 Locale에 따라 다국화하는 기능도 제공한다**.(optical)
- FomatterRegistry에 등록하여 사용한다.
- Thread-Safe 하므로 빈으로 등록 가능

> 다국화 기능이 있으므로 웹에 더 특화되어 있다.

[##_Image|kage@zrtb0/btqAviyVvca/3xEFSEKUtD1cEjr7QozO3K/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]
- Converter는 Generics 타입 변환이므로 따로 구현했지만 Fomatter는 String-Object 변환이므로 하나만 구현하면 된다.
- 파라미터를 보면 Locale이 있어 다국화 기능을 사용할 수 있음을 알 수 있다.

<br>

[##_Image|kage@ccI10Q/btqAtc7HnpS/VukroUKwKo2hrxklLhAVK0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

이 또한 WebMvcConfigurer를 이용하여 추가해주어야 한다. 그러면 아래와 같이 테스트가 통과된다.

[##_Image|kage@QYycO/btqAvZToAMf/vmYpTEsjGHq2QV6Xm9Uq00/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

---

### PropertyEditor와 Converter, Formatter

- 데이터 바인딩 작업을 PropertyEditor는 **DataBinder가** 담당한다.
- Converter와 Formatter는 **ConversionService가** 실제로 변환을 가능하게 해준다..

### ConverterService
- 위의 예제와 같이 실제 변환 작업은 이 인터페이스를 통해서 Tread-Safe 하게 사용할 수 있다.
- 스프링 MVC, 빈 설정, SpEL에서 사용한다.


### 스프링 부트에서의 Converter, Fomatter
- Converter나 Formatter가 **Bean으로 등록되어 있다면** 자동으로 ConversionService에 등록해준다

<br>

[##_Image|kage@cAVO4D/btqAv1KshRh/63PIKK3yCBU8kV7vBvDqtK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@1YQ92/btqAtcs2Owy/066siLw7AraJ9cM1vyQS31/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]
- Converter에 @Component를 붙여 빈으로 등록한다.
- 그러면 따로 WebConfig에 추가할 필요가 없다.
- ~~Test코드에서 @WebMvcTest에 따로 명시해주어야 한다.~~
- ~~@WebMvcTest는 웹에 관한 빈들만 처리해주기 때문에 따로 추가해주지 않으면 에러가 나타난다.~~

> @WebMvcTest에서 컨버터는 따로 추가하지 않아도 빈으로 등록해주며 포매터는 해주지 않습니다.
그러므로 컨버터는 따로 추가할 필요가 없습니다.

<br>

[##_Image|kage@b7Q1ta/btqAxhZ6hUy/qBgoqGQYYdNYyfpy4jOHL0/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]

- 포매터도 마찬가지로 빈으로 등록하고 테스트에서 추가해주면 된다.

[##_Image|kage@di9wJM/btqAxhTlxrQ/nK977Ui6b3qDkymFVYLg00/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@wk25G/btqAuNeNYaM/XSqP2VVVAlgHzwZmS95fKk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 컨버터, 포매터 모두 스프링 부트에서 빈으로 등록하면 따로 추가할 필요 없이 적용되는 것을 알 수 있다.

---

[참고 자료](https://www.inflearn.com/course/spring-framework_core)
