# Environment
> 프로파일과 프로퍼티를 다루는 인터페이스이다.

### Profile
- Bean들의 그룹이다. 특정 환경에서 어떠한 빈들을 등록할 때 사용할 수 있다.
- 테스트 환경에서는 A그룹의 빈을 사용하고 배포 환경에서는 B 그룹의 빈을 사용하게 할 수 있다.

[##_ImageGrid|kage@v85Yj/btqAnnuBrfm/zIdLZ4dxk0O2bKH1vcqaa0/img.png,kage@4HC4F/btqAnoAjnVv/xfAtFS2kEWb9uREq02VluK/img.png|data-origin-width="882" data-origin-height="355" style="width: 60.3191%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 38.5181%;"|_##]

- ApplicationContext가 Environment를 상속받아 사용되기 때문에 Environment를 받아올 있다.
- 현재 아무런 설정을 하지 않았기 때문에 Active 값은 ""이며 Defalut 값은 "default"로 설정된다.

#### Profile 사용해보기

[##_ImageGrid|kage@1dsRl/btqAo6rYd3M/zeKD1V4n6L8KVVS2U4432k/img.png,kage@dBEDBN/btqArirairH/DZJnTiAMcklVxokbmzrdE0/img.png|data-origin-width="0" data-origin-height="0" style="width: 42.5071%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 56.3301%;"|_##]

- @Profile을 이용하여 특정 Profile에만 원하는 로직을 수행할 수 있다.
- test Profile에서만 BookRepository를 빈으로 등록한다.
- test Profile이 아닐 경우 BookRepository는 빈으로 등록되지 않으므로 사용할 수 없다.

#### Profile 설정이 안 되어 있을 때

[##_Image|kage@bWyu1d/btqAnomMS7V/ZnyH9I82KfKUfaW1jKMWLk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@NZ7ID/btqApkQ2thJ/Z20lrtLzzq3eKsIAc8v4SK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 프로파일을 설정하지 않고 그냥 실행하면 빈이 등록되지 않았으므로 빈을 찾지 못한다는 에러가 발생한다.

#### Profile 설정하기

[##_ImageGrid|kage@lXop3/btqAowYIvjx/SC4h0F5kzSyvvrVtiPHHKk/img.png,kage@bg0Cnm/btqAq3ns9lY/rI9SmOh8pPgiZ25fGEoKb1/img.png|data-origin-width="0" data-origin-height="0" style="width: 47.7018%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 51.1354%;"|_##]

- IDE 우측상단에 왼쪽 사진처럼 표시되어있는 곳을 클릭해 EditConfigurations에 들어가 profile을 test로 설정하였다.

[##_Image|kage@T8sxK/btqAnwET3va/KoK6dvZurD5A01ZCKRuseK/img.png|floatLeft|data-origin-width="0" data-origin-height="0"|||_##]

- 정상적으로 앱이 구동되고 ActiveProfiles에 test가 출력된다.

[##_Image|kage@c6jtBJ/btqArhluIdx/UUTcncWsH9jAbBq8ehhEj0/img.png|floatLeft|data-origin-width="0" data-origin-height="0"|||_##]

- Configuration을 따로 만들지 않고 Repository에 직접 프로파일을 설정할 수도 있다.
- 주석으로 처리된 @Profile과 같이 !를 붙이면 test가 아닌 모든 곳에 적용한다는 뜻이다.

---

### Property
- 다양한 방법으로 **설정값을** 정의할 수 있다.
- **Environment의 역할은** Property Source 설정 및 Property 값을 가져오는 것이다.

#### Property 우선순위
- 1\. ServletConfig 매개변수
- 2\. ServletContext 매개변수
- 3\. JNDI(java:comp/env/)
- 4\. JVM 시스템 프로퍼티(-Dkey = "value")
- 5\. JVM 시스템 환경 변수(운영 체제 환경 변수)

#### Property 사용해보기

[##_Image|kage@tjGQi/btqAnm3zPZf/FjcNARfpOr6knTLkSXM831/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_ImageGrid|kage@HBKZX/btqAnYuqCKv/lRvxj8TJVNX9UEYfD8fFKk/img.png,kage@mStjQ/btqAowRYdnI/UaxBKnwCyEk83aIRvCWSQ1/img.png|width="765" height="108" data-origin-width="0" data-origin-height="0" style="width: 58.6516%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 40.1856%;"|_##]

- Vm options에 -Dapp.name=test1라고 설정 한다
- getProperty("app.name")을 하면 test 1이 출력된다.

[##_ImageGrid|kage@VErda/btqApH6jq5S/O0GBDuJODgba3IieVpCvD0/img.png,kage@NrwYN/btqAovlewxw/oKCKSZOrXTqTFZeFYB0kQk/img.png|data-origin-width="0" data-origin-height="0" style="width: 37.3095%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 61.5277%;"|_##][##_Image|kage@dNrLHv/btqAnwLF9mJ/f5Fw8d1jLf4u2fnqOLRV41/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 다른 방법으로는 resouces 폴더에 app.properties라는 파일을 만들고 위와 같이 입력한다.
- Configuration이 존재하는 클래스에 @PropertySource를 통해 추가가 가능하다.

[##_Image|kage@dcxJwI/btqArjcBt7T/NdSzjE1lp7Frn60mst8ETk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@buN7XZ/btqAqgm8FJf/L3LZIS0ZZmY2U6bUDJxPlk/img.png|floatLeft|data-origin-width="0" data-origin-height="0"|||_##]

- 결과를 확인해보면 app.about property도 존재하는것을 확인할 수 있다. 

---

[참고 자료](https://www.inflearn.com/course/spring-framework_core)
