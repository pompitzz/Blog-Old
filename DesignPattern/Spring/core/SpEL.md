# SpLE(Spring Expression Language)
- 스프링 3.0부터 지원
- 객체 그래프를 조회하고 조작하는 기능을 제공한다.
- Unified EL과 비슷하지만, 추가적으로 메서드 호출, 문자열 템플릿 기능을 제공한다.
- SpLE은 모든 스프링 프로젝트 전반에 걸쳐 사용할 EL로 만들었다.


### 예제

[##_ImageGrid|kage@lKlHX/btqAwk4XDzb/zq0DDucsCBQwskxhw9NyZ1/img.png,kage@c0x1Wp/btqAv0MqFqo/aZHpH0AaHmbKJOTgwrTBr1/img.png|data-origin-width="0" data-origin-height="0" style="width: 49.0237%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 49.8135%;"|_##]
- application.properties나 빈으로 등록된 클래스의 값들을 SpLE를 통해 받아올 수 있다.

<br>

[##_ImageGrid|kage@doWcRE/btqAyG6PF1U/xVK1l5uIjRKlZhLNifDk21/img.png,kage@c96cUe/btqAyHdAN36/wSWkl13WpYfld9Uw3lbGKk/img.png|data-origin-width="0" data-origin-height="0" style="width: 46.0962%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 52.741%;"|_##]
- 값을 사용할 수 있는 다양한 방법 들이 존재한다.
- 빨간 박스처럼 properties값들을 받아올 수 있다.
- 그리고 파란 박스에서는 빈으로 등록된 데이터를 받아온 것을 알 수 있다.
- #{ }으로 **표현식** 을 사용할 수 있고, ${ }으로 **property** 를 참고 할 수 있다.


#### 참고
- #{${ } }이런식으로 표현식 안에는 property값을 사용할 수 있으나 반대로는 불가능하다.

> SpEL은 스프링 시큐리티, 스프링 데이터, Thymeleaf 등**스프링 전반에 걸쳐 사용**된다.

[레퍼런스](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions-language-ref)
- 레퍼런스에서 다양한 예시들과 정보를 얻을 수 있다.

---

[참고 자료](https://www.inflearn.com/course/spring-framework_core)
