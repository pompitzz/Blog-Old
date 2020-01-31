> Vue.js로 만든 페이지를 배포하려면 대표적으로 GitHub이나 Netlify에서 할 수 있다.

Netlify는 저장소만 설정해준다면 Git으로 Push할시 자동으로 빌드를 해주지만 Github페이지에 배포하려면 간단하게 스크립트를 작성하면 된다.

### **저장소 만들기**

[##_Image|kage@dhcHxw/btqBdUKgp3O/HTHH1PWdVqry59k2Hfjdp1/img.png|floatLeft|data-origin-width="0" data-origin-height="0"|||_##]

우선 Vue.js 프로젝트를 하나 만든다.

[##_Image|kage@1gjWc/btqA7qD5Rww/Th57ktY3mZ04txAfPTSZw0/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@oURKJ/btqBcM6YOWc/rJhf7lkvzgukHkkrFIK7H1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

만든 Vue.js 프로젝트에 remote 설정을 하고 저장소와 연결되었는지 확인해본다.  기본적으로 vue project를 만들 때 따로 설정을 하지 않으면 git이 세팅되어 있으므로 remote 설정 후 바로 push 하면 된다.

[##_Image|kage@bxwEzw/btqBdS6N6nM/ikDR6Hg1L6tiQuBY1TD12K/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

**정상적으로 저장소에 올라간 것을 확인할 수 있다.**

### **Netlify로 배포하기**

**[https://cli.vuejs.org/guide/deployment.html#netlify](https://cli.vuejs.org/guide/deployment.html#netlify)**

[##_Image|kage@TcNLV/btqA8jxXgiv/ukrySNoqSMgy1UkJ0maVd0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

Netlify에 배포 시 위의 링크와 같이 public 하위에 \_redirects를 만들어 설정을 해줘야 라우팅 시 문제가 생기지 않는다.

[https://www.netlify.com/](https://www.netlify.com/) 우선 Netlify로 접속하여 회원가입 후 로그인을 한다.

[##_ImageGrid|kage@ewdKti/btqBdVbjMze/SxyFKxOVabXuAqKdmEcUrK/img.png,kage@cIbjjv/btqBezFQV15/YzBx8YZbikhWjpB3gSfGT1/img.png|data-origin-width="0" data-origin-height="0" style="width: 44.412%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 54.4252%;"|_##]

아래의 New site from Git에 들어가 GitHub을 설정해주고 Netlify에 Git 저장소를 등록해준다.

[##_Image|kage@c63mFE/btqA9A0xkEN/3qK3kvHZokglMZ207ZJXvK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

그리고 등록한 저장소 설정으로 들어가서 아래와 같이 Build command에 빌드 명령어를 써주고 기본 빌드 directory 위치가 dist이므로 Publish directory를 dist로 설정한다. 그 후 버튼을 눌러 deploy 해준다.

[##_Image|kage@bjp4b6/btqA9BrGwcw/BLqscgGwKVYyltlrIznFi0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

배포가 완료된 후 개인 페이지로 가서 확인해보면 아래와 같이 배포가 완료되었다고 한다. 그 주소로 들어가 보면  정상적으로 배포된 것을 확인할 수 있다.

[##_Image|kage@bjUaez/btqBaBET1rD/K1CpyMgtv5mT9ZnMZw1Wb1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

### **GitHub에 배포해보기**

```
#!/usr/bin/env bash

set -e # 도중에 오류 발생시 스크립트를 종료한다.

echo ">> 빌드를 시작합니다 <<"
npm run build

echo ">> 빌드 Directory로 이동 <<"
cd dist

echo ">> git init <<"
git init
git add -A
git commit -m "Deploy"

echo ">> git push <<"
git push -f https://github.com/DongmyeongLee22/vue-deploy.git master:gh-pages

cd ..
```

GitHub에 배포하기 위해서는 빌드된 파일을 저장소에 올려야 한다. 그러므로 위와 같이 스크립트 파일을 만들어 프로젝트 폴더에 저장해준다.

빌드된 directory에서 git을 추가하고 deploy-pages branch로 강제로 푸시하였다.

[##_Image|kage@cAfSmi/btqBc6jXjir/Kwk3JRLKl768MJeKxYSxx0/img.png|floatLeft|data-origin-width="0" data-origin-height="0"|||_##]

그리고 프로젝트 폴더에 vue.config.js를 만들어 publiPath를 원격저장소 이름으로 지정해준다. 왜냐하면 GithubPage는 URL주소가 [github.io/vue-deploy/](https://dongmyeonglee22.github.io/vue-deploy/) 이런식으로 지정되기 때문이다.

[##_Image|kage@VRq4O/btqA7q5cJgo/3KZa286qVoF6Q6IX9qixE1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

터미널에서 스크립트를 실행하고 GitHub Setting에 들어가 아래와 같이 GitHub Pages를 gh-apges로 설정해준 후 배포된 주소로 들어가 보면 정상적으로 배포된 것을 알 수 있다.

[##_Image|kage@mqDoU/btqA9BZsPVl/VHmQD1KKJanSYH1susKjQ1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]
