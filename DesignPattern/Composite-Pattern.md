#컴포지트 패턴
> 객체들을 트리 구조로 구성하여 부분과 전체를 나타내는 계층구조로 만들 수 있다.

- 이 패턴을 이용하면 클라이언트에서 개별 객체와 다른 객체들로 구성된 복합 객체를 똑같은 방법으로 다룰 수 있다.


<br>

- [이터레이터 패턴](https://sun-22.tistory.com/25)에서 구현한 예제에서 만약 점심, 저녁 메뉴에 디저트 메뉴들을 추가 해야 한다고 가정해보자.
- 이럴 때 컴포지트 패턴을 구현한다면 다른 메뉴들을 추가하더라도 처음 부터 구현할 필요 없이 간단하게 구현할 수 있다.

[##_Image|kage@csRR0s/btqAC0DYoLP/Nw2xzltXBfSdKdMTx7gTY0/img.png|alignCenter|data-origin-width="0" data-origin-height="0" width="533" height="317"|||_##]
Component
- Component는 Composite와 Leaf의 부모가 되고 해당 두 객체가 가질 수 있는 동작들을 가지고 있다.

Composite
- Composite는 Component를 컬렉션으로 가지고 있다.
- 즉 자기자신인 Composite와 Leaf를 가질 수 있는 것이다.
- 컬렉션으로 가지고 있는 Component들을 추가 제거할 수 있고 opertion() 또한 가능하다.

Leaf
- opertion()만 할 수 있는 객체이다.


### 예제

#### Component
```java
public abstract class MenuComponent {
    public void add(MenuComponent menuComponent) {
        throw new UnsupportedOperationException();
    }

    public String getName() {
        throw new UnsupportedOperationException();
    }

    public String getDescription() {
        throw new UnsupportedOperationException();
    }


    public double getPrice() {
        throw new UnsupportedOperationException();
    }

    public void print() {
        throw new UnsupportedOperationException();
    }
}
```
- MenuComponent가 Component가 된다.
- Composite와 Leaf가 가질 수 있는 모든 행동을 가진다.
- 만약 하위 클래스들이 이 행동을 구현하지 않는다면 예외를 던지도록 설계하였다.

​<br>

#### Composite
```java
public class Menu extends MenuComponent {
    private List<MenuComponent> menuComponents;
    private String name;

    public Menu(String name) {
        menuComponents = new ArrayList<>();
        this.name = name;
    }
    @Override
    public void add(MenuComponent menuComponent) {
        menuComponents.add(menuComponent);
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public void print() {
        System.out.println("\n           " + getName());
        System.out.println("--------------------------------");
        for (MenuComponent menuComponent : menuComponents) {
            menuComponent.print();
        }
    }
}
```
- Menu가 **Composite가** 된다.
- MenuComponent를 List형태로 가지고 있고 menuComponents를 통해 행동을 정의하고 저장할 수 있다.
- Menu, MenuItem 모두 menuComponents에 추가 될 수 있다.

<br>

#### Leaf
```java
public class MenuItem extends MenuComponent {
    private String name;
    private String description;
    private double price;

    public MenuItem(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public double getPrice() {
        return price;
    }
    @Override
    public void print() {
        System.out.print(getName());
        System.out.println(" ," + getPrice());
        System.out.println(" -- " + getDescription());
    }
}
```

- ​MenuItem이 **Leaf** 이다.
- 기존타입의 데이터로 name, description, price만 존재한다.
- MenuItem은 오직 출력만 하는 기능을 제공할 뿐 이외의 기능은 없다.

#### Client
```java
public class Waitress {
    MenuComponent allMenus;

    public Waitress(MenuComponent getAllMenus) {
        this.allMenus = getAllMenus;
    }
    public void printMenu() {
        allMenus.print();
    }
}
```
- 클라이언트인 Waitress는 **MenuComponent만을 참조하여 모든 메뉴들을 출력할 수 있다.**

​
#### Test
```java
public class Main {
    public static void main(String[] args) {
        MenuComponent lunchMenu = new Menu("점심 메뉴");
        MenuComponent dinerMenu = new Menu("저녁 메뉴");
        MenuComponent dessertMenu = new Menu("디저트 메뉴");

        MenuComponent allMenus = new Menu("전체 메뉴");

        createLunchAndDinerMenu(lunchMenu, dinerMenu);

        allMenus.add(lunchMenu);
        allMenus.add(dinerMenu);

        createDesserMenu(dessertMenu);

        lunchMenu.add(dessertMenu);
        dinerMenu.add(dessertMenu);

        Waitress waitress = new Waitress(allMenus);
        waitress.printMenu();

    }
    private static void createLunchAndDinerMenu(MenuComponent lunchMenu, MenuComponent dinerMenu) {
        lunchMenu.add(new MenuItem("토스트 세트"
                , "기본 토스트에 아메리카노 포함"
                , 4000));
        lunchMenu.add(new MenuItem("아보카도 토스트 세트"
                , "아보카도와 다양항 야채들이 들어간 샌드위치에 아메리카노 포함"
                , 5000));
        dinerMenu.add(new MenuItem("알리오 올리오 파스타"
                , "베이컨, 마늘, 핫페퍼가 들어간 파스타"
                , 10000));
        dinerMenu.add(new MenuItem("마르게리타 피자"
                , "토마토, 모차렐라, 바질이 들어간 피자"
                , 12000));
    }

    private static void createDesserMenu(MenuComponent dessertMenu) {
        dessertMenu.add(new MenuItem("팥빙수"
                , "팥과 연유가 가득 들어간 빙수"
                , 4000));
        dessertMenu.add(new MenuItem("망고빙수"
                , "망고가 들어간 빙수"
                , 6000));
    }
}
```
- 우선 각각 점심, 저녁, 디저트 메뉴를 생성하였다.
- 전체 메뉴를 생성하였다. 전체 메뉴가 Component가 될 것이다.
- 점심, 저녁 메뉴에 각 메뉴아이템을 생성하여 추가한다.
- 전체 메뉴에 점심, 저녁 메뉴를 추가한다.
- 디저트 메뉴에 디저트 메뉴 아이템들을 추가한다.
- 점심, 저녁 메뉴에 디저트 메뉴를 추가한다.
- 웨이터는 Component인 allMenus 하나만 알고 있으면 모든 메뉴를 출력할 수 있다.

<br>

[##_ImageGrid|kage@3LvP4/btqABDwpEhg/WpL86K6edxNq9cLPlaZQkK/img.png,kage@bzhV1W/btqAAtuwRAI/FMtwtAHUzb2Jj6zXG7VtM1/img.png|data-lazy-src="" data-width="557" data-height="387" data-origin-width="0" data-origin-height="0" style="width: 50.8701%; margin-right: 10px;",data-lazy-src="" data-width="437" data-height="322" data-origin-width="0" data-origin-height="0" style="width: 47.9671%;"|_##]
- 출력을 확인해보면 점심, 저녁메뉴에 디저트 메뉴가 알맞게 들어간 것을 알 수 있다.

---

### 단일 역할 원칙
> 클래스를 바꾸는 이유는 한 가지뿐이어야 한다.
<br>
[이터레이터 패턴](https://sun-22.tistory.com/25)에서 언급한 원칙이다.

- 컴포지트 패턴에서는 단일 역할 원칙이 지켜지지 않았다.
- 유연성 측면에서는 단일 역할 원칙을 지키지 않은 컴포지트 패턴이 안좋다고 생각할 수 있다.
- 하지만 클라이언트인 웨이터는 복잡한 구현 없이 Component 하나만 알고 있으면 모든 메뉴를 출력할 수 있다.
- 이렇듯 디자인을 설계할 때는 기존의 디자인 원칙을 지키는 것도 중요하나 더 효율적인 방법이 있다면 그게 따라 새로운 방법을 시도해 나가야 한다.
- 모든 설계는 적절한 트레이드오프를 통해 이루어져야 한다.
​
---

[참고 도서](http://www.yes24.com/Product/Goods/1778966)
