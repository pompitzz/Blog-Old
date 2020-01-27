# Validation 추상화
> 애플리케이션에서 사용하는 객체 검증용 인터페이스이다.

### 특징
- 모든 계층(웹, 서비스, 데이터)에서 사용 가능하다.
- Data Binder에 들어가 바인딩 할 때같이 사용되기도 한다.

### Validator를 만들어서 사용하기

[##_Image|kage@qs0bj/btqAwlBRgDT/fMYzvwGHBLzM9iQZtIrK50/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@c4fFEr/btqAuMfTfXF/NZ3g0leL6pCsZN4mFSkfpk/img.png|widthContent|data-lazy-src="" data-width="693" data-height="185" data-origin-width="0" data-origin-height="0"|||_##]

- Event 클래스를 만들고 Getter, Setter를 붙여주었다.
- EventValidator를 만들어 Validator를 구현한다.
- supports는 어떤 타입의 객체를 검증할 때 사용할 것인지 결정하는 것이다.
- Event.class를 검증할 것이므로 Event.class와 파라미터를 비교해준다.
- validate는 실제 검증 로직을 구현하는 곳이다.
- ValidationUtils를 이용하여 title field가 비어있으면 에러를 확인하였다.

[##_Image|kage@nZIE1/btqAtbHKOGN/T3MbkbGvUkSNrPPT0WqKk0/img.png|widthContent|data-lazy-src="" data-width="693" data-height="251" data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@MG86r/btqAuMfTfWR/7FDy4kx2LGL2PI68wsw8Wk/img.png|widthContent|data-lazy-src="" data-width="693" data-height="161" data-origin-width="0" data-origin-height="0"|||_##]

- Application Runner에서 EventValidator에 넣을 Errors가 필요하므로 Errors를 만들어 줬다.
- 그리고 validate에 event와 만든 errors를 아규먼트로 넘겨준다.
- 그 후  errors를 출력해보면 에러가 검증된것을 확인할 수 있다.

### Validation Annotation 활용하기

[##_Image|kage@cIIsTM/btqAuya8rG6/1aK99cTt8H3JBJpCV7AzWk/img.png|widthContent|data-lazy-src="" data-width="693" data-height="358" data-origin-width="0" data-origin-height="0"|||_##][##_ImageGrid|kage@bleZGL/btqAxhMxEc1/6UrVHtzb1nbVfdnyJcXsBK/img.png,kage@bhZbWw/btqAv0rdaWR/G4y6KfaLPXSSK3PZ8kWHB1/img.png|data-origin-width="0" data-origin-height="0" style="width: 42.9229%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 55.9143%;"|_##]

- **스프링 부트를 사용할 경우** 복잡한 에러 검증로직을 직접 만들 필요가 없고 스프링이 지원해주는 Validator를 사용할 수 있으며Annotation으로 검증이 가능하다.
- 위와 같이 Event 클래스에 title, limit, email에 필요한 검증을 붙여준다.
- 그리고 AppRunner에서 event에 검증에 걸릴만한 값들을 넣고 스프링이 제공해주는 Validator를 통해 검증하면 이렇게 에러가 검증되고 출력되는 것을 확인할 수 있다.

---

[참고 자료](https://www.inflearn.com/course/spring-framework_core)
