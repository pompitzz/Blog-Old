> 핸들러 메서드인 @ModelAttribute, @Valid, @Validated에 대해 알아본다.

### @ModelAttribute
- URI 패스, 요청 , 세션 등 에 있는 **단순 타입 데이터를 복합 타입 객체로** 받아오거나 **해당 객체를 새로 만들 때** 사용할 수 있다.

[##_ImageGrid|kage@lo6DC/btqAGcL0BRb/LtyxKdJRXuC9OP4EoSItB1/img.png,kage@csYwsS/btqAGdYsGvA/xVWlChNT4HzKuUShVUTpfK/img.png|data-lazy-src="" data-width="323" data-height="209" data-origin-width="0" data-origin-height="0" style="width: 32.9363%; margin-right: 10px;",data-lazy-src="" data-width="637" data-height="206" data-origin-width="0" data-origin-height="0" style="width: 65.9009%;"|_##]
- @ModelAttribute를 이용하면 객체로 데이터를 받아올 수 있다.

<br>

[##_Image|kage@vGInF/btqAD0lwFo9/Ay3GMD6lUB1lAfyjShr170/img.png|widthContent|data-lazy-src="" data-width="693" data-height="300" data-origin-width="0" data-origin-height="0"|||_##]

- param에 name, limit을 담아 전송한다.
- @ModelAttribute는 그 값들을 Event 객체에 매핑해준다.

<br>

[##_Image|kage@zpXNo/btqAGC4MgGJ/39V0GjA8LiFfxy06qi777k/img.png|widthContent|data-lazy-src="" data-width="693" data-height="160" data-origin-width="0" data-origin-height="0"|||_##]

- name과 limit이 정상적으로 전송되어 Event 객체로 변환된 것을 알 수 있다.
- param으로만 데이터를 전송했지만 **URI 패스, 세션 등에도 똑같이** 적용 된다.

#### @ModelAttribute 생략

[##_ImageGrid|kage@23gYO/btqAGZyEffY/FUFqzpsU1n9uc8qhOTG5D1/img.png,kage@b85ujd/btqAGDo5YqL/JCgNRM2oX0zdSC2FNKhKS0/img.png|data-lazy-src="" data-width="344" data-height="200" data-origin-width="0" data-origin-height="0" style="width: 42.7145%; margin-right: 10px;",data-lazy-src="" data-width="513" data-height="227" data-origin-width="0" data-origin-height="0" style="width: 56.1227%;"|_##]
- @ModelAttribute는 생략하여도 알아서 변환해준다. 하지만 명시적으로 붙여주는 것이 좋다.

[##_ImageGrid|kage@YqcnR/btqAHpRrr5F/TkWCiM9RJLUHtY1VFBhGo1/img.png,kage@do94LP/btqAGX1X0QR/opneNBEJOiYwfTWjSQ3GvK/img.png|data-lazy-src="" data-width="609" data-height="229" data-origin-width="0" data-origin-height="0" style="width: 61.928%; margin-right: 10px;",data-lazy-src="" data-width="317" data-height="200" data-origin-width="0" data-origin-height="0" style="width: 36.9092%;"|_##]
- @ModelAttribute를 생략하여도 정상적으로 테스트가 수행하는것을 알 수 있다.

#### 바인딩 에러

[##_Image|kage@nTHes/btqAGDQbeMQ/1FAkq0VeUv3nRnN5x4YY41/img.png|widthContent|data-lazy-src="" data-width="693" data-height="188" data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@qzmX4/btqAHIJ0n8m/kn4uWquni4vNUYLk6SeuNK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="123" data-origin-width="0" data-origin-height="0"|||_##]
- Integer 타입인 limit에 String을 전송하면 바인딩 에러가 발생한다.

<br>

[##_Image|kage@cNMYMj/btqAE7xHeTB/myeuHoIiYFdUsf7jlBtkkK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="196" data-origin-width="0" data-origin-height="0"|||_##]
- BindingResult나 Errors를 이용하여 에러 발생 시 필요한 작업을 수행할 수도 있다.

<br>

[##_Image|kage@lDfWX/btqAFpx5gnN/D4vU2YMDRfpTp1RRSNcieK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="73" data-origin-width="0" data-origin-height="0"|||_##]
- 위와 똑같은 테스트를 실행한다.
- Integer타입에 String을 넣었으므로 에러가 발생한다.
- 그 에러는 BindingResult에서 가지고 있다
- 그러므로 위와 같이 출력이 나타나는 것을 알 수 있다.

---

### @Valid
- @Valid를 통해 검증작업을 할 수 있다.

[##_ImageGrid|kage@bDmuuG/btqAGZrULtp/2nYZXEn42q3X4JAU8KPEX1/img.png,kage@d13QWb/btqAHJIT9vB/YaVc1O3h0JEyvhRGr9SVh1/img.png|data-lazy-src="" data-width="313" data-height="226" data-origin-width="0" data-origin-height="0" style="width: 32.207%; margin-right: 10px;",data-lazy-src="" data-width="659" data-height="230" data-origin-width="0" data-origin-height="0" style="width: 66.6302%;"|_##]

- Event의 인스턴스 변수에 Vaild와 관련된 Annotation을 통해 검증작업을 수행할 수 있다.
- Integer에 @Min(0)을 붙여 0보다 크거나 같은 값만 허용하게 하였다.
- 검증을 위해선 Controller에서 값을 받는 파라미터에 @Valid를 붙여줘야 한다.  

<br>

[##_Image|kage@viaWX/btqAHHK5DtX/8e4EzHgkybxyTgwKcCk8kK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="183" data-origin-width="0" data-origin-height="0"|||_##][##_ImageGrid|kage@mmWwe/btqAHIDe4uB/FlyI1arYV1mXAco4GK3B51/img.png,kage@brO9NC/btqADYnGBq9/ubeV43in6lwCHzCbczrawK/img.png|data-lazy-src="" data-width="614" data-height="80" data-origin-width="0" data-origin-height="0" style="width: 49.0194%; margin-right: 10px;",data-lazy-src="" data-width="468" data-height="60" data-origin-width="0" data-origin-height="0" style="width: 49.8178%;"|_##]

- 테스트에서 limit에 -10을 담고 요청을 보낸다.
- @Valid를 통해 limit에 설정된 @Min(0)을 검증하여 에러를 발생시킨다.

### @Validated
- 스프링 프레임워크가 제공해주는 @Validated를 이용하면 **그룹화를 할 수 있다.**

[##_Image|kage@vJUMD/btqAFpERoWI/lO0u7mF5ErGQSer4hlPJH1/img.png|widthContent|data-lazy-src="" data-width="693" data-height="279" data-origin-width="0" data-origin-height="0"|||_##]
- name의 @NotBlank는 ValidatedName으로 그룹화 하였다.
- limit의 @Min은 ValidatedLimit으로 그룹화하였다.

<br>

[##_Image|kage@bO7BNw/btqAGZFsEkK/eK4mD3IErtzVP3kZYnou4k/img.png|widthContent|data-lazy-src="" data-width="693" data-height="153" data-origin-width="0" data-origin-height="0"|||_##]
- 해당 컨트롤러 메서드에서 검증을 원하는 파라미터에 @Validated를 붙이고 그룹화한 클래스를 넣어준다.
- ValidatedLimit을 넣었으므로 Min에 대한 검증만 이루어 지게 될 것이다.
- 그러므로 이전과 같은 테스트 실행시 에러가 바인딩 될 것이다.

<br>

[##_Image|kage@bfTvUa/btqAGZZKEwu/ElQYAVOCxeKgjxcdNxaG0k/img.png|widthContent|data-lazy-src="" data-width="693" data-height="154" data-origin-width="0" data-origin-height="0"|||_##]
- name을 검증하는 ValidateName을 붙여주면 에러가 바인딩 되지 않아 주황색 박스는 통과 될 것이다.

---

[참고 자료](https://www.inflearn.com/course/%EC%9B%B9-mvc)
