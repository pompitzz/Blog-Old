# WebSocket

-   웹 소켓 프로토콜은 TCP 커넥션 위에서 클라이언트와 서버 간 양방향, 전이중 통신 채널을 수립하는 표준화된 방법을 제공한다.
-   웹소켓의 상호작용은 HTTP 요청에 Upgrade 헤더를 사용하여 웹 소켓 프로토콜로 스위치 한다.

### HTTP 요청

```
GET /spring-websocket-portfolio/portfolio HTTP/1.1
Host: localhost:8080
Upgrade: websocket // (1)
Connection: Upgrade // (2)
Sec-WebSocket-Key: QUIQmkdoq123asodqdpwqeWOE==
Sec-WebSocket-Protocol: v10.stomp, v11.stomp
Sec-WebSocket-Version: 13
Origin: http://localhost:8080
```

-   이런 식으로 WebSocket 요청이 이루어지는데 (1)을 보면 Upgrade헤더에 websocket을 명시하였다.
-   (2)는 Upgrade Connection을 사용한다는 뜻이다.
-   이외에 웹 소켓 키, 지원 가능한 프로토콜들, 버전을 명시하는 것을 알 수 있다.

### HTTP 응답

```
HTTP/1.1 101 Switching Protocols
Upgrade: websocket
Connection: Upgrade
Sec-WebSocket-Accept: 1qwduiEio2NSDuoiqdpwqeWOE==
Sec-WebSocket-Protocol: v10.stomp
```

-   위의 요청에 대한 응답이다.
-   일반적인 200 상태 응답이 아닌 101 Switching Protocols 응답이 나타난다.
-   Sec-WebSocket-Protocol에서 클라이언트가 보낸 프로토콜 중 선택한 프로토콜을 응답해준다.

> 성공적으로 핸드 셰이크가 완료되면 HTTP Upgrade Request의 아래 계층에 있는 TCP 소켓은 클라이언트와 서버가 메시지를 주고받을 수 있도록 열린 채로 유지된다.

-   위와 같이 HTTP Upgrade 요청을 보내기 위해서는 서버에 따로 설정을 해야 할 수도 있다.
-   클라우드 환경에서 애플리케이션을 구동한다면 웹소켓과 관련된 클라우드 제공자의 지침을 확인해야 한다.

### HTTP vs WebSocket

> WebSocket이 HTTP 업그레이드 요청으로 커넥션을 맺지만 HTTP와는 많은 구조적, 모델의 차이가 있다.

-   **HTTP는** 클라이언트가 애플리케이션을 사용하기 위해 URL에 접근하여 HTTP method, header, entity 등을 통해 요청을 한다.
-   그리고 서버는 그에 따른 요청에 응답을 통해 통신이 이루어진다.

-   하지만 **WebSocket은** 초기에 연결한 **오직 하나의 URL** 만 존재한다.
-   즉 모든 애플리케이션 메시지는 같은 TCP 커넥션을 통해 통신된다.
-   HTTP와는 완전히 다른 방식의 비동기식 **이벤트 중심** 메시징 아키텍처이다.
-   WebSocket은 HTTP 핸드 셰이크 과정에서 Sec-WebSocket-Protocol에서 STOMP와 같은 높은 수준의 메시징 프로토콜을 사용하도록 요청할 수 있다.

### 웹소켓 사용처

-   웹소켓은 웹페이지를 동적이며 인터렉티브 하게 만들 수 있다.
-   하지만 Ajax와 HTTP 스트리밍 혹은 long polling을 통해 간단하고 효과적으로 구현할 수도 있다.
-   예를 들어 메일이나 소셜 피드 같은 것들은 동적으로 구성되어야 하나 몇 분 정도는 딜레이가 있어도 큰 문제가 생기지 않는다. 이럴 경우는 위와 같이 HPTP스트리밍 혹은 폴링 방식을 활용하는 게 효율적일 수 있다.
-   딜레이뿐만 아니라 메시지 볼륨이 상대적으로 낮은 경우에도 HTTP 스트리밍과, 폴링 방식이 효과적일 수 있다.
-   하지만 금융과 관련된 것들은 실시간이 매우 중요하고, 게임은 실시간과 높은 용량이 필요하다.
-   이런 경우에 적합한 거 같다.

> **즉 웹 소켓은 매우 짧은 대기시간과 높은 볼륨이 필요한 경우에 매우 효과적인 대책이 된다.**

#### 참고사항(외부 프락시)

-   제어 위를 벗어난 프록시가 HTTP Upgrade Header에 대해 알지 못한다면 웹 소켓 커넥션을 방해할 수도 있다.

> 위에 설명에서 나왔듯이 STOMP와 같은 높은 수준의 메시징 프로토콜을 사용하면 Spring을 통해 어렵지 않게 웹 소켓을 구현할 수 있다.

> 다음 게시글에서는 STOMP에 대해 알아보고 간단한 채팅방을 구현해 볼 것이다.

---

[참고 자료(Spring Docs)](https://docs.spring.io/spring/docs/5.2.2.RELEASE/spring-framework-reference/web.html#websocket)
