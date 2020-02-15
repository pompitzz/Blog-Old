# @ModelAttribute 사용법

> 1\. @RequestMapping을 사용한 핸들러 메서드의 아규먼트에 사용하기
- [지난 게시글](https://sun-22.tistory.com/49?category=363027)에서 사용해본 방법이다.

<br>

> 2\.@Controller 또는 @ControllerAdvice를 사용한 클래스에서 모델 정보를 초기화할 때 사용한다.
###  첫번째 방법
[##_Image|kage@cGvm24/btqAm4OUGf9/gWmrmweMy0ZBrDQvDV08JK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 모든 핸들러에서 공통적으로 참고해야 하는 모델 정보가 있을 때 매번 각 핸들러에서 모델 정보를 받지 않고  @ModelAtrrtibute를 이용하면 모든 핸들러에게 제공된다.

#### 테스트

[##_Image|kage@bgG7GX/btqAmthar6p/DDS4fqU4M4r6jwuPKVPevK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@qB3lF/btqAnozEeNB/wYr3CC8gmos3dHUbqw7Nn0/img.png|alignCenter|data-origin-width="1049" data-origin-height="214"|||_##]

###  두번째 방법

[##_Image|kage@bnaaU5/btqAnnU1bl0/M16KeqjVkgHKI3mNxNjwP1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@SAKtS/btqAms3A4cS/ziqvOezaKd4QVihkEYLniK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 만약 모델에 담을 객체가 하나라면 이렇게 하여도 정상적으로 동작한다.

---

[참고 자료](https://www.inflearn.com/course/%EC%9B%B9-mvc)
