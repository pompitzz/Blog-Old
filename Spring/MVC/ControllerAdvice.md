# @(Rest)ControllerAdvice
> 예외 처리, 바인딩 설정, 모델 객체를 **모든 컨트롤러 전반에 걸쳐 적용하고 싶은 경우에** 사용한다.  

- 예외를 처리하는 @ExceptionHandler와 함께 사용하면 전반에 걸친 예외처리가 가능하다.
- 바인딩 또는 검증을 설정할 수 있는 @InitBinder와 함께 사용하면 전반에 걸친 바인딩 설정이 가능하다.
- 모델 정보를 초기에 초기화할 수 있는 @ModelAttribute와 함께 사용하면 전반에 걸친 모델 정보 설정이 가능하다.

### 예제

[##_Image|kage@qxYCx/btqAqgAdhjh/qZrrj2Vo1lnQKC56QEZ5tK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 이전 게시글들에서 작성한 [@InitBinder](https://sun-22.tistory.com/9?category=363027), [@ModelAttribute](https://sun-22.tistory.com/8?category=363027), [@ExceptionHandler](https://sun-22.tistory.com/12?category=363027)를 @ControllerAdivice로 정의한 BaseController에 작성하였다.
- 빨간 줄처럼 @ControllerAdvice에 적용하고 싶은 클래스들을 지정할 수 있다. 즉 EventConroller, EventApi에 이 컨트롤러가 적용되는 것이다.

### 적용할 범위 지정하는 방법들
첫 번째 방법
- 위에서 사용한 방법으로 특정 클래스에 지정하는 방법이다.

두 번째 방법

[##_Image|kage@1QOua/btqAo7cXfQ4/CmDnJF85Xb84Of7rtKGpWK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]
- 특정 Annotation에 적용하는 방법이다.  @Controller에만 적용을 한다고 설정하였다.


세 번째 방법
[##_Image|kage@bxTO3a/btqAnxDrIYu/8OoSapk6a0KSxnUT67P4Pk/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]

- 특정 패키지 이하에 있는 컨트롤러에 적용하는 방법이다.

### @ControllerAdvice,  @RestControllerAdvice

- 이 둘의 차이는 @Controller와 @RestController의 차이와 같다.
- 즉 RestControllerAdvice를 사용한다면 핸들러들에게 자동으로 @ResponseBody가 적용되어 있는 것이다.

---

[참고 자료](https://www.inflearn.com/course/%EC%9B%B9-mvc)
