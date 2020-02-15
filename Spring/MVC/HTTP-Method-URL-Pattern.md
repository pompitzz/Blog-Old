# HTTP Method와 URL 패턴
> Spring Web MVC에서 사용하는 HTTP Method 매핑 방법과 URL 패턴에 대해 알아본다.

- 먼저 자주 사용하는 HTTP Method에 대해서 알아보자.

### HTTP Method
#### GET
- 클라이언트가 서버의 리소스를 요청할 떄 주로 사용하는 메서드이다.
- 브라우저에 기록이 남으며 URL에 전송하는 데이터가 표시되므로 민감한 데이터를 보내면 안된다.
- 멱등성 O, 캐시 가능

#### HEAD
- GET요청과 같지만 응답에 본문이 없고 헤더만 존재한다.
- 헤더를 통해 본문 정보를 유추할 때나 응답 상태 코드를 확인만 필요할 떄도 유용하다.
- 멱등성 O, 캐시 가능

#### POST
- 클라이언트가 서버의 리소스를 수정하거나 새로 만들 때 사용한다.
- 서버에 리소스를 수정하거나 새로 만들 때 보내는 데이터를 POST 요청 본문에 담는다.
- 브라우저에 기록이 남지 않는다.
- 프락시는 POST 요청을 바로 서버로 통과시키는 성질이 있다.
- 멱등성 X, 캐시 가능

#### PUT
- URL에 해당하는 리소스를 새로 만들거나 수정할 때 사용한다.
- 특정 URL에 해당하는 데이터를 다루는 것이므로 POST와는 조금 다르다.
- 멱등성 O, 캐시 불가능

#### DELETE
- 요청 URL로 지정한 리소스를 삭제할 떄 사용한다.
- HTTP 명세는 서버가 클라이언트에게 알리지 않고 요청을 무시하는 것을 허용하므로 삭제를 보장하지 못한다.
- 그러므로 응답을 통해 삭제에 대한 확인을 받는게 좋다.
- 멱등성 O, 캐시 불가능

#### PATCH
- PUT은 전체 리소스를 수정할 때 주로 사용되며, PATCH는 해당 자원의 일부를 수정하는 의미로 사용된다.
- 멱등성 X, 캐시 가능

#### OPTIONS
- 웹 서버에게 여러 가지 종류의 지원 범위에 대해 물어보는 메서드
- 허용하는 HTTP Method, 요청 등을 응답으로 보내준다.
- CORS요청 시 Preflight Request에서 사용된다.

### 멱등성(Idempotent)
- 특정 메서드를 여러 번 요청해도 한 번 요청할 때와 결과가 같다는 뜻이다.
- GET, PUT, DELETE 같은 경우 해당 URL에 똑같은 요청을 보낼 경우 결과는 같다.
- POST는 리소스에 따라 결과가 달라지므로 멱등성을 지키지 못한다.

---

### HTTP method 매핑하기

#### 테스트 코드
[##_Image|kage@lcHp0/btqABRCg6Le/hoFq7NporJJCK4BVySXNkK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="403" data-origin-width="0" data-origin-height="0"|||_##]
- 해당 테스크 코드를 실행시켜가며 HTTP Method 매핑에 대해 알아 볼 것이다.

#### @RequestMapping
- @RequestMapping시 따로 HTTP Method를 설정하지 않으면 모든 Method를 허용하게 된다.

[##_ImageGrid|kage@ANHfd/btqAB2wPsMj/h4nhih49qIEKiaXVue61Z1/img.png,kage@DknJb/btqACF2seSJ/xx8avNxfLoVJuSSfnXlnik/img.png|data-lazy-src="" data-width="525" data-height="205" data-origin-width="0" data-origin-height="0" style="width: 58.4228%; margin-right: 10px;",data-lazy-src="" data-width="349" data-height="197" data-origin-width="0" data-origin-height="0" style="width: 40.4144%;"|_##]
- GET, POST 요청 모두 테스트가 성공하는 것을 알 수 있다.

#### @GetMapping
[##_ImageGrid|kage@GCYkX/btqAE6D5L5r/51dDAMrgyfMxB2TUyXgWr0/img.png,kage@B73gG/btqABRCg6Kn/flD4cgGDdCSksVkd9rbj4k/img.png|data-lazy-src="" data-width="498" data-height="204" data-origin-width="0" data-origin-height="0" style="width: 55.4447%; margin-right: 10px;",data-lazy-src="" data-width="363" data-height="190" data-origin-width="0" data-origin-height="0" style="width: 43.3925%;"|_##]
- @GetMapping을 사용할 경우 GET METHOD만 허용한다.
- 테스트 중 POST 요청은 실패한 것을 알 수 있다.

#### @RequestMapping에서 메서드 설정
[##_Image|kage@t8WvZ/btqABS85n8P/F2SpNdqTxJfr2k1PqsdWhK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="143" data-origin-width="0" data-origin-height="0"|||_##]
- @RequestMapping으로 특정 메서드만 설정할 수도 있다.

#### Controller에 Http Method 설정하기
[##_Image|kage@baeNA4/btqADZZJtaq/WNWPxtl1jaeSTZ3kB3sFTK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="193" data-origin-width="0" data-origin-height="0"|||_##]
- Class단에도 @RequestMapping을 붙여 메서드를 설정할 수 있다.
- SampleController는 오직 GET Method만 허용할 것이다.

---

### URL 패턴들

#### 여러 가지의 URL 설정

[##_Image|kage@dMj4Ga/btqADG7bFwG/N6Y5rNkNEOw34CKXSnomkK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="484" data-origin-width="0" data-origin-height="0"|||_##]
[##_ImageGrid|kage@6MZsU/btqACOx4we8/c1SJCKOBXIgI3lKWgH1dK0/img.png,kage@bdMl0z/btqAB2jitnX/b75sGiQ3CsFNiEnFiEwdDK/img.png|data-lazy-src="" data-width="553" data-height="206" data-origin-width="0" data-origin-height="0" style="width: 58.1921%; margin-right: 10px;",data-lazy-src="" data-width="360" data-height="192" data-origin-width="0" data-origin-height="0" style="width: 40.6451%;"|_##]
- @RequestMapping에 여러 가지 URL을 설정할 수 있다.

<br>

#### 요청을 식별자로 매핑하기

[##_Image|kage@b8wyCF/btqACF9cOaR/KK0SDa75KMufBep897IUC1/img.png|widthContent|data-lazy-src="" data-width="693" data-height="328" data-origin-width="0" data-origin-height="0"|||_##]
[##_ImageGrid|kage@6wR1o/btqAE6D5ZLM/3UxKKBcHxswif0VrBXcw7K/img.png,kage@dJLNSR/btqACQbCYet/tYKt36GILl3FsuVKvgkMuK/img.png|data-lazy-src="" data-width="529" data-height="206" data-origin-width="0" data-origin-height="0" style="width: 52.1187%; margin-right: 10px;",data-lazy-src="" data-width="366" data-height="159" data-origin-width="0" data-origin-height="0" style="width: 46.7185%;"|_##]
- ?를 사용하면 한 글자만 매핑해준다.
- /hello/3으로 요청하였을 때 테스트가 성공한 것을 알 수 있다.
- \*, \*\* 등도 존재한다.
- (/hello/\*) 이렇게 설정하면 /hello/123123 이렇게 여러 글자까지 매핑해준다.
- (/hello/\*\*) 이렇게 설정하였다면 /hello/123/321/12 이렇게 여러 패스까지 매핑해준다.

<br>

#### Class @RequestMapping

[##_Image|kage@c0I9PC/btqAC1jEIyY/AtW3K84aSAMmgF3VjH4u9k/img.png|widthContent|data-lazy-src="" data-width="693" data-height="319" data-origin-width="0" data-origin-height="0"|||_##]
[##_ImageGrid|kage@qY4Tv/btqADIYdKFP/xqZKjsQy9hkofwCqT4i9JK/img.png,kage@T71Xx/btqADZrUoEZ/zWgjMIk3HHg3QOeacoiE00/img.png|data-lazy-src="" data-width="458" data-height="220" data-origin-width="0" data-origin-height="0" style="width: 46.0736%; margin-right: 10px;",data-lazy-src="" data-width="360" data-height="151" data-origin-width="0" data-origin-height="0" style="width: 52.7636%;"|_##]
- 클래스의 RequestMapping에 URI를 설정하면 해당 컨트롤러 내의 URL의 기본 패스가 추가된다.
- Controller에 /hello로 설정하였으므로 /hello/\* 로 요청을 하여야 한다.


<br>

#### 정규 표현식 활용

[##_ImageGrid|kage@bJubIe/btqADjK8ROz/klHXACAEXq4cwcvCVRm690/img.png,kage@x74vv/btqACQo8EPe/3KCfMh2NzgyTKnDzPlu3Mk/img.png|data-lazy-src="" data-width="585" data-height="222" data-origin-width="0" data-origin-height="0" style="width: 57.6377%; margin-right: 10px;",data-lazy-src="" data-width="356" data-height="189" data-origin-width="0" data-origin-height="0" style="width: 41.1995%;"|_##][##_Image|kage@ntgk3/btqAB2Q6kS1/AfZog79ytFkbCrg1GxZ8wK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="445" data-origin-width="0" data-origin-height="0"|||_##]

- 정규 표현식을 활용할 수도 있다.
- name이 a ~ z 문자여야하고 + 는 한번이상 존재해야 한다는 뜻이다.
- 즉 name이 문자이면서 한글자이상 존재하는 경우에만 매핑이 된다.
- 테스트를 보면 master는 통과하지만 @@는 통과되지 않는것을 알 수 있다.


#### URI 패턴이 중복될 때

[##_Image|kage@nVGxI/btqAE6RDfSW/UlYOq5lj6uKdZJUOMIhwP1/img.png|widthContent|data-lazy-src="" data-width="693" data-height="283" data-origin-width="0" data-origin-height="0"|||_##]
- 가장 구체적으로 매핑되는 핸들러를 선택한다.
- 그러므로 "hello test"를 리턴하는 메서드에 매핑될 것이다.

---

[참고 자료](https://www.inflearn.com/course/%EC%9B%B9-mvc#)
