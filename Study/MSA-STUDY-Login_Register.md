> [vue 프로젝트 생성](https://sun-22.tistory.com/28?category=363029)은 해당 게시글을 참조하시면 됩니다.

> 이해가 안 되는 부분들은 [공식 사이트](https://vuejs.org/)를 참조하시면 많은 도움이 될 거 같습니다.

---

### Vue Router, Vuex, Axios 설치
Vue Router
- 싱글 페이지 애플리케이션을 구현할 때 사용하는 라이브러리

Vuex
- vue.js 애플리케이션을 위한 상태 관리 패턴과 라이브러리

Axios
- 뷰에서 권고하는 Promise 기반의 HTTP 통신 라이브러리

[##_Image|kage@S66rd/btqAQNRMSSx/Fi5K0IEq2jGmRX3UOmkqxk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- vue create로 생성한 프로젝트의 package.json을 보면 core-js, vue만 의존성이 추가된 것을 알 수 있다.
- 화면 제작에 앞서 뷰의 코어 라이브러리인 vue router, vuex와 axios를 설치한다.

[##_ImageGrid|kage@bmX6Xv/btqAMOE5gLT/aeKQIC4Wzar7DhlTIVBQ6K/img.png,kage@bxG8nr/btqAMOrwPf3/AfwqSQxW0kMOJ9gAK1dnOK/img.png|data-origin-width="0" data-origin-height="0" style="width: 73.135%; margin-right: 10px;",data-origin-width="356" data-origin-height="157" style="width: 25.7022%;"|_##]

- npm을 통해 설치한 후 package.json을 보면 의존성이 추가된 것을 확인할 수 있다.

### **Router 확인하기**

[##_ImageGrid|kage@uFHtE/btqAMkK3gJs/YUItk3KBaZtEKkOc3XKkg0/img.png,kage@cfElOf/btqAPeoOk21/uKbDFKHk5fdRvGEs6Xo0H1/img.png|data-origin-width="0" data-origin-height="0" style="width: 47.7161%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 51.1211%;"|_##]
- 기존의 HelloWorld.vue를 삭제하고 components폴더에 Simple.vue를 하나 생성한다. - 그리고 App.vue에 기존 내용을 삭제하고 router-view 태그만 작성해준다.

<br>

[##_ImageGrid|kage@beL8fG/btqAQmmABLN/WwQTZtKH1JdeBsfZIxtIqK/img.png,kage@ez65Ii/btqAMP43eCf/jO3PaRQklhKTVwkzGj1oK1/img.png|data-origin-width="0" data-origin-height="0" style="width: 35.0285%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 63.8087%;"|_##]
- src이하에 routes폴더를 만들고 route.js를 생성한 후 VueRouter를 등록해준다.
- 그후 router 인스턴스를 하나 생성하고 routes에 path와 component를 설정할 수 있다.
- path가 '/'일땐 /simple로 redirect 해주었다.
- 그 후 가장 하단에 export{router}를 통해 외부에서 사용 가능하게 해준다.

<br>

[##_Image|kage@bWHWJ8/btqAOIDERwC/cFS34rudpsSMHO8uvC2Hn0/img.png|alignCenter|data-origin-width="749" data-origin-height="226"|||_##]
- App.vue의 router-view가 routes에서 설정한 component를 보여준다.
- 그리고 0마지막으로 main.js에 router를 등록해준다.

<br>

[##_Image|kage@cQO3uv/btqAMOE5klf/qeK6RvkKhZMQdisXUu1BBK/img.png|floatLeft|data-origin-width="0" data-origin-height="0" width="384" height="149"|||_##]

- npm run serve 후 브라우저를 통해 확인해보면 /simple로 redirect되어 정상적으로 router가 동작하는 것을 알 수 있다.

---

### MD Bootstrap Vue

- 간편한 반응형 디자인 개발을 위해 Bootstrap의 일종인 Material Design for BootStrap Vue를 사용할 것이다.
- [Document](https://mdbootstrap.com/docs/vue/)

[##_Image|kage@bj6bmE/btqAOcrXSw9/wbfetprphsfZ4amlMxo0bk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 터미널에 vue add mdb를 입력하면 아래 빨간 박스와 같이 원하는 옵션을 선택하면 된다.
- Font Awesome과 Robot font도 사용할 예정이므로 추가해줬다.

<br>

[##_ImageGrid|kage@b54I6M/btqAQmACnnW/cLKEqV1KeHOLneMzsuYKZ0/img.png,kage@b3oDIS/btqAQmtQ9gW/dOK4s2JPE8AA6fHSZ1bwek/img.png|data-origin-width="0" data-origin-height="0" style="width: 58.2609%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 40.5764%;"|_##]
- Simple.vue에 mdbBtn 컴포넌트를 추가하고 template을 위와 같이 작성하고 페이지를 확인해보면 정상적으로 MDBootstrap이 적용된 것을 알 수 있다.


### 로그인, 회원가입 화면 만들기
[##_ImageGrid|kage@O3iwv/btqAN3VMWnX/tN4MzPVsrdamAYLlCtXB2k/img.png,kage@bIw7MO/btqAOI4JC2x/a8fiLhStQTtu9QuTLIosg0/img.png|data-origin-width="0" data-origin-height="0" style="width: 40.328%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 58.5092%;"|_##]

- 우선 라우터 간 이동을 하기 위한 Navbar를 MDB를 커스텀하여 하나 만들고 App.vue의 router-view위에 넣어줬다.
- 해당 문서를 보면 사용법이 자세히 설명되어 있다.

<br>

[##_ImageGrid|kage@mk0xg/btqAOJii0YK/u0N8hbXigZ3b9pU82tP5W0/img.png,kage@bUub9l/btqAMkxq7HM/aSMlsK5teGGzmzOUlXOLk1/img.png|data-origin-width="0" data-origin-height="0" style="width: 48.2594%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 50.5778%;"|_##]

- UI framwork를 이용하면 매우 간단하게 반응형 웹을 만들 수 있다.
- 위의 아이템로그인, 회원가입을 클릭하면 각 화면으로 라우팅이 될 것이다.

<br>

[##_Image|kage@djmjrn/btqAOcdZaqg/xDvRztExUknUjBAemnsU60/img.png|floatLeft|data-origin-width="0" data-origin-height="0" width="277" height="267"|||_##]

- 이전에 만든 Simple.vue를 Main.vue로 변경하고 routes 설정도 그에 맞게 변경하였다.
- Login, Register vue를 생성하고 roter에 각 경로에 맞게 등록시켰다.
- 마지막으로 각 router에 name을 설정해주었는데 라우팅 시 path 경로가 아닌 이 이름 사용하여도 라우팅이 가능하다.
- 혹은 현재 라우팅 정보를 쉽게 알수 있으므로 유용하다.

<br>

[##_Image|kage@bCoUE0/btqAOIYve12/be98xpH5KBnAGicTxvk6R1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]
- Navbar의 template이다.
- 각 메뉴 태그를 router-link로 설정하고 to='/경로'로 설정할 것을 알 수 있는데 각 메뉴를 클릭하면 to에 등록된 경로로 라우팅 된다는 뜻이다.
- 그리고 :class도 동적으로 바인딩이 가능하다.
- 중괄호에서 오른쪽 부분이 참이면 왼쪽의 active가 클래스에 추가된다는 뜻이다.
- active 클래스가 활성화 되면 해당 부분을 강조하여 유저가 현재 어느 경로에 있는지 쉽게 인지가능하게 할 수 있다.

[##_ImageGrid|kage@RwIFo/btqANCD882w/ec80qsvPhJLBDPZKj6Lznk/img.png,kage@IzqLK/btqAPcYORiy/4HOUy2X0bzbRHWvK4ijRdK/img.png|data-origin-width="0" data-origin-height="0" width="321" height="415" style="width: 50.7807%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 48.0565%;"|_##]

- 로그인, 회원가입 화면을 디자인하고 확인해보면 위와 같이 메뉴를 클릭했을 때 정상적으로 라우팅이 되며 로그인 라우터일 때 로그인이 강조되는 것을 알 수 있다.

---
[소스코드](https://github.com/DongmyeongLee22/msa-study)
