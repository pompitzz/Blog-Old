# 데코레이터 패턴
- 주어진 상황 및 용도에 따라 어떤 객체에 책임을 덧붙이는 패턴
- 객체에 추가적인 요건을 동적으로 첨가하며, 기능 확장이 필요할 때 서브 클래싱 대신 쓸 수 있는 유연한 대안이 될 수 있다.  


[##_Image|kage@exqO8t/btqAmt2l8MC/b6PrWbB5HTzMoFnflV6Vw1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

#### Component
- 실질적인 인스턴스를 컨트롤하는 역할

#### ConcreteComponent
- Component의 실질적인 인스턴스의 부분이며 책임의 주체의 역할

#### Decorator
- Component와 ConcreteDecorator를 동일시하도록 해주는 역할

#### ConcreteDecorator
- 실질적인 장식 인스턴스 및 정의이며 추가된 책임의 주체 부분  


---

### 예제   
커피 주문을 예시로 들어서 진행해 보자.

#### Component

```java
public interface Beverage {

    public double cost();

    public String getDescription();

}
```

- Beverage 인터페이스가 **Component이다.**
- 즉 여기서 실질적인 인스턴스를 컨트롤하게 된다.
​

#### ConcreteComponent
```java
public class Espresso implements Beverage {
    @Override
    public double cost() {
        return 2000;
    }

    @Override
    public String getDescription() {
        return "에스프레소";
    }
}

public class HouseBlend implements Beverage {
    @Override
    public double cost() {
        return 3000;
    }

    @Override
    public String getDescription() {
        return "하우스 블렌드 커피";
    }
}
```

- 음료들이 Beverage를 구현하므로 ConcreteComponent가 된다.
- 이 음료들이 책임의 주체가 된다.

#### Decorator

```java
public interface Decorator extends Beverage {
    public String getDescription();

    public double cost();
}
```

- 어떤 책임을 덧붙이기 위해 사용되는 Decorator로 여기서는 음료의 옵션을 덧붙이기 위해 사용된다.
- Component를 상속받아 만들어 Component와 ConcreteDecorator를 이어주는 역할을 한다.

#### ConcreteDecorator

```java
public class Whip implements Decorator {

    private Beverage beverage;

    public Whip(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", 휘핑 크림";
    }

    @Override
    public double cost() {
        return beverage.cost() + 1500;
    }
}


public class MoCha implements Decorator {

    private Beverage beverage;

    public MoCha(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public double cost() {
        return beverage.cost() + 1000;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", 모카";
    }
}
```

- 커피의 옵션을 구현한 ConcreteDecorator이다.
- 최상위 부모인 Beverage를 멤버변수로 가지고 있다.
- 여기서는 getDescription과 cost()를 수행할 때 자신의 옵션과 함께 가지고 있는 beverage의 메서드도 함께 실행한다.

#### 결과​

```java
public class Test {
    public static void main(String[] args) {
        Beverage beverage1 = new HouseBlend();
        beverage1 = new MoCha(beverage1);
        beverage1 = new Whip(beverage1);

        System.out.println("가격: " + beverage1.cost());
        System.out.println("메뉴: " + beverage1.getDescription());
    }
}

가격: 5500.0
메뉴: 하우스 블렌드 커피, 모카, 휘핑 크림
```
- beverage1은 처음에 ConcreteComponent중 하나인 HouseBlend 인스턴스를 생성한다.
- 그 후에 옵션중 하나인 ConcreteDecorator의 MoCha를 생성하는데 생성자 아규먼트로 beverage1을 넣어준다.
- 즉 Mocha가 HouseBlend를 감싼 것이다.
- 그 후 또 Whip을 생성하여 한번 더 감싼다.
- 즉 Whip -> Mocha -> HouseBlend순으로 감싸게 되었다.
- 그 후 출력을 위해 메서드를 호출하게 되면 Whip의 method가 호출될 것이고 그 때 Whip이 가지고 있는 (Mocha -> HouseBlend)의 메서드가 호출되고 마지막으로 HouseBlend의 메서드가 호출된다.
- 그렇게 호출 스택이 쌓이고 빠져나가면서 하나씩 실행 되므로 위와같은 출력결과가 나온 것을 확인할 수 있다.

> 데코레이터 패턴을 이용하면 음료들의 옵션을 음료 각각의 구현을 변경하지 않고 유연하게 옵션을 추가할 수 있다.

---

[참고 도서](http://www.yes24.com/Product/Goods/1778966)

​
