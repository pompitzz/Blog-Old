# 스테이트 패턴(상태 패턴)
> 객체의 내부 상태가 바뀜에 따라서 객체의 행동을 바꿀 수 있다.

- 마치 객체의 클래스가 바뀌는 것과 같은 결과를 얻을 수 있다.

​

### 스테이트 패턴을 사용하지 않았을 때
- 자판기가 있고 동전을 넣은 후 버튼을 누르면 물병이 나오는 시스템을 구현해본다.

```java
public class VendingMachine {

    final static int NO_COIN = 0;
    final static int HAS_COIN = 1;
    final static int SOLD = 2;

    int state = NO_COIN;

    public VendingMachine() {
    }
    public void insertCoin() {
        if (state == NO_COIN) {
            System.out.println("동전을 넣습니다");
            state = HAS_COIN;
        } else if (state == HAS_COIN) {
            System.out.println("이미 동전이 있습니다");
        } else {
            System.out.println("물병이 나오고 있습니다.");
        }
    }
    public void ejectCoin() {
        if (state == NO_COIN) {
            System.out.println("동전을 넣어주세요");
        } else if (state == HAS_COIN) {
            System.out.println("동전이 반환됩니다");
            state = NO_COIN;
        } else {
            System.out.println("물병이 나오고 있으므로 반환할 수 없습니다.");
        }
    }
    public void pushButton() {
        if (state == NO_COIN) {
            System.out.println("동전을 넣어주세요");
        } else if (state == HAS_COIN) {
            System.out.println("버튼을 눌렀습니다.");
            state = SOLD;
            comingOut();
        } else {
            System.out.println("이미 물병이 나갔습니다.");
        }
    }
    private void comingOut() {
        if (state == NO_COIN) {
            System.out.println("동전을 넣어주세요");
        } else if (state == HAS_COIN) {
            System.out.println("물병이 나오고 있습니다.");
        } else {
            System.out.println("물병이 나갔습니다.");
            state = NO_COIN;
        }
    }
}
```
- VendingMachine이 모든 상태를 가지며 if문으로 구현하였다.
- final static int를 통해 숫자에 맞게 상태를 정의하여 구현할 수 있다.
- 만약 여기서 이벤트를 열어 1/10확률로 물병과 함께 과자가 같이 나오게 하기 위해선 어떻게 해야 할까?
- WINNER라는 상태를 만든다.
- 그리고 모든 메서드 if 문에 하나하나 추가해야 할 것이다.
- 변화에 유연하지 않으며 객체지향적이지 않은 방식인 것을 알 수 있다.

> 이럴 때 **스테이트 패턴** 을 이용하면 객체지향적으로 코드를 구현할 수 있다.

---

### 스테이트 패턴 다이어그램

[##_Image|kage@vAWq2/btqAB4f1S3e/zrP8bZAjGr0Zyl4YH2Vuzk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

State
- 상태에 따른 기능을 정의하여 서브 클래스가 자신의 상태에 맞게 구현할 수 있도록 한다.

ConcreteState
- State를 구현하여 자신의 상태에 맞게 행동을 설계한다.

Context
- Context는 State를 통해 ConcreteState에서 구현된 행동을 실행할 수 있다.

### 구현

#### State
```java
public interface State {

    void insertCoin();

    void ejectCoin();

    void pushButton();

    void comingOut();
}
```
- State로 ConcreteState가 구현할 행동을 정의한다.
​
<br>

#### ConcreteState
```java
public class Sold implements State {

    VendingMachine vendingMachine;

    public Sold(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin() {
        System.out.println("물병이 나오고 있습니다");
    }

    @Override
    public void ejectCoin() {
        System.out.println("물병이 나오고 있어 동전을 반환할 수 없습니다.");
    }

    @Override
    public void pushButton() {
        System.out.println("버튼은 한번만 클릭 하세요.");
    }

    @Override
    public void comingOut() {
        System.out.println("물병이 나갔습니다.");
        vendingMachine.setState(vendingMachine.getNoCoin());
    }
}
```
- SoldState이며 VendingMachine을 필드에 가지고 있다.
- Sold 상태에서는 동전을 넣으면 물병이 나오고 있다고 알려준다.
- 동전을 반환하려고 하면 이미 판매되어 반환이 불가능하다고 한다.
- 버튼을 누르면 한 번만 누르라고 경고한다.
- 물병이 나오면 클라이언트에게 알려준 후 vendingMachine의 상태를 NoCoin으로 변경한다.

<br>

```java
public class HasCoin implements State {

    VendingMachine vendingMachine;

    public HasCoin(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin() {
        System.out.println("이미 동전이 있습니다.");
    }

    @Override
    public void ejectCoin() {
        System.out.println("동전을 반환합니다.");
        vendingMachine.setState(vendingMachine.getNoCoin());
    }

    @Override
    public void pushButton() {
        System.out.println("버튼을 클릭합니다");
        vendingMachine.setState(vendingMachine.getSold());
    }

    @Override
    public void comingOut() {
        System.out.println("물병이 나갈 수 없습니다.");
    }
}
```
- HasCoin State로 Sold State와 동일하게 VendingMachine을 가지고 있다.
- 동전이 있는데 또 동전을 넣으려고 하면 동전이 있다고 알려준다.
- 동전을 반환하려고 하면 동전을 반환 시켜주고 VendingMachine의 상태를 NoCoin으로 변경 한다.
- 버튼을 클릭하면 VendingMachine의 상태를 Sold로 변경한다.

<br>

```java
public class NoCoin implements State {

    VendingMachine vendingMachine;

    public NoCoin(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin() {
        System.out.println("동전을 넣습니다.");
        vendingMachine.setState(vendingMachine.getHasCoin());
    }

    @Override
    public void ejectCoin() {
        System.out.println("동전을 넣어주세요");
    }

    @Override
    public void pushButton() {
        System.out.println("동전을 넣어주세요");
    }

    @Override
    public void comingOut() {
        System.out.println("동전을 넣어주세요");
    }
}
```
- NoCoin은 동전을 넣게 되면 VendingMachine의 상태를 HasCoin으로 변경한다.
- 나머진 모두 동전을 넣으라고 요구한다.;


> 이렇게 3개의 상태를 클래스로 State을 구현하였다.

​
#### Context
```java
@Getter
@Setter
public class VendingMachine {

    private State noCoin;
    private State hasCoin;
    private State sold;

    private State state;

    public VendingMachine() {
        noCoin = new NoCoin(this);
        hasCoin = new HasCoin(this);
        sold = new Sold(this);
        state = noCoin;
    }

    public void insertCoin() {
        state.insertCoin();
    }

    public void ejectCoin() {
        state.ejectCoin();
    }

    public void pushButton() {
        state.pushButton();
        state.comingOut();
    }
}
```
- VendingMachine이 Context이다.
- 우선 필드 변수로 각 상태들을 가지고 있다.
- 생성시 각 상태의 생성자에 자신의 전달인자로 전달해주고 상태들을 생성한다.
- 시작은 noCoin 상태일 것이므로 state를 noCoin으로 설정한다.
- insertCoin, ejectCoin, pushButton에 맞게 각 상태들의 기능을 호출한다.


> **여기서 만약 WINNER라는 상태를 추가하게 된다면 어떻게 될까?**

<br>

```java
public class Winner implements State {

    VendingMachine vendingMachine;

    public Winner(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public void insertCoin() {
        System.out.println("물병이 나오고 있습니다");
    }

    @Override
    public void ejectCoin() {
        System.out.println("물병이 나오고 있어 동전을 반환할 수 없습니다.");
    }

    @Override
    public void pushButton() {
        System.out.println("버튼은 한번만 클릭 하세요.");
    }

    @Override
    public void comingOut() {
        System.out.println("이벤트에 당첨되어 과자도 함께 지급됩니다.");
        vendingMachine.setState(vendingMachine.getNoCoin());
    }
}
```
- 우선 Winner라는 상태를 구현한다.

<br>

```java
@Override
public void pushButton() {
    System.out.println("버튼을 클릭합니다");

    Random random = new Random();
    int winner = random.nextInt(10);

    if (winner == 0) vendingMachine.setState(vendingMachine.getWinner());

    vendingMachine.setState(vendingMachine.getSold());
}
```
- HasCoin의 pushButton 메서드에서 10% 확률로 winner 상태가 되게 설정한다.

<br>

```java
private State noCoin;
private State hasCoin;
private State sold;

// Winenr 추가
private State winner;

private State state;

public VendingMachine() {
    noCoin = new NoCoin(this);
    hasCoin = new HasCoin(this);
    sold = new Sold(this);

    // Winenr 추가
    winner = new Winner(this);
    state = noCoin;
}
```
- 마지막으로 VendingMachine에 상태를 추가해주기만 하면 된다.
- 스테이트 패턴을 사용하지 않았을 때는 상태를 static int 형태로 추가해주고 기능마다 if문 하나하나를 직접 수정해야 했다.
- 만약 기능이 수백가지 였다면 매우 비효율적이 였을 것이다.
- 하지만 스테이트 패턴을 이용하면 상태를 객체지향적으로 추가할 수 있고 추가된 상태를 사용하는 곳과 VendingMachine에서 추가된 상태만 필드에 넣어주면된다.

#### Main
```java
public class Main {
    public static void main(String[] args) {
        VendingMachine vendingMachine = new VendingMachine();

        vendingMachine.insertCoin();
        vendingMachine.pushButton();
        vendingMachine.ejectCoin();
    }
}
```
- Client는 Context인 VendingMachine만 알면 자판키를 작동시킬 수 있게 되었다.
​

---

### 스트래티지 패턴과 스테이트 패턴

- 스트래티지 패턴과 같은 다이어그램을 가지고 있지만 두 패턴의 용도에서 차이가 난다.
- 스테이트 패턴에서는 클라이언트는 상태 객체에 대해서 알필요 없고 Context만 알면 된다.
- 스트래티지 패턴은 객체를 유연하게 바꾸는 게 목적이므로 어떠한 객체들을 직접 지정해서 사용하므로 각 세부 객체들을 알고 있어야 한다.

​
---

[참고 도서](http://www.yes24.com/Product/Goods/1778966)
