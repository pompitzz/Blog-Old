# AOP(Aspect-Oriented Programming)
> OOP를 보완하는 수단으로, 흩어진 Aspect를 모듈화 할 수 있는 프로그래밍 기법이다.


[##_ImageGrid|kage@OcTmq/btqAwkYdtRU/OBA2tm4mJIBE95i1qdQgb1/img.png,kage@bPvh6x/btqAwjyc8VB/27TzrhaLUBihkBm5IVrdZ1/img.png|data-lazy-src="" data-width="611" data-height="273" data-origin-width="0" data-origin-height="0" style="width: 57.1972%; margin-right: 10px;",data-lazy-src="" data-width="655" data-height="402" data-origin-width="0" data-origin-height="0" style="width: 41.64%;"|_##]

- 왼쪽 그림의 A, B, C Class에서 같은 색의 띠가 공통되는 로직이라고 생각해보자.
- 만약 노란색 띠의 로직의 문제가 생긴다면 A, B, C Class 제각각 수정을 해야한다.
- 공통되는 로직들을 오른쪽 그림처럼 한 곳에 모아 모듈로 간편하게 만들어 사용하는 것을 AOP라고 한다.
- 보안, 로깅, 트랜잭션 등에 주로 사용하는 것으로 알고 있다.

### AOP의 주요 개념
Aspect
- Advice와 PointCut을 합친 개념이다.

Advice
- 해야 할 일들을 담고 있는 모듈

Target
- 적용이 되는 대상

PointCut
- 어드바이스를 어디에 적용을 해야 하는지에 대한 정보

JoinPoint
- 어드바이스가 적용될 수 있는 위치(생성자 호출 직전, 호출 후 등등)

Weaving
- 포인트 컷에 의해 결정된 타깃의 조인 포인트에 어드바이스를 삽입하는 과정​

### JAVA 구현체
AspectJ
- **컴파일, 로드 타임 시점** 에서 사용

스프링 AOP
- **런타임 시점** 에서 사용

### AOP 적용 방법
#### 컴파일 시점
- 자바 파일을 클래스 파일로 만들 때 AOP를 적용하는 방법
- 장점: 로드, 런타임에 성능 부하가 없음
- 단점: 별도의 컴파일 과정 필요

#### 로드 타임 시점
- 컴파일 후 클래스 파일을 로딩하는 시점에 AOP를 적용하는 방법
- 장정: 다양한 문법을 사용할 수 있다.
- 단점: 클래스 로딩 시 약간의 부하 생길 수 있음, 로드 타임 위버를 설정해야 한다.

#### 런타임 시점
- 클래스를 읽어 온 후 빈을 생성할 때 AOP를 적용하는 방법
- 장점: 별도의 컴파일, 로드 타임 위버가 필요 없고 문법이 간단하다.
- 단점: 빈을 만드는 초기에 성능이 추가되어 비용이 든다. 다양한 조인 포인트가 필요할 때 불리

​
> 런타임 시점에서 사용되는 스프링 AOP에 대해 알아보도록 한다.

---

### 프록시 패턴 이해하기
> 스프링 AOP는 프록시 패턴 기반으로 만들어 졌으므로 프록시 패턴에 대해 아는게 유리하다.

#### 프록시 패턴
- 다른 객체를 대변하는 객체를 만들어서 주 객체에 대한 접근을 제어할 수 있다

#### 예제

[##_Image|kage@kKgAW/btqAwjydC1o/FHUMXMCYMTyACAeyT6cKDK/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]
- ItemService를 Interface로 만들었다.
- 실제 사용할 클래스와 프록시 클래스는 이 인터페이스를 상속하여 구현한다.

<br>

[##_Image|kage@LwMTA/btqAxgugDtY/PxTEP35HvwB7cLvbVrqRQK/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]
- ItemService를 구현하여 메서드 호출 시 출력을 하게 작성하였다.
- 메서드들은 Tread.sleep으로 시간차를 두고 출력을 하게 만들었다.
- **@Service를 통해 빈으로 등록하였다.**

<br>

[##_Image|kage@v2iWr/btqAyHEETxd/lkJfdKKCyspKT2lOO5zIi0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]
- Proxy용 클래스를 하나 만들고 ItemService를 구현한다.
- 프록시는 빈으로 등록한 ItemServiceImpl를 가지고 있다.
- 각 메서드에서 시간 차이를 계산하는 로직사이에 각 로직에 맞는 itemServiceImpl의 메서드들을 선언한다.
- 그리고 AppRunner에서 ItemService를 받아올 때 Proxy 클래스를 받아올 수 있도록 **@Primary를 붙여 우선순위를 부여한다.**

[##_ImageGrid|kage@bfk7hN/btqAy2u2z3N/SC9Wj1UNRAcqJew9Tp9880/img.png,kage@OuA6N/btqAv0FGNbd/N5XQMkvJhhKtV9kuTRCnCK/img.png|data-origin-width="0" data-origin-height="0" style="width: 65.519%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 33.3182%;"|_##]

- AppRunner에서 결과를 확인해보면 정상적으로 프록시 객체가 실행되어 ItemServiceImpl의 동작을 제어한 것을 알 수 있다.
- 하나 이렇게 사용하면 메서드마다 중복 코드가 발생하고 새로운 프록시 객체를 만들어야 하는 불편함이 있다.

> 스프링 AOP를 이용하면 시간을 계산하는 로직을 하나만 작성하고 원하는 곳에 설정할 수 있다.

---

### 스프링 AOP
> 스프링 IoC 컨테이너가 제공하는 기반 시설과 Dynamic 프록시를 사용하여 여러 복잡한 문제를 해결할 수 있다.

동적 프록시
- 동적으로 프록시 객체를 생성한다.

스프링 IoC
- 기존 빈을 대체하는 **동적 프록시 빈을 만들어 등록** 시켜준다.

#### 스프링 AOP 특징
- 프록시 기반의 AOP 구현체, **스프링 빈에만 AOP를 적용** 할 수 있다.
- 모든 AOP 기능을 제공하는 것이 아닌 스프링 IoC와 연동하여 엔터프라이즈 애플리케이션에서 가장 흔한 문제에 대한 해결책을 제공하는 것이 목적이다.

[##_Image|kage@IwLAb/btqAwJpY1pC/j5hKDqJDKb0mzYPZQqXmU0/img.png|floatLeft|data-origin-width="0" data-origin-height="0"|||_##]

- 우선 스프링 aop를 사용하기 위해서 pom.xml에 의존성을 추가한다.

[##_ImageGrid|kage@dMwEDE/btqAxOK3Q8G/mpkqq3uNxKajFrxcQ3NZS0/img.png,kage@pjHJY/btqAuMumHgi/kEWz67UeO9kqGfqHAwZsX1/img.png|data-origin-width="0" data-origin-height="0" style="width: 73.6079%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 25.2294%;"|_##]

- 프록시 패턴 예제에서 생성한 ProxyItemService는 삭제한다.
- @Aspect를 이용하여 스프링 AOP를 적용시킬 수 있다.
- @Aspect가 붙어있는 logPerf메서드가 해야 할 일들을 담고있는 Adivce가된다.
- @Around에 PointCut을 정해 타겟들을 설정할 수 있다.
- 간단히 설명하자면 우선 첫번 째 \*는 리턴타입으로 모든 타입을 리턴을 허용한다는 뜻이다.
- 그리고 클래스를 패키지명을 포함하여 작성한다.
- 그 후 .\*은 모든 지정한 클래스의 모든 메서드들에 대해 적용하라는 뜻이다.
- 마지막 괄호는 파라미터에 대한 설정인데 (..)란 0개 이상을 의미한다.
- 즉 ItemService의 모든 메서드에서 리턴값은 상관이 없고 0개 이상의 파라미터를 가진 대상에게 적용한다는 뜻이다.
- 실행결과를 확인해보면 정상적으로 동작하는 것을 알 수 있다.

#### Annotaiton으로 PointCut 적용하기
- 만약 특정 메서드에만 적용하고 싶다면 어떻게 해야할까?
- 표현식을 좀 더 정교하게 써서 할 수도 있을 것이다.
- 하지만 간단하게 annotaion을 만들어 그 annotaiton에만 AOP를 적용할 수도 있다.

[##_Image|kage@bEkcL9/btqAvjS0SZO/LON96oy9kruec6bDaJj3J0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]
- PerLogging Annotation이 붙은 메서드에만 Advice를 적용한다고 할 수도 있다.
- 그리고 아래와 같이 createItem에만 @PerLogging을 붙인다.

[##_Image|kage@tzvBW/btqAuLIXgAW/xNq3GGgTi01CUA7tHd8ks0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_ImageGrid|kage@dk0xfr/btqAvjla6eH/EwsNgr7hZ4jBTZGoCNduU1/img.png,kage@cd9YXy/btqAyGZ6j3k/Oxgr9nvvoUEwzmc7HKkvk1/img.png|data-origin-width="0" data-origin-height="0" style="width: 60.2062%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 38.631%;"|_##]
- PerLogging annotaion를 만들고 실행시켜보면 PerLogging이 붙어있는 create Item만 AOP가 적용된 것을 알 수 있다.

---

[참고 자료](https://www.inflearn.com/course/spring-framework_core)
