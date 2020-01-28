# 엔티티와 인코딩
> HTTP는 수십억개의 다양한 미디어를 전송할 수 있고 올바르게 처리될 수 있게 다음을 보장한다.

- Content-Type과 Content-Language 헤더를 통해 객체는 올바르게 식별되어 처리된다.
- Content-Length와 Content-Encoding 헤더를 통해 객체는 올바르게 압축이 풀린다.
- 엔터티 검사기와 캐시 만료 제어를 통해 객체는 항상 최신이다.
- 데이터 압축, 범위 요청등 다양한 기법을통해 네트워크를 효율적을 이동한다.
- 전송 인코딩 헤더와 체크섬을 이용해 조작되지 않고 온전하게 도착한다.

### 메시지는 컨테이너, 엔티티는 화물
- HTTP 메시지가 운송 시스템의 컨테이너라면 HTTP 엔티티는 화물이다.

```HTTP
HTTP/1.0 200 OK
ServerL: Netscape
Date: Sun, 17m, ...
Content-type: text/plain
Content-length: 18

Hello World
```
- 간단한 타입과 길이로 엔티티 헤더를 표현할 수 있다.

#### HTTP/1.1의 헤더 필드들
Content-Type
- 객체의 종류를 표시

Content-Length
- 본문의 크기를 바이트 단위로 나타냄
- 청크 인코딩이 아니라면 필수적으로 메시지에 이써야 한다.
- 길이가 없다면 커넥션이 끝나면 이게 다 응답된건지 알 수 없다.
- 그래서 보통 캐시 프락시 서버는 content-length가 없다면 캐시 하지 않는다.
- 지속커넥션에도 필수이다.
- length를 통해 시작과 끝을 알 수 으므로 지속 커넥션을 맺을 수 있다.
- 만약 encoding되어 압축되었다면 압축된 길이를 보내야 한다.

Content-Language
-

Content-Encoding
Content-Location
Content-Range: 부분 엔티티라면 어느 부분인지
Content-MD5: 엔티티 본문 콘텐츠의 체크섬
Last-Modified
Expires
Allow
ETag: 이 인스턴스에 대한 고유한 검사기
Cache-Control

### MIME and Charset
- MIME 타입은 전달되는 데이터 매체의 기저형식의 표준화된 이름
- 클라이언트 애플리케이션은 콘텐츠를 적절히 해독하고 처리하기 위해 MIME 타입을 이용한다.

```js
txet/html: 엔티티 본문이 html 문서
text/plain: 엔티티 본문은 플레인 텍스트
image/gif
imgage/jpeg
audio/x-wav: 엔티티 본문은 wav 음향 데이터를 포함
multipart/byteranges
```

#### 멀티파트 미디어 타입
- MIME 멀티파트 이메일 메시지는 서로 붙어있는 여러개의 메시지를 포함하여 하나의 복합 메시지로 보낸다.

#### 멀티파트 폼 제출
- HTTP 폼을 채워서 제출하면
