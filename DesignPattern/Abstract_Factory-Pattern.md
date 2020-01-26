# 추상 팩토리 패턴
> 인터페이스를 이용하여 서로 연관된, 또는 의존하는 객체들을 구상 클래스를 지정하지 않고도 생성하는 패턴

[##_Image|kage@mhOYR/btqAnomn7d5/VE9SC8DpffX43NemklUifk/img.png|alignCenter|width="693" data-origin-width="0" data-origin-height="0"|||_##]

- 다이어 그램을 보면 지난번의 팩토리 메서드 패턴과는 조금 다른 것을 알 수 있다.
- 팩토리 메서드 패턴은 실제 팩토리를 구현하는 인스턴스를 만들 때 상위 클래스를 확장하고 팩토리 메서드를 오버라이드 하여 상속을 통해 인스턴스를 생성하였다.
- 하지만 추상 팩토리 패턴은 AbstractFactory를 Interface 타입으로 생성하고 그 **인터페이스를 실체화하여 객체를 생성** 하는 것을 알 수 있다.

---

### **예제**
- 운영체제에 따른 GuiFactory에 대한 예제이다.

#### AbstractFactory

```java
public interface GuiFactory {

    public Button createButton();

    public Text createText();

}
```
- AbstractFactory 부분으로 실제 생성해야 할 Product들을 규정하였다.

#### AbstractProduct

```java
public interface Button {

    public void click_Button();

}

public interface Text {

    public void text_Description();

}
```
- Factory를 통해 생성하는 Button과 Text가 AbstractProduct들 이다.
- ConcreteFactory에서 AbstractProduct를 상속받은 실제 Product들을 생성하게 될 것이다.


#### ConcreteFactory
```java
public class WindowGuiFactory implements GuiFactory {
    @Override
    public Button createButton() {
        return new WindowButton();
    }

    @Override
    public Text createText() {
        return new WindowText();
    }
}

public class MacGuiFactory implements GuiFactory {
    @Override
    public Button createButton() {
        return new MacButton();
    }

    @Override
    public Text createText() {
        return new MacText();
    }
}
```

- 각 운영체제의 Factory들이 ConcreteFactory이다.
- AbstractProduct를 구현한 각 운영체제의 Button과 Text인 Product들을 생성하여 리턴해준다.

#### Product
```java
public class WindowButton implements Button {
    @Override
    public void click_Button() {
        System.out.println("윈도우 버튼 클릭!!");
    }
}

public class WindowText implements Text {
    @Override
    public void text_Description() {
        System.out.println("윈도우 텍스트!!");
    }
}

public class MacButton implements Button {
    @Override
    public void click_Button() {
        System.out.println("맥 버튼 클릭!!");
    }
}

public class MacText implements Text {
    @Override
    public void text_Description() {
        System.out.println("맥 텍스트!!");
    }
}
```

- 각각 운영체제별 Button, Text들이다.
- ConcreteFactory에서 이 Product을 생성하는 것을 위에서 확인하였다.

### 테스트
```java
public class Main {
    public static void main(String[] args) {
        GuiFactory macGuiFactory = new MacGuiFactory();
        print_factory(macGuiFactory);

        System.out.println("===========================");

        GuiFactory windowGuiFactory = new WindowGuiFactory();
        print_factory(windowGuiFactory);
    }

    private static void print_factory(GuiFactory factory) {

        Button button = factory.createButton();
        Text text = factory.createText();

        button.click_Button();
        text.text_Description();
    }
}

맥 버튼 클릭!!
맥 텍스트!!
===========================
윈도우 버튼 클릭!!
윈도우 텍스트!!
```

- 클라이언트는 자신을 사용할 운영체제의 Factory를 생성하기만 하면 알아서 각 운영체제에 맞는 Button과 Text를 생성해준다.
- 패턴이름에서도 그렇듯이 팩토리 메서드 패턴과 마찬가지로 추상 형식에 의존하므로 결합도가 낮아서 유연한 설계를 할 수 있다.


#### 팩토리 메서드와의 차이점
- 팩토리 메서드 패턴은 하나의 팩토리마다 하나의 제품만 생성이 가능하다.
- 그렇기 때문에 [팩토리 메서드 게시글]()에서는 Pizza 타입을 추가하더라도 각 지역별 PizzaStore만 변경하면 되고 추상화된 인터페이스는 변경이 필요 없었다.
- 허나 추상 팩토리 패턴은 하나의 팩토리에서 연관된 다양한 제품(Button, Text)를 생산할 수 있다.
- 그러나 만약 Form이라는 Gui를 추가한다면 GuiFactory도 createForm을 추가해줘야하고 서브 클래스들도 변경이 필요하다.

> 그러므로 다양한 제품들을 만들 때는 추상 팩토리 패턴을 이용하는 것이 좋고 하나의 제품만 생산할 때는 팩토리 메서드를 활용하는 것이 좋다.

---

[참고 도서](http://www.yes24.com/Product/Goods/1778966)

​
