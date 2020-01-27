# 이터레이터 패턴
> 컬렉션 구현 방법을 노출시키지 않으면서도 그 집합체 안에 들어있는 모든 항목에 접근할 수 있게 해주는 방법을 제공해 준다.


[##_Image|kage@ARh75/btqAy2u00m3/Lum3YjmKrA5iJy4VIcwPZ0/img.png|alignCenter|data-origin-width="0" data-origin-height="0" width="552" height="284"|||_##]

### 예제

- 가게에서 점심 메뉴는 List로 구현하고 저녁 메뉴는 HashMap으로 구현되어 있다고 하자.
- 두 메뉴를 한 번에 나타내고 싶다고 하였을 때 이터레이터 패턴을 사용할 수 있다.

#### Aggregate
```java
public interface Menu {
    public Iterator createIterator();
}
```
- Iterator를 생성하는 인터페이스가 된다. Iterator는 자바에서 제공하는 API이다.


#### ConcreteAggregate

```java
public class MenuItem {
    private String name;
    private String description;
    private double price;

    public MenuItem(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
}
```
- 우선 각 메뉴들을 표현하기 위한 MenuItem을 정의한다.

<br>

LunchMenu
```java
public class LunchMenu implements Menu {

    private List<MenuItem> menuItems;

    public LunchMenu() {
        menuItems = new ArrayList<>();

        addItem("토스트 세트"
                , "기본 토스트에 아메리카노 포함"
                , 4000);

        addItem("아보카도 토스트 세트"
                , "아보카도와 다양항 야채들이 들어간 샌드위치에 아메리카노 포함"
                , 5000);

    }

    private void addItem(String name, String description, int price) {
        MenuItem menuItem = new MenuItem(name, description, price);
        menuItems.add(menuItem);
    }


    @Override
    public Iterator createIterator() {
        return menuItems.iterator();
    }
}
```
- LunchMenu는 리스트로 구현하였다.
- 오버라이드한 createIterator에서 자바가 제공해주는 API인 itertor()로 반환하였다.

<br>

DinnerMenu
```java
public class DinerMenu implements Menu {

    private Map<String, MenuItem> menuItems;

    public DinerMenu() {
        menuItems = new HashMap<>();

        addItem("알리오 올리오 파스타"
                , "베이컨, 마늘, 핫페퍼가 들어간 파스타"
                , 10000);

        addItem("마르게리타 피자"
                , "토마토, 모차렐라, 바질이 들어간 피자"
                , 12000);

    }

    private void addItem(String name, String description, int price) {
        MenuItem menuItem = new MenuItem(name, description, price);
        menuItems.put(menuItem.getName(), menuItem);
    }


    @Override
    public Iterator createIterator() {
        return menuItems.values().iterator();
    }
}
```
- HashMap으로 구성된 DinnerMenu이다.
- 이 또한 자바에서 제공해주는 API인 iterator로 번환하였다.

> ConcreteAggregate에서는 ConcreteIterator를 참조한다. 하지만 여기서는 그냥 JAVA에서 제공해주는 Iterator를 사용하여 생략하였다.

​
#### Client
```JAVA

public class Waitress {
    private Menu lunchMenu;
    private Menu dinerMenu;

    public Waitress(Menu lunchMenu, Menu dinerMenu) {
        this.lunchMenu = lunchMenu;
        this.dinerMenu = dinerMenu;
    }

    public void printMenu() {
        Iterator lunchIterator = lunchMenu.createIterator();
        Iterator dinerIterator = dinerMenu.createIterator();
        System.out.println("===== 점심 식사 메뉴 =====");
        printMenu(lunchIterator);
        System.out.println("===== 저녁 식사 메뉴 =====");
        printMenu(dinerIterator);
    }

    private void printMenu(Iterator iterator) {
        while (iterator.hasNext()) {
            MenuItem menuItem = (MenuItem) iterator.next();
            System.out.print(menuItem.getName() + ", ");
            System.out.print(menuItem.getDescription() + ", ");
            System.out.println(menuItem.getPrice());
        }
    }
}
```
- Waitress가 Client이다.
- 웨이터는 추상화된 Menu와 Iterator만으로 메뉴들 출력할 수 있다.
- Menu들의 세부사항들이 어떤 컬렉션으로 구현되었는지 알 필요없다.
- 모든 컬렉션타입은 Iterator로 변환되기 떄문이다.
- 웨이터는 모든 메뉴들을 Iterator를 통해 접근하고 사용만 하면된다.
- 즉 책임이 분리된 설계를 할 수 있게 되었다.

#### Test​
```JAVA
public class Main {
    public static void main(String[] args) {
        LunchMenu lunchMenu = new LunchMenu();
        DinerMenu dinerMenu = new DinerMenu();

        Waitress waitress = new Waitress(lunchMenu, dinerMenu);

        waitress.printMenu();
    }
}

===== 점심 식사 메뉴 =====
토스트 세트, 기본 토스트에 아메리카노 포함, 4000.0
아보카도 토스트 세트, 아보카도와 다양항 야채들이 들어간 샌드위치에 아메리카노 포함, 5000.0

===== 저녁 식사 메뉴 =====
마르게리타 피자, 토마토, 모차렐라, 바질이 들어간 피자, 12000.0
알리오 올리오 파스타, 베이컨, 마늘, 핫페퍼가 들어간 파스타, 10000.0
```
- 테스트를 통해 위의 설계가 정상적으로 동작하는 것을 확인할 수 있다.


---

### **단일 역할 원칙**
- 클래스를 바꾸는 이유는 한 가지뿐이어야 한다.

> 위의 예에서 이터레이터를 구현하지 않고 그 기능들을 LunchMenu, DinerMenu에 넣었다면 어떻게 될까?

- 그렇게 된다면 컬렉션의 값을 바꿀 때도 클래스가 변경된다.
- 반복자 관련 기능(이터레이터가 해준 기능)을 바꿀 때도 클래스가 변경되어야 한다.
- 변경되는 이유가 두가지가 되므로 단일 역할 원칙을 지키지 못하게 된다.
- 이러한 설계는 **응집도가 낮아져 유연한 설계가 불가능** 하게 된다.
