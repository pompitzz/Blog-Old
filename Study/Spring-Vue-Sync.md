> Vue.js 프로젝트 빌드 시 스프링부트 프로젝트 resouces/static 이하에 빌드되게 설정하는 게시글

### SpringBoot 프로젝트 생성
[스프링 부트 스타터](https://start.spring.io/)
- 해당 링크를 들어가면 스프링 부트 프로젝트를 생성할 수 있다.

[##_Image|kage@2vqpV/btqAz7pU9Gf/EXaRX4FuUU8d07VxIW5sa0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- Spring Initializr를 이용하여 위와 같이 Spring Web만 의존성 추가한 후 Generate를 클릭하여 프로젝트 파일을 다운로드한다.

<br>

[##_Image|kage@cGqyFl/btqAAtGhQuX/0ck40IPkgnti87JM1Pt7n1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 다운로드한 프로젝트 파일을 인텔리 J로 실행시킨 후 서버를 띄워 프로젝트가 정상 동작하는지 확인한다.

<br>

[##_Image|kage@VUJCh/btqAy056tSd/Z42BIOIawkF3m61ZWkyKv0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 앱이 정상적으로 구동되는 것을 확인하였다. 그럼 이제 Vue프로젝트를 다운로드한다.

---

### Vue 프로젝트 생성

#### [Node.js 공식사이트](https://nodejs.org/ko/)

- 우선 Vue 프로젝트를 생성하기 위해선 Vue CLI가 필요하고 Vue CLI를 다운로드하기 위해 Node.js를 설치하여야 한다.
- Node.js 공식사이트에 들어가 Node.js를 설치하고 아래와 같이 node, npm 버전을 보고 정상적으로 다운로드가 되었는지 확인한다.

[##_Image|kage@cjqRBW/btqAz7Drj1R/dv93i4H09AWNPfsctA8jf0/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@VxSyf/btqAzxij0TR/kUbtBKGUvmnliWMNjl8KU0/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]
- 그 후 npm을 통해 Vue CLI를 다운로드한다.

<br>
[##_Image|kage@dxVlVk/btqAyyI39jc/cVQOVbwqYBh19kWxtDnzDK/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]

- 다운로드 후 버전을 확인하면 4.1.1 버전이 다운로드된 것을 확인할 수 있다.

[##_Image|kage@djh1jT/btqAyFOHpoN/Huxgn5lwgHs8hLDthoprlK/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]

- 스프링 부트 프로젝트가 들어있는 폴더와 동일한 경로에 Vue 프로젝트를 생성한다.
- vue create '프로젝트 이름'으로 프로젝트를 생성하면 아래와 같이 vue-project가 생성된 것 을 알 수 있다.
- 그리고 create는 default로 하면 된다.

[##_Image|kage@T1Cfk/btqAyxDmYPA/BLIrCmECmFAbA8sOq8WmOk/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]

### **Vue Build 경로를 SpringBoot 폴더로 변경하기**

[##_Image|kage@vCYch/btqAzyO5G6t/AGv4KG3waXYZJFkuHHY9MK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- vue 프로젝트 폴더로 가서 npm run serve를 통해 vue 프로젝트가 정상 동작하는지 확인한다.
- 확인 후 npm run build로 vue를 빌드하면 vue 프로젝트 dist 폴더에 빌드 파일이 생성될 것이다.
- 스프링부트 resources폴더에 빌드된 파일을 저장히기 위해선 아래와 같이 설정을 해주어야 한다. 

[##_Image|kage@qKZMk/btqAyHMudKO/P7KhRhogfysR5X5Er3lX0k/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]
- 프로젝트 최상위 폴더에 vue.config.js를 생성하고 위와같이 작성한다.
- outputDir로 build시 설치될 경로를 지정하고 indexPath로 index.html이 저장될 폴더를 지정한다.


[##_Image|kage@lDDY9/btqAxOZCqKl/RwC5mbSV4bmzagMhjkNuNk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@cbaSP0/btqAAtzwL9W/RIO21kuyO5fywavI2ajop1/img.png|floatLeft|data-origin-width="0" data-origin-height="0"|||_##]

- 설정 후 npm run build를 하면 스프링 부트 프로젝트 static이하에 빌드된것을 알 수 있다.

[##_Image|kage@d6Qexg/btqAz3aedk6/qYTbVkGK0jrxpOaButEDQ0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

[##_Image|kage@vliOu/btqAy1Rymzg/FgjQaa3CqDd6NDlt0kS3B0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 스프링 부트 애플리케이션을 실행하고 서버 포트번호를 확인 후 접속해보면 정상적으로 Vue 프로젝트가 동작된다.
