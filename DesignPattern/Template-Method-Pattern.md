# 템플릿 메서드 패턴
> 메소드에서 알고리즘의 골격을 정의한다. 알고리즘의 여러 단계 중 일부는 서브 클래스에서 구현할 수 있다.

- 템플릿 메서드를 이용하면 **알고리즘의 구조는 그대로 유지하면서 서브 클래스에서 특정 단계를 재정의할 수 있다.**

[##_Image|kage@JNUxH/btqAyFUk20J/UZktzpZ59wd3Xev16jkZK1/img.png|alignCenter|data-origin-width="0" data-origin-height="0" width="502" height="342"|||_##]

- AbstractClass의 templateMethod()에는 알고리즘의 골격이 정의되어 있다.
- 모든 객체들이 공통으로 수행하는 작업은 AbstractClass에서 수행한다.
- 그 후 나머지는 abstract 메서드로 구현하여 서브 클래스에서 구현하게 만든다.
​
### 예제

#### AbstractClass
```java
public abstract class CaffeineBeverage {

    final void prepareRecipe() {
        boilWater();
        brew();
        pourInCup();

        // hook
        if (wantCondiments()){
            addCondiments();
        }
    }

    private void boilWater() {
        System.out.println("물 끓이는 중");
    }

    protected abstract void brew();

    private void pourInCup() {
        System.out.println("컵에 따르는 중");
    }

    protected abstract void addCondiments();

    protected boolean wantCondiments() {
        return true;
    }
}
```
- AbstractClass에서는 boilWater와 pourInCup은 서브클래스들의 공통된 과정이라고 생각하고 여기서 직접 구현하였다.
- 서로 다른 동작을 하는 메서드들은 추상 메서드로 구현하였다.
- 모든 메서드들의 수행이 들어 있는 final로 구현된 prepareRecipe가 templateMethod이다.
- 메서드 내부에 메서드들의 순서가 정해져 있고 final로 구현되었기 때문에 하위 클래스에서는 이 메서드를 오버라이드 할 수 없다.

#### Hook
- if 문안에 있는 wantCondiments()이 **hook이다.**
- hook을 통해 서브 클래스에서 동작을 수행할지 결정할 수 있다.
- 부모 클래스에서 wantCondiments은 true를 리턴한다.
- 만약 하위 클래스에서 addCondiments를 원하지 않는다면 wantCondiments를 오버라이드 하여 false를 리턴하면 된다.
​

#### ConcreteClass
```java
public class Coffee extends CaffeineBeverage {
    @Override
    protected void brew() {
        System.out.println("커피를 내리는 중");
    }

    @Override
    protected void addCondiments() {
        System.out.println("설탕을 추가하는 중");
    }
}

public class Tea extends CaffeineBeverage {
    @Override
    protected void brew() {
        System.out.println("차를 우리는 중");
    }

    @Override
    protected void addCondiments() {
        System.out.println("레몬을 추가하는 중");
    }

    @Override
    protected boolean wantCondiments() {
        return false;
    }
}
```
- ConcreteClass들로 부모클래스에서 추상메서드로 작성한 것들을 구현한다.
- Tea는 hook인 wantCondiments를 따로 구현하여 false를 반환한다.
- Tea에서는 addCondiments가 동작하지 않을 것이다.

#### Test
```java
public class Main {
    public static void main(String[] args) {
        Coffee coffee = new Coffee();
        Tea tea = new Tea();

        coffee.prepareRecipe();
        System.out.println("=============================");
        tea.prepareRecipe();
    }
}

물 끓이는 중
커피를 내리는 중
컵에 따르는 중
설탕을 추가하는 중

=============================

물 끓이는 중
차를 우리는 중
컵에 따르는 중
```

- 결과를 확인해보면 정해진 단계별로 잘 출력이 되는것을 알 수 있다.
- Tea는 hook을 통해 addCondiments를 실행하지 않기로 하였기 때문에 템플릿 메서드들 실행하여도 addCondiments가 실행되지 않았다.
