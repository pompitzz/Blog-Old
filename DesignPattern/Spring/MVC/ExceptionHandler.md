# @ExceptionHandler
> 어떤 요청을 처리하다가 예외들이 발생했을 때 직접 정의한 핸들러를 통해 예외를 처리해서 응답할 수 있다.

### 예제

[##_Image|kage@bjAc0p/btqAovrvpDD/UUJL81GKtaGhCubdgodwS1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- RuntimException을 상속받은 EventException을 하나 만들었다.

[##_Image|kage@cUPW13/btqAnngJqyE/d3E92nDk1ZKJtPnl5WqTl1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 그리고 예외가 발생했을때 보여줄 페이지를 만든다.
- 빨간 박스의 message에 예외 메시지를 담아 보여줄 것이다.

[##_Image|kage@AtdkB/btqAnomoW6F/KrryJPpEfEflgEnMUd2ob0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@stjkH/btqAo7KIHNl/nHAylvdheEyi4KBARhHxD0/img.png|floatLeft|data-origin-width="0" data-origin-height="0" width="300" height="124"|||_##]

- 컨트롤러에서 @ExceptionHandler를 사용한 메서드를 하나 작성한다.
- 메소드 파라미터에 원하는 Exception을 넣으면 예외 처리가 가능하다.
- EventException을 파라미터로 넣었으므로 EventException 발생 시 eventErrorHandler가 동작하여 message를 담아 줄 것이다.
- 그리고 위에서 작성한 error.html로 view를 바인딩해줄 것이다.
- /make/error에 접속시 바로 예외를 던지게 만들었다.

- 테스트를해보면 잘 동작하는 것을 알 수 있다.

#### 구체적인 타입으로 매핑

[##_Image|kage@yRBBn/btqAnm29jPe/oO7PG1NTk9LeFY3Zn1YCgK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- EventException Handler와 EventException의 부모인 RuntimeException Handler를 만들어서 테스트를 해보면 **더 구체적인 타입인 EcentException Handler가 동작된다.**

[##_Image|kage@bq483U/btqAmsW1SGd/0d0Z5gPaLkXWPQudKR039K/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 메소드 파라미터가 아닌 Annotation에도 넣어줄 수 있으며 **한 번에 여러 가지 타입을 정의** 할 수도 있다.

---

### REST API의 경우
- REST API의 경우 응답 본문에 에러에 대한 정보를 담아줘야 한다.
- ResponseEntity를 통해 상태 코드를 설정하고 body에 에러 정보를 담아 줄 수 있다.

[##_Image|kage@bFrfMy/btqApkwjSNY/MDJhe3Ew1EOP4Zawy2W9f0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- /api/events/exception/test로 포스트 요청을 보내면 예외를 던지게 만들었다.

[##_Image|kage@T0Abf/btqAmt9uQSd/G3ag0QxtAYz4NHrzEs4Hk1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@0ONhs/btqAnozU7MX/KppHxoJxIh53N72o9q0p0k/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 실제 테스트를 해보면 잘 동작하고 body에 에러정보가 들어가 있는 것을 확인할 수 있다.

---

[참고 자료](https://www.inflearn.com/course/%EC%9B%B9-mvc)
