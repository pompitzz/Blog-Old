# Component, Component Scan
> 빈으로 등록할 수 있는 Component와 그 종류들에대해 살펴보고 사용한 Component들을 스캔하여 빈으로 등록할지 결정하는 Component Scan에 대해 알아본다.

### **Component**
- @Component Annotation을 붙이면 그 객체는 빈으로 등록이 된다.
- 계층이나 상황에 맞게 명시적으로 붙일 수 있도록 추가로 4가지의 종류가 존재한다.
- 이는 @Component를 Meta Annotation으로 가지고 있어 똑같은 기능을 한다.

@Repository
- Rersistence Layer에서 빈으로 등록할 때 주로 사용하는 Annotation

@Service
- Business Layer에서 빈으로 등록할 때 주로 사용하는 Annotation

@Controller
- Presentation Layer에서 빈으로 등록할 때 주로 사용하는 Annotation

@Configuration
- Configuration을 하는 곳을 빈으로 등록할 때 주로 사용하는 Annotation


> 실제 스캐닝은 ConfigurationClassPostProcessor라는**BeanFactoryPostProcessor에**의해 처리된다.
> BeanPostProcessor와다른 것이며 다른 모든 Bean들을 등록하기 전에 동작하는 Interface이다.

### **Component Scan의 기능**

[##_Image|kage@cTa2GS/btqApkbDTb0/fQaHdf3SpVjrocPzxJoN9k/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@dmlGta/btqAovLuBBy/5OFIyg4YkbzsTntWBS0Ye0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@cBAFx0/btqAo6rcz9g/I07u0eQ88I3gK15JlfISQK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- @SpringBootApplicaiton -> @ComponentScan을 보면 많은 Filter들이 있는데 이 **Filter를** 통해 빈등록이 필요없는 것들을 제외시킬 수 있다
- 그리고 @ComponentScan에 들어가보면 **"basePackages"를** 찾을 수 있다.
- 이것을 통해 어느 패키지 부터 스캔을 할지 결정하게 된다.



### **Functional 한 Bean 등록**
- Annotation을 이용하여 bean을 삽입하면 초기에 생성하여 많은 빈을 삽입할 땐 초기 구동시간이 오래 걸릴 수 있다.
- 그러하여 Spring 5부터 가능한 Function을 사용하여 빈을 등록할 수 있다.

[##_Image|kage@Vj0MA/btqAnxbVjPO/meF7hXNiVNq4Y6llgyOaXK/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]

- 구동시간 때문에 Function을 이용하면 위와 같이 많은 설정 파일이 필요하다.
- Component가 아닌 직접 @Bean을 이용할 때 사용하면 좋다.

---

[참고 자료](https://www.inflearn.com/course/spring-framework_core#)
