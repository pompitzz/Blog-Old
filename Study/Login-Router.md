> 이전 게시글들에서 제작하였던 SpringBoot를 이용한 Auth2.0 서버와 Vue 로그인, 회원가입 화면을 통해 로그인, 회원가입 기능을 구현하고 라우터 내비게이션 가드를 이용해 권한별 라우터 설정을 하는 게시글입니다.

---

### 회원가입 기능
- 우선 UI framwork를 Vuetify로 변경하였다.

#### 회원가입 입력 검증

[##_Image|kage@snLKf/btqAQlb77qT/UK4KsJClE5AmjdlXeXZMEk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- Resister.vue의 template 코드이다.
- 빨간 밑줄의 :rules를 통해 입력 검증이 가능하다.
- rules은 전달 인자(Argument)로 현재 입력 폼의 text를 넘겨겨준다.
- 그 값을 이용하여 아래와같이 검증을 할 수 있다.


[##_ImageGrid|kage@brw5w1/btqARX8YlRx/r3S8307XL8G3hYz679apok/img.png,kage@nRJpe/btqARiZ2FdK/CK7oPZCKDpEBEKtcBpEQxK/img.png|data-origin-width="0" data-origin-height="0" style="width: 58.2003%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 40.6369%;"|_##]

- 왼쪽 코드는 vuex의 state 코드이다.
- 각 검증을 ||(OR)을 통해 한 것을 알 수 있는데 즉 첫 번째 코드가 참이면 true가 되므로 뒤의 요구사항들이 출력되지 않게 된다.
- nameRules를 보면 전달 인자로 전달받은 v를 이용하여 !!v를 하였다.
- !!v를 하게 되면 v가 빈 값 일 땐 false를 반환하고 빈 값이 아니면 true를 반환한다
- v에 값이 존재하지 않을 때만 뒤의 검증 요구사항이 나타나게 된다.

> state의 값들을 Resister.vue에서는 mapState를 통해 전달받아 사용하였다.

[##_ImageGrid|kage@3ic11/btqAUvYb8on/6YbM8thNws9kvjGM6OjyM1/img.png,kage@c0LvVB/btqARW95sH5/0x5LJnpQ8erKEBKmNO7juk/img.png|data-origin-width="0" data-origin-height="0" style="width: 49.3579%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 49.4793%;"|_##]

- 확인을 해보면 정상적으로 검증이 이루어지는 것을 알 수 있다.

#### 회원가입 하기

- 위의 회원가입 입력 검증의 template 코드 중 초록 밑줄을 확인햅면 버튼 클릭시 joinRequest 메서드가 동작하게 된다.

![](https://k.kakaocdn.net/dn/06OF1/btqAQOZqA3j/4g9eSyjGC57uObm9vmAqO0/img.png)![](https://k.kakaocdn.net/dn/5ewJc/btqARxJuqyR/zEhCr91rZ73yQii6G2wKq0/img.png)

- joinRequest는 우선 위에서 회원가입 입력 검증이 이루어졌을 때만 동작하도록 한다.
- 검증되었다면 Vuex의 Actions 메서드인 REQUEST\_JOIN을 회원가입 작성폼의 데이터가 있는 member와 함께 호출한다.

```js
async REQUEST_JOIN(context, member) {
        try {
            const response = await requestJoinMember(member);
            context.commit('OPEN_MODAL', setModalTexts(true));
            return response;
        } catch (e) {
            context.commit('OPEN_MODAL', setModalTexts(false));
        }
    },
```

- 비동기 처리를 담당하는 actions의 REQUEST\_JOIN이다
- async & await을 통해 비동기 처리를 하는 것을 알 수 있다.
- 가장 최신에 등장한 비동기 처리 문법으로 Promise기반으로 동작하며 이전의 Promise에서는 네트워크 에러만 catch로 잡아줬지만 async & await은 JS 내부 에러까지 잡아주는 것으로 알고 있다.
- async를 선언하고 비동기 처리를 받을 함수에 await을 붙여주면 된다.
- requestJoinMember를 호출하여 api통신 결과를 받고 회원가입이 완료되면 로그인 화면으로 라우팅 시켜주는 Modal을 띄워준다
- 실패하면 다시 한번 더 시도해 달라는 Modal을 띄워준다.

[##_Image|kage@cFI56X/btqAU7bIelB/l4GOfDsnQDFVZkWE1whOZ1/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]

- api 통신을 수행하는 requestJoinMember이다.
- http 통신 라이브러리인 axios를 이용하여 post요청으로 서버에 회원가입을 요청한다.

[##_ImageGrid|kage@cotGAe/btqAQOrAVJI/t4M3DVg41JCM7kbMDUY7ik/img.png,kage@cBHL05/btqAQ3WoJ0Z/kInnx4verWEgKpKmMgIJm0/img.png,kage@b1emfi/btqAUvjAOyi/CsEckskYZxjtBA6nSHVdEK/img.png|data-origin-width="0" data-origin-height="0" style="width: 28.4865%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 28.2031%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 40.9848%;"|_##]

- 회원가입 실패 시 실패 Modal, 성공 시 로그인 페이지로 이동시켜주는 Modal을 띄워주는 것을 알 수 있다.
- 이 Modal은 Component화 하여 재활용 하였다.
- 그리고 오른쪽은 서버의 log 내역이다.
- 회원가입 요청 성공 시 정상적으로 DB에 쿼리가 날아가는 것을 알 수 있고 아래처럼 DB 데이터에 정상적으로 값이 들어가 있는 것을 알 수 있다.

[##_Image|kage@yzSDf/btqAUursaXn/p8Rcd954QcURSvzvkeFSk0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

---

### 로그인 기능

#### 로그인 하기

> 입력 검증은 회원가입과 동일하게 진행되므로 생략한다.

[##_ImageGrid|kage@c8OuvP/btqAUuEZGAD/nNi1KlAahJwTw3HL1rtS40/img.png,kage@Qecv6/btqAQkYyGaS/U60xL1DsdVdn3bE9wPB1R0/img.png|data-origin-width="0" data-origin-height="0" style="width: 38.5461%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 60.2911%;"|_##]

- 로그인 버튼을 누르면 loginRequest 메서드가 실행되고 REQUEST\_LOGIN이 member의 전달 인자와 함께 실행된다

```js
 async REQUEST_LOGIN(context, member) {
        try {
            const response = await requestLogin(member);
            context.commit('LOGIN_SUCCESS', response.data);
            return response;
        } catch (e) {
            context.commit('OPEN_MODAL', {
                    title: '로그인 실패',
                    content: '다시 한번 더 시도해주세요.',
                    option: '닫기'
                }
            )
        }
    }
```
- actions의 REQUEST_LOGIN이다.
- 회원가입과 비슷하게 requestLogin을 통해 api 로그인 요청을 보낸다.
- 정상적으로 동작되었다면 commit을 통해 state의 값 변경 로직을 담당하는 mutations의 메서드인 LOGIN\_SUCCESS를 response.data와 함께 호출하게 된다.
- response.data에는 토큰 정보들이 담겨 있다.

```js
function requestLogin(member) {
    let form = new FormData();
    form.append('username', member.email);
    form.append('password', member.password);
    form.append("grant_type", "password");
    const requestData = {
        url: `${config.baseUrl}/oauth/token`,
        method: "POST",
        auth: {
            username: process.env.VUE_APP_CLIENTID,
            password: process.env.VUE_APP_CLIENTSECRET,
        },
        data: form
    };
    return axios(requestData);
}
```

- 서버에 로그인을 요청하는 api코드인 requestLogin이다.
- form에 username으로 email, password에 password, grant\_type을 password타입으로 설정한다.
- auth를 통해 clientId와 secret을 담아 요청을 보낸다.
- 정상적으로 요청이 성공하면 위의 REQUEST\_LOGIN에 reponse로 Response Resource들을 담아 리턴해줄 것이다.

[##_Image|kage@baL8e2/btqAXPCVOan/qCnE3NcsgU1PHvTKqJM4dk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 로그인 요청이 성공하면 actions의 REQUEST\_LOGIN에서 commit을 통해 mutations의 LOGIN를 호출하게 된다.
- 전달 인자로 받은 토큰 정보들을 아래 코드와 같이 localStorage에 저장하고 axios의 디폴트 헤더 설정으로 accessToken 값을 등록시켜준다.
- 마지막으로 로그인 성공 시메인화면으로 push 해준다.
- 로그아웃은 로그아웃 버튼을 누르면 commit을 통해 호출하게 하였으며 localStorage와 header의 토큰값을 제거하여 인증정보를 초기화하였다. 

```js
const setTokenInLocalStorage = (tokenInfo) => {
    localStorage.setItem("access_token", tokenInfo.access_token);
    localStorage.setItem("refresh_token", tokenInfo.refresh_token);
};

const deleteTokenInLocalStorage = () => {
    localStorage.removeItem("access_token");
    localStorage.removeItem("refresh_token")
};

const setAccessTokenInHeader = (accessToken) => {
    axios.defaults.headers.common['Authorization'] = accessToken;
};

const deleteAccessTokenInHeader = () => {
    axios.defaults.headers.common['Authorization'] = null;
};
```

---

### 권한별 라우터 설정

[##_ImageGrid|kage@ba9kUu/btqASy81MbI/VDkkP7kDF2nrkBwA8Znplk/img.png,kage@bS5eFu/btqASyA7r9d/oRKgL2xmiJyuXZ7b67QRCK/img.png|data-origin-width="0" data-origin-height="0" style="width: 40.6312%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 58.206%;"|_##][##_ImageGrid|kage@6xnjK/btqAUvjA0bI/pc0tJIysYV9NpvPu2oA3fk/img.png,kage@bawmee/btqAQOLVPKX/oVFfj0KikkmPXDUXinZTd1/img.png|data-origin-width="0" data-origin-height="0" style="width: 55.2143%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 43.623%;"|_##]

- 우선 vues getters를 이용하여 inAuthenticated 메서드를 통해 state에 토큰이 있는지 없는지 확인한다.
-로그인 시 로그아웃 버튼이 보이고 로그인이 되어있지 않으면 로그인 회원가입 버튼이 보이게 하였다.

[##_ImageGrid|kage@uhfsj/btqASz7QTAV/U3HcRFBWNMpB5w40RvqmCK/img.png,kage@dlhfWS/btqARwRgSxs/rAy7gGszRo1JiZUYis38p1/img.png|data-origin-width="0" data-origin-height="0" style="width: 45.7588%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 53.0784%;"|_##][##_ImageGrid|kage@s0VpK/btqARxbF6VY/SOgesfd2XzLhSy8dgWRcSK/img.png,kage@dHfTm3/btqARyBFj4t/G6GDAUOfyLfl7SkULjkDuk/img.png|data-origin-width="0" data-origin-height="0" style="width: 52.8246%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 46.0126%;"|_##]

- 그리고 라우터 내비게이션 가드인 beforeEnter를 이용한다.
- Memo 라우터로 가기 전에 로그인된 유저인지 확인하여 로그인되지 않았으면 no-auth 라우터로 라우팅하고 로그인된 유저라면 memo 라우터로 라우팅 되게 설정하였다.
