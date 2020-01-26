# @AutoWired
> 등록된 빈을 주입하여 사용하고 싶을 떄 사용하는 Annotation이다.

- **필요 한 의존 객체의 "타입"에 해당하는 빈을 찾아 주입한다.**
- required의 기본값은 true이므로 빈을 찾지 못하면 App 구동 실패  
- 생성자, 세터, 필드에서 사용할 수 있다. (생성자는 스프링 4.3부터 생략 가능)

### 1\. 해당 타입의 빈이  한 개인 경우 

[##_Image|kage@bXdSDu/btqAm3PcLmA/QYxDnkNTLnJcizt7ePkKx1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@Ipsi9/btqAms2LjXD/pTOWRSckPDUkFknUTDkBfK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- @Service와 @Repository를 통해 빈으로 등록하였으므로 정상적으로 빈을 찾아 주입한다.

#### **@Repository를 제거했을** **때**

[##_Image|kage@bUz40d/btqAnv5yI07/bLPYq5yk2gx3qypCwGxJJk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@bZtj7o/btqAnnfAGzo/fhBH1StpsuuYT03HKocaA0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- Repository에 Annotation을 주지 않고 생성자를 통해 빈을 주입하려고 하면 에러가 발생한다.  

> 생성자는 생성 시 userRepository의 Bean을 찾지 못해 에러가 뜨는 건 확실하나 Setter는 생성시에는 아무런 영향을 미치지 않는데 에러가 나타난다.

- 그 이유는 **@Autowired가 의존성 주입을 시도** 하기 때문이다.
- 그러므로 @Autowired를 제거하면 아래와 같이 생성자는 그대로 에러가 뜨지만 Setter는 에러가 발생하지 않는다.

[##_Image|kage@m1LLN/btqAlP5ficp/xu5x0ErpUjpYmkSV5wsrK0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

<br>

[##_Image|kage@4LPpC/btqAn0c829k/MDv3woIV0DlB4V4WCLFTt0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]
- 의존성이 옵션일 때는 @Autowired(required = false) 옵션을 주면 된다.
- required = false일 경우 의존성 주입을 시도하지만 빈으로 등록되지 않을 경우는 하지 않는다.


### 2\. 해당 타입의 빈이 여러 개인 경우
- UserRepository를 인터페이스로 바꾸고 A, B Repository를 만든 후 UserRepository를 상속받는다.


#### 방법 1
[##_Image|kage@B04NE/btqAnwcjdQi/K3JKKlBTURRlpyayUwDk80/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@wJ4gr/btqAkTtbkuP/ioMCirmXJPiFWxwBDTtrP1/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]

> 하나의 Repository에 @Primary를 하여야 한다.

####  방법 2


[##_Image|kage@c8zNyz/btqAlYt4dzF/oUwAlzWBmQTASR9LV81wu0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- AUserRepository를 사용하고 싶으면 이렇게 @Qualifier에 "AUserRepository"를 명시하면 된다.

#### 방법 3
- 컬렉션을 통해 모든 Repository를 받아올 수도 있다.

[##_Image|kage@vchs5/btqAkS18ZXX/ttIYWAqkihGXVeFATOf8QK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@c5Dapn/btqAlZNhw7a/kuyuPSfSt0KRbsih8NRti0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@2YnQt/btqAnZ6nUZv/76wNpbe05ndM8uqLGW4wvk/img.png|floatLeft|data-origin-width="0" data-origin-height="0"|||_##]

- AppRunner를 통해 빈들을 확인하면 A, B Repository가 List에 들어가 있는 것을 알 수 있다.  
​  

#### 방법 4
- Repository의 인스턴스를 만들 때 원하는 Repository로 만드는 방법
- 변수명을 빈으로 등록하기 원하는 클래스에 맞게 설정하면 된다.

[##_Image|kage@CHfVd/btqAmsVYWhl/maS1BiZYqY1awCX7GlnjF0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@wrtPp/btqAm48pQmH/6OJ4vGo2W80Xngm2BIsnYk/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]

- 이 방식이 가능한 것은 BeanPostProcessor Interface 때문이다. 
- 아래의 lifecycle을 보면 12번 InitializingBean\`s 전에 11번의 BeanPostProcessors에서 찾아준 후에 12번에서 빈이 Initializing 되기 때문이다.

[##_Image|kage@csPb50/btqAlQbZWFC/XcTAyRaBuqpJ1WqXWK3ATk/img.png|alignCenter|data-lazy-src="" data-width="693" data-height="114" data-origin-width="0" data-origin-height="0"|BeanFactory의 lifecycle||_##]

[##_Image|kage@kOsS2/btqAnxvwKAM/DXPjG534570w4Zuw6sp8n1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]


- 이런 식으로  @PostConstruct를 사용하면 12번에서의 동작을 확인할 수 있다.
- 아래의 출력을 보면 빈이 정상적으로 등록되었고 앱 빌드 후가 아닌 중간에 출력이 되는 것을 알 수 있다.

[##_Image|kage@bURBy9/btqAlX9KdpO/pviwt82CLAfg5bRVmBBSnk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

---

[참고 자료](https://www.inflearn.com/course/spring-framework_core#)
