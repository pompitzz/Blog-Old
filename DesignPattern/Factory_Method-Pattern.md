# 팩토리 메서드 패턴
- 객체를 생성하기 위해 인터페이스를 정의하고, 어떤 클래스의 인스턴스를 만드는 것은 서브 클래스에서 결정하게 만드는 패턴
- **클래스의 인스턴스를 만드는 일을 서브 클래스에게 맡긴다.**

[##_Image|kage@lXCEG/btqAmtg3SnU/IMY6hVaBkeq9HsaKxvqXz1/img.png|alignCenter|width="628" data-origin-width="0" data-origin-height="0"|||_##]

Creator
- 팩토리 메서드에서 생성한 인스턴스를 이용해야 하므로 추상 클래스로 구현한다.

Product
- 하위 클래스들이 공통으로 가지는 행동이 있다면 추상 클래스로 생성하고 그렇지 않으면 interface로 생성한다.


> 다이어 그램에서 interface란 java의 Interface 구현 타입이 아닌 **추상화된 메서드를 구현** 한다는 뜻이다.

### 예제

#### Creator

```java
public abstract class PizzaStore {
    public void orderPizza(PizzaType type) {
        Pizza pizza;

        pizza = createPizza(type);

        pizza.description();

    }

    abstract Pizza createPizza(PizzaType type);
}
```

- PizzaStore가**Creator**이다.
- **createPizza라는 factoryMethod로** Pizza 인스턴스를 생성하고 그 Pizza를 orderPizza에서 사용한다.
- 여기서 createPizza는 추상 메서드로 구성되었기 때문에 이 PizzaStore 자체에서는 어떤 Pizza가 생성되는지 알 수 없다.

#### Product

```java
public abstract class Pizza {
    String name;

    String dough;

    public void description() {

        System.out.println("피자의 이름은: " + name);
        System.out.println("피자의 도우는: " + dough);
    }
}
```

- 위에서 PizzaStore(Creator)가 가지고 있는 Pizza가 Product가 된다.
- decription메서드는 Pizza 하위 클래스가 공통으로 사용이 가능하므로 추상 클래스로 구현하였다.

#### ConcreteCreator

```java
public class SeoulPizzaStore extends PizzaStore {
    @Override
    Pizza createPizza(PizzaType type) {
        if (type == PizzaType.Bacon) {
            return new SeoulBaconPizza();
        } else if (type == PizzaType.Beef) {
            return new SeoulBeefPizza();
        } else {
            return null;
        }

    }
}

public class BusanPizzaStore extends PizzaStore {
    @Override
    Pizza createPizza(PizzaType type) {
        if (type == PizzaType.Bacon) {
            return new BusanBaconPizza();
        } else if (type == PizzaType.Beef) {
            return new BusanBeefPizza();
        } else {
            return null;
        }
    }
}
```
- 실제 Pizza 인스턴스를 생성하는 ConcreteCreator이다.
- 파라미터로 받는 타입에 따라 알맞는 Pizza를 생성하여 반환한다.
- 여기서 리턴하는 각 Pizza 객체들이 Pizza를 구현한 ConcreteProduct이다.

```java
public enum PizzaType {
    Bacon, Beef
}
```
- PizzaType은 enum으로 구성하였다.

#### ConcreteProduct

```java
public class SeoulBeefPizza extends Pizza {
    public SeoulBeefPizza() {
        name = "서울식 비프 피자";
        dough = "오리지널 도우";
    }
}

public class SeoulBaconPizza extends Pizza {
    public SeoulBaconPizza() {
        name = "서울식 베이컨 피자";
        dough = "씬 도우";
    }
}

public class BusanBaconPizza extends Pizza {
    public BusanBaconPizza() {
        name = "부산식 베이컨 피자";
        dough = "오리지널 도우";
    }
}

public class BusanBeefPizza extends Pizza {
    public BusanBeefPizza() {
        name = "부산식 비프 피자";
        dough = "나폴리 도우";
    }
}
```

- ConcreteProduct들 이다.
- ConcreteCreator인 각 지역별 PizzaStore들이 타입에 맞게 ConcreteProduct를 Creator인 PizzaStore에게 리턴해준다.

#### 결과

```java
public class Main {
    public static void main(String[] args) {
        PizzaStore busanStore = new BusanPizzaStore();

        PizzaStore seoulStore = new SeoulPizzaStore();

        busanStore.orderPizza(PizzaType.Beef);

        System.out.println("============================");

        seoulStore.orderPizza(PizzaType.Bacon);
    }
}

피자의 이름은: 부산식 비프 피자
피자의 도우는: 나폴리 도우
============================
피자의 이름은: 서울식 베이컨 피자
피자의 도우는: 씬 도우
```
- 우선 각 부산, 서울 피자스토어를 생성하였다.
- 부산의 orderPizza에는 Beef 타입을 넣고 서울은 Bacon을 넣는다.
- 각각 orderPizza를 호출하면 createPizza를 호출하여 타입에 맞는 Pizza를 생성한다.
- 그 후 생성된 pizza를 출력하게 되는 것이다.
- 클라이언트인  피자스토어는 피자의 타입만 정하고 order했을 뿐 그 피자가 생성되어 출력되는 것은 알 수 없다.
- 그렇기 때문에 피자의 종류가 추가된다고 하더라도 각 지열변 피자 스토에 따로 추가만 해주면되므로 PizzaStore자체는 변경할 필요가 없다.

> PizzaStore는 하위 클래스에게 Pizza 생성을 위임했으므로 Pizza와의 결합도가 낮아져 유연한 설계를 할 수 있게 되었다.

---

### 의존성 뒤집기 원칙(Dependency Inversion Principle)
> 구체화된 타입이 아닌 추상화된 타입에 의존하라

- 위의 예제에서 만약 Pizza라는 추상 클래스가 아닌 각 종류별 피자에 의존했다면 유연한 설계가 불가능 헀을 것이다.
- 각 종류별 피자라는 구체화된 타입이 아닌 추상화된 피자에 의존하므로 유연할 설계까 가능했다.
- 즉 구체화된 타입이 아닌 추상화된 타입에 의존하라는 의존성 뒤집기 원칙을 지키게 되면 유연한 설계가 가능해진다.

---
[참고 도서](http://www.yes24.com/Product/Goods/1778966)
