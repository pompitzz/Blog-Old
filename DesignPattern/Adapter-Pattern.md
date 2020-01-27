# 어댑터 패턴
> 한 클래스의 인터페이스를 클라이언트에서 사용하고자 하는 다른 인터페이스로 변환한다.

- 어댑터를 이용하면 인터페이스 호환성 문제 때문에 같이 쓸 수 없는 클래스 들을 연결해서 쓸 수 있다.

​

[##_Image|kage@yEVoX/btqAwkQughV/NPezPFzrC3zkUOp4vYMDnK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]
Target
- 클라이언트는 Target만을 알고 있다.
- 즉 다른 인터페이스는 이 Target으로 변환되어야 한다.

Adapter
- Adaptee를 Target으로 변환해주는 중간 매개체이다.

Adaptee
- 변환이 될 대상이다.

### 예제

- 새로운 번역기가 있고 오래된 번역기가 있는데 클라이언트는 새로운 번역기만 알고 있다.

#### Target
```java
public interface NewTranslator {

    public void koreanTranslation();

    public void chineseTranslation();

    public void russianTranslation();

    public void frenchTranslation();

}
```

- NewTranslator가 Client가 알고 있는 **Target** 이 된다.
- 한국어, 중국어, 러시아어, 불어를 변역하는 기능을 가지고 있다.

#### Adaptee
```java
public interface OldTranslator {

    public void koreanTranslation();

    public void chineseTranslation();
}
```
- OldTranslator는 변환이 되는 대상인 **Adaptee** 이다.
- 한국어와 중국어만 변환할 수있다.

<br>

```java
public class ATranslator implements OldTranslator {
    @Override
    public void koreanTranslation() {
        System.out.println("한국어로 번역합니다.");
    }

    @Override
    public void chineseTranslation() {
        System.out.println("중국어로 번역합니다.");
    }
}
```

- ATranslator는 OldTranslator를 실체화한 것이다.

#### Adapter
```java
public class OldTranslatorAdapter implements NewTranslator {

    private OldTranslator oldTranslator;

    public OldTranslatorAdapter(OldTranslator oldTranslator) {
        this.oldTranslator = oldTranslator;
    }

    @Override
    public void koreanTranslation() {
        oldTranslator.koreanTranslation();
    }

    @Override
    public void chineseTranslation() {
        oldTranslator.chineseTranslation();
    }

    @Override
    public void russianTranslation() {
        throw new UnsupportedOperationException("지원되지 않는 기능입니다.");
    }

    @Override
    public void frenchTranslation() {
        throw new UnsupportedOperationException("지원되지 않는 기능입니다.");
    }
}
```
- 실제 Adapter 역할을 하는 Adapter이다.
- OldTranslator를 구성하고 있으며 NewTranslator를 상속받고 있다.
- NewTranslator의 기능들 중 OldTranslator가 가지고 있는 기능은 메서드를 호출하여 그대로 기능하고 나머지 기능들은 예외를 던진다.

​

```java
public class Client {
    public static void main(String[] args) {
        ATranslator aTranslator = new ATranslator();
        NewTranslator oldTranslatorAdapter = new OldTranslatorAdapter(aTranslator);
        oldTranslatorAdapter.chineseTranslation();
        oldTranslatorAdapter.koreanTranslation();
    }
}
```
- 클라이언트는 Adapter를 통해 NewTranslator로 OldeTranslator의 구현체인 ATranslator를 사용할 수 있다.
- NewTranslator의 기능들 중 구현할 수 없는 것들은 예외를 던졌지만 클라이언트는 NewTranslator만 알고 있으면 된다.
- 유연한 설계를 할 수있게 되었다.
