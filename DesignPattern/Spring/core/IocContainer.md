# Spring IoC Container
> 스프링 IoC 컨테이너와 번 설정 방법들에 대해서 알아본다.


### 컨테이너
- 적절한 설정만 되어있다면 누구의 도움없이도 프로그래머가 작성한 코드를 스스로 참조한 뒤 알아서 객체의 생성과 소멸을 컨트롤 해준다.

### IoC(Inversion of Conrol) 제어의 역전
- 어떤 객체가 사용하는 **의존 객체를 직접 만들어 사용하는게 아닌, 주입받아 사용하는 방법**
- 스프링 프레임워크에서는 인스턴스의 생성부터 소멸까지 인스턴스의 생명주기를 개발자가 아닌 IoC 컨테이너가 대신해준다.

### 스프링 IoC 컨테이너
- BeanFactory Interface(가장 최상위에 있는 인터페이스)
- 애플리케이션 컴포넌트의 중앙 저장소.
- **빈 설정 소스로부터 빈 정의를 읽고, 빈을 구성하고 제공한다.**

---

### 빈(Bean)
- 스프링 IoC 컨테이너가 관리하는 객체
- 의존성 주입을 하기 위해서는 **인스턴스를 빈으로 등록하여야 한다.**

### IoC 컨테이너 빈의 장점
- 의존성 관리를 IoC 컨테이너가 하므로 비즈니스 로직에만 신경을 쓰면 된다.
- 객체의 생성과 소멸등 생명주기를 관리 해주므로 메모리를 효율적으로 사용할 수있다.
- 라이프사이클 인터페이스를 이용하여 원하는 작업을 할 수 있다.

### ApplicationContext
- BeanFactory를 상속받아 그 기능을 가지고 있으면서 추가적으로 다양한 기능들을 가지고 있는 인터페이스로 많이 사용된다.

---

## Application Context와 다양한 빈 설정 방법

### 스프링 IoC 컨테이너의 역할
- 빈 인스턴스 생성
- 의존 관계 설정
- 빈 제공

### 빈 설정 방법
#### 1) xml에 직접 등록

[##_Image|kage@QNFYN/btqAnvRWHrv/5A9v81ijsfuWmqkXpSf3m0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 빈 설정을 위해 UserRepository와 UserService를 만든다.
- resources 아래에 bean설정 xml을 만들고 아래와 같이 작성한다. 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="userService"
          class="me.sun.spring.UserService">
        <property name="userRepository" ref="userRepository"/>
    </bean>

    <bean id="userRepository"
          class="me.sun.spring.UserRepository"/>

</beans>
```
- xml을 통헤 빈을 주입할 수 있다.
- property tag에 값을 설정하면 된다.
- name은 Class의 bookRepository이고 ref는 xml에 설정한 bean의 id이다.

[##_Image|kage@bxW33c/btqAjVLOORm/idTz7qmj5ifSXuPqftyW61/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 위의 결과를 true가 출력될 것이다.

### 2) xml에 컴포넌트 스캔 등록
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="me.sun.spring"/>
</beans>
```

[##_Image|kage@edXAS8/btqAkSntu3E/IrxKnmU7TNb3HhWXaZb871/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@bqFqAR/btqAlPYnX3P/FtSi6QlZii0P7rhb4gumh1/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]

- xml에 컴포넌트 스캔을 설정한 후 각 Service와 Repository에 Annotation으로 빈을 등록할 수 있다.
- 컴포넌트 스캔에 등록된 package이하의 모든 클래스에 적용이 가능하다.
- @Autowried 혹은 생성자 등 다양한 방법으로 등록된 빈을 주입할 수 있다.

### 3) 클래스로 등록

[##_Image|kage@mPLx8/btqAkwE1OP6/T3AWi68DHdoppNJw3ueVSk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@bitz5O/btqAnoZSAse/ppO9Xw36eVawZs3ZNrOAi0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]
- 위에서 설정한 xml 설정을 제거한다.
- 그리고 ApplicationConfig를 만들어 빈으로 등록하고 싶은 객체를 @Bean통해 설정한다.
- 그 후 ApplicationContext를 통해 빈으로 등록이 가능하다.

### 4) 클래스단에 컴포넌트 스캔 등록

[##_Image|kage@mC8pP/btqAnZrKZxZ/9HrpPfjEE9ulQsefLSjMtK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 이렇게 컴포넌트 스캔을 등록하면 MySpringApplication.class이하의 모든 클래스들을 스캔하여 빈으로 등록을 한다.
- 단 이렇게 사용하기 위해서는 2)와 같이 빈으로 등록하고 싶은 곳에 @Component 부류의 Annotation을 붙여줘야 한다.

### 5) 스프링부트를 사용

[##_Image|kage@bAKpyU/btqAm32Ln6C/i67qZaYBkWeFlgSNRdihIK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@I1Nm9/btqAnYfjqQy/SdUxyuhzFFNmmFgojLhAfk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 스프링 부트를 사용하면 따로 컴포넌트 스캔을 사용할 필요가 없다.
- 왜냐하면 @SpringBootApplication의 Annotation을 확인해보면 이미 @COmponentScan이 등록되어 있다.

---

[참고 자료](https://www.inflearn.com/course/spring-framework_core)
