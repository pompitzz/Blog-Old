# ApplicationEventPublisher
- 이벤트 프로그래밍에 필요한 인터페이스 제공. **옵저버 패턴 구현체로** 이벤트 기반의 프로그래밍에 유용한 인터페이스이다.
- ApplicationContext은 EventPublisher를 상속받고 있다.​

#### [옵저버 패턴 참고](https://sun-22.tistory.com/3?category=363035)

### 예제

[##_Image|kage@c8CNaC/btqAt6R7cWu/C2IsjBwaXUmKhsa9nr1Nj0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]
- MyEvent 클래스를 만들고 ApplicationEvent를 상속받은 후 data를 하나 추가하고 data가 포함된 생성자를 만든다.
- 이 이벤트를 **빈으로 만들지 않는다.** 이 이벤트를 받을 **핸들러를 빈으로** 하여야 한다.

<br>

[##_Image|kage@HnKgD/btqAritdELb/klHvIN7mKKhgRGreF0kQp0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]
- AppRunner에서 ApplicationEventPublisher를 이용해 이벤트를 생성할 수 있다.
- 생성된 이벤트를 받을 핸들러를 만들어야 한다.

<br>

[##_Image|kage@xHt8X/btqAtBrijEd/Tc34ctTiSWgg6y6wYhAunk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

[##_Image|kage@bE6AYk/btqAuwiJdmy/VFkhyXkW1CpHCDxhk1aSm0/img.png|floatLeft|data-origin-width="0" data-origin-height="0"|||_##]

- 이전에는 ApplicationListener <T>를 상속받아 이벤트를 핸들링해야 했지만 현재는 annotation만 붙이면 된다.

#### ApplicationEvent 상속제거(POJO)

[##_Image|kage@srGgr/btqAt6q22Jy/WydUwYH3oWon64DkKpOj11/img.png|floatLeft|data-origin-width="534" data-origin-height="272" width="480" height="245"|||_##]

- 스프링 4.2 이후 에는 MyEvent를 상속받지 않고 이렇게 구현하여도 정상적으로 이벤트가 생성된다. 
- 이것이 스프링이 주요하게 생각하는 전력 중 하나인 POJO(Plain Old Java Object)이다. 
- POJO란 객체지향 원리에 충실하면서, 특정한 환경이나 규약에 종속되지 않고 필요에 따라 재활용될 수 있는 방식으로 설계된 객체이며 이것을 통해 개발자는 테스트 코드 작성이 간결해지며 유지보수성이 좋아진다.

---

### EventHandler가 두 개일 때
- EvnetHanler가 여러 개 일 때는 실행 순서를 **알 수 없다. 그렇기 때문에 따로 순서를 정할 수도 있다.****

#### @Order

[##_ImageGrid|kage@cpDdtv/btqAsSN7jfh/YTVGUM7dXvGlnPnjowf1lk/img.png,kage@btBYWG/btqAt5ZXPnp/2DcqdIg9yff1e45mlOkWf0/img.png|data-origin-width="0" data-origin-height="0" style="width: 47.8701%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 50.9671%;"|_##][##_Image|kage@ZQ4UN/btqAt6dwZCd/Uo6JKfgML2PUbroUDz8JYK/img.png|floatLeft|data-origin-width="0" data-origin-height="0"|||_##]

- @Order로 순서를 정해줄 수 있으며 숫자가 더 높은 Another가 늦게 실행되는 것을 확인할 수 있다.
- 물론 **스레드는 같은 main 스레드에서 실행(동기)** 되는 것이다.
- 비동기로 이벤트를 핸들링하고 싶으면 @Order대신 @Async를 붙이고 MainApplication에 @EnableAsync를 붙이면 제각기의 쓰레드에서 실행되어 비동기로 핸들링된다.

---

[참고 자료](https://www.inflearn.com/course/spring-framework_core)
