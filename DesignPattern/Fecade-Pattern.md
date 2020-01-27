# 퍼사드 패턴
> 어떤 서브시스템의 일련의 인터페이스에 대한 통합된 인터페이스를 제공한다.

- 퍼사드에서 고수준 인터페이스를 정의하기 때문에 서브시스템을 더 쉽게 사용할 수 있다.

#### 어댑터 패턴과의 차이

- 어댑터 패턴은 인터페이스를 변경해서 클라이언트가 필요로 하는 인터페이스로 변환하는 것
- 퍼사드 패턴은 어떤 서브시스템에 대한 간단한 인터페이스로 제공하는 것이다.

#### 퍼사드 패턴 다이어그램

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2FcXWGZN%2FbtqAuMGVazi%2FWRFrogtHCJTMbw3KeOT70K%2Fimg.png)

- 수많은 subsystem class 들을 Facade에서 통합된 인터페이스로 제공한다.
- Clent는 Facade 하나만 알고 있으면 수많은 subsystem class들을 사용할 수 있다.

### 예제
- 영화관에서 영화를 상영하는 시스템이 있다고 생각해보자.
- 영화을 상영하기 위해 필요한 절차는 아래와 같다.
  - (1) 조명을 영화관 모드로 변경
  - (2) 스크린을 내림
  - (3) 프로젝터를 킴
  - (4) 프로젝터를 영화관 모드로 변경
  - (5) DVD Player를 킴
  - (6) DVD Player에서 영화를 시작

<br>

- 위의 모든 과정을 수행하기 위해서는 조명, 프로젝터, 스크린, DVD Player 객체을 알고 있어야한다.
- 클라이언트가 이 모든 객체를 알고 있으면 결합도가 상당히 높고 복잡해 진다.
- 퍼사드 패턴을 이용하여 클라이언트는 퍼사드 객체 하나만으로 영화를 상영할 수 있게 구현해본다.

#### SubSystem
```java
public class Light {
    public void on() {
        System.out.println("조명 킵니다.");
    }

    public void movieMode() {
        System.out.println("조명을 영화관 모드로 변경합니다.");
    }
}

public class Screen {
    public void down() {
        System.out.println("스크린을 내립니다.");
    }

    public void up() {
        System.out.println("스크린을 올립니다.");
    }
}

public class Projector {
    public void on() {
        System.out.println("Projector를 킵니다.");
    }


    public void off() {
        System.out.println("Projector를 끕니다.");
    }

    public void movieMode() {
        System.out.println("Projector를 영화관모드로 설정합니다.");
    }
}

public class DvdPlayer {
    public void on() {
        System.out.println("DvdPlayer를 킵니다.");
    }

    public void play(String movieName) {
        System.out.println(movieName + "를 시작합니다.");
    }

    public void off() {
        System.out.println("DvcPlayer를 끕니다.");
    }
}
```

- 영화를 상영하는데 필요한 객체들이다.
- 조명, 스크린, 프로젝터, DVD player로 구성된다.

#### Fecade
```java
public class HomeTheaterFacade {
    Light light;
    Projector projector;
    Screen screen;
    DvdPlayer dvdPlayer;

    public HomeTheaterFacade(Light light, Projector projector,
                             Screen screen, DvdPlayer dvdPlayer) {
        this.light = light;
        this.projector = projector;
        this.screen = screen;
        this.dvdPlayer = dvdPlayer;
    }

    public void watchMovie(String movieName) {
        System.out.println("===============WATCH MOIVE===============");
        light.movieMode();
        screen.down();
        projector.on();
        projector.movieMode();
        dvdPlayer.on();
        dvdPlayer.play(movieName);
    }

    public void endMovie() {
        System.out.println("===============END MOIVE===============");
        light.on();
        screen.up();
        projector.off();
        dvdPlayer.off();
    }
}
```
- Fecade는 영화 상영에 필요한 모든 장치들을 가지고 있다.
- watchMovie가 호출되었을 때 해당 영화를 상영하는데 필요한 모든 동작을 수행한다.
- endMovie가 호출되었을 떄 영화를 종료하는데 필요한 모든 동작을 수행한다.

#### Test
```java
public class Client {
    public static void main(String[] args) {
        HomeTheaterFacade homeTheaterFacade =
                new HomeTheaterFacade(new Light(), new Projector(), new Screen(), new DvdPlayer());

        homeTheaterFacade.watchMovie("조커");
        homeTheaterFacade.endMovie();
    }
}
```
- 클라이언트에서는 HomeTheaterFacade만 알고 있으면 영화를 상영하고 끌 수 있다.

> 퍼사드 패턴을 통해 구현과 서브시스템을 분리하여 클라이언트는 퍼사드 객체 하나만 참조하므로 결합도가 낮아졌다.

---

### **최소 지식 원칙**
- 객체 자체의 메소드만 호출한다.
- 메소드에 매개변수로 전달된 객체의 메서드만 호출한다.
- 메소드에서 생성하거나 인스턴스를 만드는 객체의 메서드만 호출한다.
- 객체의 구성요소에 속하는 객체들의 메소드만을 호출한다.

> 이 원칙을 지키면 객체들 사이의 의존성을 줄일 수 있어 **결합도를 낮추고 응집도가 높은 객체지향적인 설계가** 가능해진다.

---
[참고 도서](http://www.yes24.com/Product/Goods/1778966)
