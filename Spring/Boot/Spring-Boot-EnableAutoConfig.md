# @SpringBootApplicaiton
> SpringBootApplication Annotation의 Meta Annotation인 @ComponentScan과 @EnableAutoConfiguration에 대해 알아 본다.

[##_Image|kage@bpyrrz/btqABB5xJmd/FGtUZgBKkEvKp1xDihBli1/img.png|widthContent|data-lazy-src="" data-width="693" data-height="54" data-origin-width="0" data-origin-height="0"|||_##]
- 스프링 부트로 프로젝트를 생성하여 메인 클래스에 가면 @SpringBootApplication annotation이 있다.
- 이 annotation을 확인해보면 아래와 같이 @SpringBootConfiguration, @EnableAutoConfiguration, @ComponentScan이 존재한다.

<br>

[##_Image|kage@cbDrxe/btqABRAjB9G/U5MZo2qk8XufIMgFmaZnpK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="199" data-origin-width="0" data-origin-height="0"|||_##]

#### @ComponentScan
- ComponentScan에서는 지정된 타켓 패키지 이하를 스캔하며 @Component Annotation이 붙어 있는 대상들을 빈을 등록해준다.
- @Controller, @Repository, @Service, @Configuration 모두 @Component를 MetaAnnotaion으로 가지고 있으므로 이들도 해당된다.
- @ComponentScan을 보면 Filter가 포함되어 있는데 Filter로 설정된 대상은 빈으로 등록하지 않게 된다.

#### @EnableAutoConfiguration
- SpringBoot의 핵심 기능중 하나인 자동설정에 맞는 대상을 빈으로 등록시켜준다.
- 의존성을 설정에 맞게 EnableAutoConfiguration에서 기본적인 자동설정을 지원해준다.
- 만약 현재 spring-boot-starter-web 의존성을 추가하였다면 EnableAutoConfiguration가 web 구동에 필요한 것들을 빈으로 등록해주기 때문에 프로젝트를 생성즉 시 웹 애플리케이션으로 구동할 수 있는 것이다.

#### @EnableAutoConfiguration가 없을 때

[##_Image|kage@oOfyQ/btqAz8iZrMf/fMvdLoAJjUJAMxsGvhhT2K/img.png|widthContent|data-lazy-src="" data-width="693" data-height="218" data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@uhmdJ/btqABSssUKE/zz3xCgYobKazGr4htsoMUK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="32" data-origin-width="0" data-origin-height="0"|||_##]
- 우선 @SpringBootApplicaiton을 Annotation에서 제거하고 @Configuration, @ComponentScan만 붙인 후 앱을 실행한다.
- 그러면 ServletWebserverFactory Bean이 missing이라는 에러가 나타날 것이다.
- 즉 EnableAutoConfiguration이 우리가 추가한 의존성을 보고 ServletWebserverFactory을 빈으로 등록해주었기 때문에 웹서버로 구동시킬 수 있었다.

<br>

[##_Image|kage@lLQ4x/btqAz24kqZL/1fxZwDpFxEtkSVuq0CSoFK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="180" data-origin-width="0" data-origin-height="0"|||_##]
- WebApplicationType을 NONE으로 설정하면 웹서버로 동작하지 않으므로 정상적으로 구동 될 것이다.

---

### @EnableAutoConfiguration

[##_Image|kage@ES1tl/btqAzyoQFUz/HO9DyGzpcWtGufNk5yHGX0/img.png|floatLeft|data-lazy-src="" data-width="693" data-height="338" data-origin-width="0" data-origin-height="0" width="442" height="216"|||_##]
- External Libraries에서 spring-boot-autoconfigure를 찾을 수 있다.
- 해당 파일의 spring.factories를 확인해보면 아래와 같이 수많은 AutoConfiguration 목록들이 나열되어 있다.
- @EnableAutoConfiguration은 이 목록들을 보고 해당 프로젝트가 설정한 조건에 맞게 알맞은 빈을 등록해주게 된다.
- 그렇기 때문에 Spring Boot로 프로젝트를 생성하면 기본적인 추가 설정이 필요 없게 된다.

[##_Image|kage@bVfZsu/btqAy1dLAef/rZ21qNYrQjD7JwjDkya3R0/img.png|widthContent|data-lazy-src="" data-width="693" data-height="177" data-origin-width="0" data-origin-height="0"|||_##]

---

[참고 자료](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8)
