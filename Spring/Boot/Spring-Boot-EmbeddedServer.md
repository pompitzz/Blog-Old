> [이전게시글](https://sun-22.tistory.com/31)에서 SpringBootApplication의 @EnableAutoConfiguration이 자동설정을 통해 스프링 부트가 웹 애플리케이션으로 구동되게 해준다는것을 알게되었다.

> 이번 게시글에서는 @EnableAutoConfiguration에 의해 빈으로 등록되어 웹 서버로 구동되게 해주는 ServletWebServerFactoryAutoConfiguration에 대해 알아본다.

[##_Image|kage@bIQqIA/btqAAGlYHzq/Nu5R99x1J1tP9EzC2b55z0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

ServletWebServerFactoryAutoConfiguration을 확인해보면 Tomcat을 제외한 Jetty, Undertow가 있는 것을 알 수 있다.

그렇기 때문에 pom.xml에 다른 서블릿 컨테이너를 등록하면 그 컨테이너로 실행이 된다.

[##_Image|kage@bwOCXH/btqAAGTRYqr/DfekOLeex1PUBhzIPBKKR0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

그리고 DispatcherServletAutoConfiguration에서 서블릿을 만들고 등록을 해주게 된다.

---

### **서블릿 컨테이너 변경하기**

**-** 기본적으로 스프링 부트로 개발할 때는 톰캣이 들어있어 톰캣을 쓰게 된다. **다른 서블릿 컨테이너**로 바꿔보자.

[##_Image|kage@LrzQx/btqAz3IZzFL/z3qLgI5xmptwFttKmEQM0k/img.png|alignCenter|data-lazy-src="" data-width="693" data-height="328" data-origin-width="773" data-origin-height="366" width="592" height="280"|||_##]

[##_Image|kage@AE2zD/btqAAGlYNLd/5r4G0C1MlFkN4I1igE26X1/img.png|floatLeft|data-lazy-src="" data-width="583" data-height="247" data-origin-width="583" data-origin-height="247" width="522"|||_##]

이렇게 tomcat을 제외시키고 jetty를 등록하면 의존성 관리 폴더에 tomcat은 사라지고 jetty가 생긴다.

[##_Image|kage@pvw30/btqABDvyabR/vuwtbz0r1gc6DB0cGmhKZ0/img.png|widthContent|data-lazy-src="" data-width="693" data-height="66" data-origin-width="735" data-origin-height="71"|||_##]

앱을 실행하면 톰캣이 아닌 jetty로 실행된 것을 확인할 수 있다.

---

### **포트 번호 변경하기**

[##_ImageGrid|kage@dHAYK5/btqAzyPW2qj/I3wRHnerBmXW2LK5jjIRK1/img.png,kage@kMOVS/btqAy1EPYUe/7f5WsKoFK1dieEnYHMlmM0/img.png|data-lazy-src="" data-width="325" data-height="33" data-origin-width="325" data-origin-height="33" style="width: 63.3639%; margin-right: 10px;",data-lazy-src="" data-width="204" data-height="37" data-origin-width="204" data-origin-height="37" style="width: 35.4733%;"|_##]

resource하위의 application.properties에서 왼쪽과 같이 포트번호를 지정할 수도 있다. 그리고 오른쪽처럼 0으로 지정하면 포트번호가 랜덤으로 정해진다.

[##_Image|kage@YOb5m/btqAz6k8pXS/3lrVpiiyq1FuPQwbbaH5j0/img.png|widthContent|data-lazy-src="" data-width="653" data-height="78" data-origin-width="653" data-origin-height="78"|||_##]

resource 폴더의 application.properties에 위의 내용 중 왼쪽 내용을 추가하면 **웹서버로 사용하지 않을 수 있고** 오른쪽처럼 **포트 번호도 변경**이 가능하다.

[##_Image|kage@buFKUK/btqAy06Z9fK/gIRJiw6CcqKjL5GrDyL7h0/img.png|widthContent|data-lazy-src="" data-width="693" data-height="121" data-origin-width="773" data-origin-height="135"|||_##][##_Image|kage@kWgpQ/btqABCKcuvK/ImswQ4yKIfiQv5RIeDXXE0/img.png|widthContent|data-lazy-src="" data-width="693" data-height="54" data-origin-width="682" data-origin-height="54"|||_##]

리스너를 생성하여 applicationContext에서 포트 번호를 받아올 수도 있다.

---

[

스프링 부트 개념과 활용 - 인프런

스프링 부트의 원리 및 여러 기능을 코딩을 통해 쉽게 이해하고 보다 적극적으로 사용할 수 있는 방법을 학습합니다. 중급 프레임워크 및 라이브러리 Spring Spring Boot 온라인 강의

www.inflearn.com



](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8)
