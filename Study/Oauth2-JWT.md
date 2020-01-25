# OAuth

### OAuth(Open Authorization, Open Authentication)란?
 - 인터넷 사용자들이 비밀번호를 제공하지 않고 다른 웹사이트 상의 자신들의 정보에 대해 웹사이트나 애플리케이션의 접근 권한을 부여할 수 있는 공통적인 수단으로 사용되는 접근 위임 개방형 표준.

### OAuth 2.0 Authorization Framework
 - OAuth 1.0a을 개선하여 나온 최신의 OAuth 기술
 - OAuth 1.0a는 HTTPS가 필수가 아니어서 signature를 따로 생성해서 호출해야 했지만 OAuth 2.0은 Bearer 토큰 인증 방식으로 간편화 하였다.
 - OAuth 2.0은 다양한 인증 방식을 제공한다
 - OAuth 2.0은 Authorization Server를 분리함으로써 인증 서버를 확장할 수 있다.

### Roles
#### Resource Owner(자원 소유자)
- 보호된 리소드에 대한 액세스를 승인할 수 있는 엔티티
- 실제 앱을 사용하는 사용자라고 할 수 있다.

#### Resource Server
- Access Token을 통해 보호된 리소스 요청을 수락하고 그 리소스를 응답에 담아 보내주는 서버

#### Client
- Resource Owner의 보호된 리소스를 요청하는 애플리케이션.

#### Authorization Server
- Resource Owner를 성공적으로 인증하고, 권한을 얻으면 Client에게 Access Toeken을 발급하는 서버



> 네이버나 구글 OAuth 2.0 서비스를 사용하면 ResouceServer, Authorization Server는 네이버, 구글이 되고 Client는 개발하는 애플리케이션이며 Resource Owner는 그것을 사용하는 사용자이다.


---

### OAuth 2.0 Protocol Flow

- (A) Client는 Resource Owner에게 권한(Authorization)을 요청한다. 이 승인은 직접 혹은 Authorization Server를 통해 간접적으로 이루어질 수 있다.
- (B) Resource Owner가 승인 시 Client는 Authorization Grant(권한 증서)를 받는다. Authorization Grant 방식은 다양하며 클라이언트의 요청이나 인증 서버가 지원하는 유형에 따라 달라질 수 있다.
- (C) Client는 인증서버에 Authorization Server에 인증과 권한 증서를 제시함으로써 Access Token을 요청한다.
- (D) Authorization Server는 Client를 검증하고 권한 증서가 유요 하면 Access Token을 발급한다.
- (E) Client는 Access Token을 제시함으로써 Resource Server에 보호된 리소스를 요청한다
- (F) 리소스 서버는 그 Access Token을 검증하고 검증되었다면 서버에게 보호된 리소스를 전송한다.



### Access Token and Refresh Token
#### Access Token
- OAuth 2.0은 Access Token을 주고받는 것으로 인증하게 되는데 HTTP 요청 시 Authorization: Bearer [access token] 이렇게 토큰을 넣어 전송하면 된다.

#### Refresh Token
- Client가 같은 Access Token을 계속해서 사용하면 그 토큰이 노출되었을 때 위험성이 높다.  그러므로 OAuth 2.0에서는 Refresh Token을 활용하여  Access Token에 만료기간을 두고 만료가 될때 Refresh Token을 통해 새로운 Access Token을 갱신 할 수 있다.

### Refreshing and Expired Access Token Flow
- (A) Client는 Authorization Grant를 제시 함으로써 Authorization Server에게 Access Token을 요청한다
- (B) Authorization Server는 Client를 인증하고 Authorization Grant를 검증하여 유효하다면 Access Token과 Refresh Token을 발행하여 보내준다.
- (C) Client는 Access Token을 제시함으로써 Resource Server에게 보호된 리소스를 요청한다.
- (D) Resource Server는 Access Token을 검증하고 만약 유효하다면 서버에게 보호된 리소스를 제공한다.
- (E) Access Token이 만료될 때까지 Step (C), (D)를 반복한다. 만약 Client가 Access Token이 만료된 것을 알고 있으면 바로 (G)로 넘어가 요청을 하고 그렇지 않다면 계속해서 Resource Server에게 요청한다.
- (F) Access Token이 만료되었다면 Resource Server는 Invalid Token Error를 리턴한다.
- (G) Client는 Refresh Token을 제시 함으로써 Authorization Server에게 새로운 Access Token을 요청한다.
- (H) Authorization Server는 Client를 인증하고 Refresh Token을 검증한다. 만약 Refresh Token이 유효하다면 새로운 Access Token을 발행하며 상황에 따라 새로운 Refresh Token도 발행해준다.

---
### 4가지 Authorization Grant
#### 1\. Authorization Code Grant
 - Client가 다른 사용자 대신 특정 리소스에 접근을 요청할 때 사용한다. 리소스 접근을 위한 사용자명과 비밀번호, 권한 서버에 요청해서 받은 권한 코드를 함께 활용하여 리소스에 대한 Access Token을 받으면 이를 인증에 사용하는 방식

#### 2\. Implicit Grant
- Authorization Code Grant와 다르게 Authorization Code 교환 단계 없이 Access Token을 즉시 반환받아 이를 인증에 이용하는 방식이다.
- 많이 사용되는 방식으로 모바일 애플리케이션이나 자바스크립트 기반의 웹 애플리케이션에서 사용

#### 3\. Resource Owner Password Credentials Grant
- Client가 암호를 사용하여 Access Token에 대한 사용자의 자격증명을 교환하는 방식
- Clinet에 아이디/암호를 저장해 놓고 직접 Access Token을 받는 방식이다.

#### 4\. Client Credentials Grant
- Client가 Context 외부에서 Access Token을 얻어 특정 리소스에 접근을 요청할 때 사용하는 방식

### Resource Owner Password Credentials Grant Flow
- 실제 구현을 Password Grant로 할 것이기 때문에 Password Grant만 본다.
- (A) Resource Owner는 Client에게 username과 password를 제공한다.
- (B) Clinet는 Resource Owner로부터 받는 자격 정보들을 포함하여 Authorization Server에게 Access Token 발행을 요청
- (C) Authorization Server는 Client를 인증하고 Resource Owner의 자격 정보를 검증하여 만약 유효하다면 Access Token을 발급한다.

> 파라미터로 grant_type="password", username="username", password="password", scope="scopes"를 요청하면 된다.

---

### JWT
#### JWT(JSON Web Token)
- JSON 객체를 사용하여 가볍고 self-contained 방식으로 정보를 안정성 있게 전달해주는 방식
- self-contained는 필요한 모든 정보를 자체적으로 가지고 있다는 뜻으로 JWT로 발급된 토큰은 토큰에 대한 기본정보, 전달할 정보(유저 정보), 토큰 검증 유무에 대한 정보를 포함하고 있다.

#### 장점
- 유저가 서버에 요청을 할 때마다 JWT를 포함하여 전달하게 되면 서버는 그 토큰을 검증하고 권한을 처리하면 된다. 따로 세션을 유지할 필요 없이 요청받은 토큰만 검증하면 되므로 서버 자원을 아낄 수 있다.

#### 단점
- 모든 정보를 가지고 있으므로 서버가 이 토큰에 대한 통제를 하기가 어렵다. 사용된 키를 무효화하게 되면 그 키로 서명된 모든 JWT가 무효화된다.

#### JWT 구조



- JWT는 점(.)으로 구분되고 HEADER(Alogorithm, Token Type), PAYLOAD(DATA), VERIFY SIGNATURE로 구성되어있다.
- https://jwt.io/ 에 들어가면 볼 수 있는 JWT의 Encoded, Decoded이다.


#### 1\. HEADER
Decoded의 HEADER는 보면 algorithm: HS256의 해싱 알고리즘을 적용하였고 type: JWT를 사용하는 것을 명시되어있다.

#### 2\.PAYLOAD
PAYLOAD에는 토큰의 정보들이 들어있다. PAYLOAD는 클레임(Claim)으로 구성되어 있고 클레임은 name/value 쌍으로 되어있다. 클레임의 종류는 3가지가 있다.

1\) Registered Claim
- 이미 정해진 클레임들로 선택적으로 포함하거나 제외할 수 있다
- 위에서 sub, iat가 Registered Claim이다.
- sub: 토큰 제목, iat: 토큰 발급시간이며 NumericDate 형태로 되어있다.
- 그 외에도 aud(토큰 대상자), exp(토큰 만료시간), nbf(토큰 활성 날짜) 등이 있다.

2\) Public Claim
- 공개 클레임들은 충돌이 방지된 이름을 가져야 하며 URI 형식으로 짓는다. 위의 사진엔 존재하지 않는다.

3\) Private Claim
- Client와 Server 협의하에 사용되는 클레임들로 공개 클레임과는 달리 충돌이 될 수 있다.
- 위의 사진에서 name: "John Doe"가 Private Claim이다.

#### 3\.VERIFY SIGNATURE
- 이 부분은 HEADER와 PAYLOAD를 인코딩하고 비밀키로 합친 후 해쉬 하여 생성한다.  위 사진을 보면 알 수 있다.

---

OAuth 2.0 내용들은 [RFC 6749](https://tools.ietf.org/html/rfc6749)를 JWT는 [공식사이트](https://jwt.io/)를 참조하였습니다.
