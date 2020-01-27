# ResourceLoader
> 리소스를 읽어오는 기능을 제공하는 인터페이스

- ApplicationContext는 ResourceLoader를 상속받기 때문에 사용 가능하다.

[##_Image|kage@reaCi/btqAtcs0GUi/AzJHiHZkJ4aE2XYtrXiOgK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@dyY2e8/btqAt6FVGeS/3OkImQKnHFCoOKLPKWDbOk/img.png|floatLeft|data-origin-width="222" data-origin-height="82"|||_##]

- ResourceLoader를 이용하여 Resource를 읽어 올 수 있다.
- 리소스 존재유무, 파일 이름등 다양한 기능이 존재한다.

[##_Image|kage@dspfaS/btqAwjKSJoq/ACagsKKy2UUWKXaCgqkqU1/img.png|alignCenter|data-origin-width="0" data-origin-height="0" width="227"|||_##]

---

# Resource 추상화
> java.net.URL을 추상화 한 것이며 **스프링 내부에서 많이 사용하는 인터페이스**

#### 추상화한 이유
- 클래스 패스 기준으로 리소스를 읽어올 수 없었다.
- ServletContext를 기준으로 상대 경로로 읽어오는 기능 부재
- 새로운 핸들러를 등록하여 특별한 URL 접미사를 만들어 사용할 수는 있지만 **구현이 복잡하며 편의성 메서드가 부족**

#### 주요 Method
- getInputStream()
- exitst()
- isOpen
- getDescription()

#### **구현체**
- UrlResource
- ClassPathResource
- FileSystemResource
- ServletContextResource

---

### 구현체 확인 해보기

#### ClassPathXml, FileSystemXmlApplicationContext

[##_Image|kage@bnHxGy/btqAtB0o2CO/ggMtV0tBGSzy2DnT0SwSAk/img.png|widthContent|data-lazy-src="" data-width="693" data-height="171" data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@8FMen/btqAvZTmzWW/Wj6Nc9EBvo5k2Syj8MTQe1/img.png|widthContent|data-lazy-src="" data-width="693" data-height="45" data-origin-width="0" data-origin-height="0"|||_##]

- ClassPath나 FileSystemApplicationContext로 읽으면 앞에 **classpath: 같은 접두어를 붙일 필요가 없다.**

#### ApplicationContext 기본 리소스

[##_Image|kage@JihZF/btqAwIRaV7f/gmAnx9bdR46Oq9jtWZhnBK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="256" data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@zVSXh/btqAwkQuMqL/nkLaMl0yDROutnqU9cmnM0/img.png|widthContent|data-lazy-src="" data-width="693" data-height="43" data-origin-width="0" data-origin-height="0"|||_##]

- ApplicationContext로 resourceLoader를 생성하였을 때는 기본적으로 **SurvletContextResource로 리소스를 찾게 된다.**
- ServletContextResource이기 때문에 **웹 애플리케이션 루트에서 상대 경로로 리소스 찾는다.** 그러므로 resoueces/test.txt를 읽어오지 않게 된다.

####   classpath: 를 붙여 리소스 타입 변경하기
[##_Image|kage@dcQiB3/btqAvjYQYDY/6qVem8m8gyWG7muWKSHmm0/img.png|widthContent|data-lazy-src="" data-width="693" data-height="221" data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@dLdvwq/btqAuxDhh0K/8wj6reAcAS4Q9eerXdO9K1/img.png|widthContent|data-lazy-src="" data-width="693" data-height="57" data-origin-width="0" data-origin-height="0"|||_##]

- ServletResouce를 classpath로 읽어오고 싶을 경우 java.net.URL 접두어( + classpath:)를 앞에 붙이게 되면 **강제로 가능하게** 된다.
- 만약 file:를 붙인다면 FileSystemResource로 변경될 것이다.


---

[참고 자료](https://www.inflearn.com/course/spring-framework_core)
