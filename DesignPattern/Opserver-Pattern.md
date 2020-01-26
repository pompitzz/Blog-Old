# 옵저버 패턴

- 한 객체의 상태가 바뀌면 그 객체에 의존하는 다른 객체들한테 연락이 가고 자동으로 내용이 갱신되는 방식으로 일대다 의존성을 정의한다.  
- 어떠한 객체에 의존하는 다른 객체들을 **Observer** , 한 객체를 **Subject** 라고 한다.

### 클래스 다이어그램

[##_Image|kage@7w68X/btqAnor3EEA/p04847rLSmLMk2zTUfKUKk/img.jpg|floatLeft|data-origin-width="0" data-origin-height="0"|||_##]
​  
- Subject와 Observer는 인터페이스로 구현한다.
- 이렇게 Subject와 Observer를 인터페이스로 구현하여 사용하면 Subject는 Observer 인터페이스만 구현하고 Obsever의 구상 클래스가 무엇인지 알 필요가 없다.  

### 특징
- 옵저버는 언제든지 새로 추가할 수 있다.(Observer 인터페이스를 구현)  
- 새로운 형식의 옵저버를 추가해도 Subject를 전혀 변경할 필요가 없다.  
- Subject와 옵저버는 서로 독립적으로 재사용 가능하다.  
- Subject나 옵저버가 바뀌어도 서로에게 영향을 미치지 않는다.

---

### 옵저버 패턴 예제 
#### Subject, Observer
```java
public interface Subject {
    public void registerObserver(Observer o);

    public void removeObserver(Observer o);

    public void notifyObservers();
}

public interface Observer {
    public void update(float temperature, float humidity, float pressure);
}

public interface DisplayElement {
    public void display();

}

```
- Subject는 Observer Interface를 등록, 삭제, 알림(notify)을 할 수 있는 기능을 가지고 있다.
- Observer는 Subject가 notify해줬을 때 업데이트하는 기능을 가지고 있다.
- DisplayElement는 결과값을 출력하기 위해 생성하였다.

#### ConcreteSubject
```java
public class WeatherData implements Subject {

    private List<Observer> observers;
    private float temperature;
    private float humidity;
    private float pressure;

    public WeatherData() {
        observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        int i = observers.indexOf(o);
        if( i >= 0){
            observers.remove(i);
        }
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(temperature, humidity, pressure);
        }
    }

    public void changed(){
        notifyObservers();
    }

    public void setMeasurements(float temperature, float humidity, float pressure){
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        changed();
    }
}
```
- Subject를 구현한 클래스이다. 온도, 습도, 기압을 알려주는 클래스로 구현 하였다.
- Observer List와 온도, 기압, 습도를 멤버 변수로 가지고 있다.
- 온도, 습도, 기압을 setting할 때 changed(); 를 호출하여 등록된 observer들에게 notify 해준다.
- notify가 되면 등록된 observer들의 update가 호출된다.

#### ConcreteObserver
```java
public class ConcreteObserver1 implements Observer, DisplayElement {

    private float temperature;
    private float humidity;

    public ConcreteObserver1(Subject weatherData) {
        weatherData.registerObserver(this);
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        display();
    }

    @Override
    public void display() {
        System.out.println("ConcreteObserver1 => " + "온도: " + temperature + " 습도: " + humidity);
    }
}

public class ConcreteObserver2 implements Observer, DisplayElement {

    private float temperature;
    private float humidity;
    private float pressure;

    public ConcreteObserver2(Subject weatherData) {
        weatherData.registerObserver(this);
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        display();
    }

    @Override
    public void display() {
        System.out.println("ConcreteObserver2 => " + "온도: " + temperature + " 습도: " + humidity
                + "기압: " + pressure);
    }
}
```
- Observer를 구현한 ConcreteObserver를 두개 만든다.
- 하나는 온도, 습도만 가지고 다른 하나는 온도, 습도, 기압을 모두 가지고 있다.
- 각 Observer가 생성될 때 Subject를 파라미터로 받아 자기 자신을 등록해준다.
- update()메서드가 실행면 display()를 통해 출력이 된다.

#### 테스트
```java
public class Test {
    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();

        ConcreteObserver1 conditionsDisplay1 = new ConcreteObserver1(weatherData);
        ConcreteObserver2 conditionsDisplay2 = new ConcreteObserver2(weatherData);

        weatherData.setMeasurements(80, 65, 30.4f);
        weatherData.setMeasurements(60, 75, 16.3f);
        weatherData.setMeasurements(40, 35, 20.4f);
    }
}

ConcreteObserver1 => 온도: 80.0 습도: 65.0
ConcreteObserver2 => 온도: 80.0 습도: 65.0 기압: 30.4
ConcreteObserver1 => 온도: 60.0 습도: 75.0
ConcreteObserver2 => 온도: 60.0 습도: 75.0 기압: 16.3
ConcreteObserver1 => 온도: 40.0 습도: 35.0
ConcreteObserver2 => 온도: 40.0 습도: 35.0 기압: 20.4
```

- Object들을 생성할 때 아규먼트로 Subject를 구현한 WeatherData를 넣는다.
- 그후 WeatherData의 측정값들을 변경할 때 마다 observer들의 update 메서드가 호출되고 display메서드에 의해 출력이 되는것을 확인할 수 있다.

---
[참고 도서](http://www.yes24.com/Product/Goods/1778966)
