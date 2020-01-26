# 커맨드 패턴
> 커맨드 패턴을 쓰면 어떤 작업을 요청한 쪽과 그 작업을 처리한 쪽을 분리시킬 수 있다.

- 요구 사항을 객체로 **캡슐화할 수 있으며**, 매개변수를 써서 **여러 가지 다른 요구 사항을 집어넣을 수도 있다.**
- 요청 내역을 큐에 저장하거나 로그로 기록할 수도 있으며 작업 취소 기능도 지원 가능하다.

[##_Image|kage@o1OKc/btqAuygtfvs/6kpFnpf4AyPqtaPd5ZKWb0/img.png|alignCenter|width="693" data-origin-width="0" data-origin-height="0"|||_##]

#### Client
- ConcreteCommand를 생성하고 Receiver를 설정한다.

#### Receiver
- 요구 사항을 수행하기 위해 어떤 일을 처리해야 하는지 알고 있는 객체이다.
- ConcreteCommand에서 Receiver를 가지고 있으며 ConcreteCommand는 명령에 따라 Receiver의 기능을 수행한다.

#### ConcreteCommand
- 특정 행동과 리시버 사이를 연결해 준다.
- Invoker에서 excute(), undo() 등 호출을 통해 요청이 들어오면 ConcreteCommand에 있는 리시버 객체의 메서드를 호출해 작업을 처리하게 된다.

#### Invoker
- 명령이 들어 있으며 excute(), undo() 등 메서드를 호출함으로써 커맨드 객체에게 특정 작업을 수행해달라는 요구를 하게 된다.

#### Command
- 모든 커맨드 객체에서 구현해야 하는 인터페이스이며 리시버에 특정 작업을 처리하라는 인 보커의 지시를 전달한다.

---

### 예제
- 집에서 블루투스 스피커와 조명을 키고 끌 수 있는 리모컨이 있다고 생각해보자.

#### Receiver
```java
public class BlueToothSpeaker {

    private String location;

    public BlueToothSpeaker(String location) {
        this.location = location;
    }

    public void on() {
        System.out.println(location + "의 블루투스 스피커가 켜졌습니다.");
    }

    public void connectWithPhone() {
        System.out.println("휴대폰과 연결 합니다.");
    }

    public void playingMusic() {
        System.out.println("음악을 킵니다.");
    }

    public void off() {
        System.out.println(location + "의 블루투스 스피커를 꺼졌습니다.");
    }
}

public class Light {

    private String location;

    public Light(String feature) {
        this.location = feature;
    }

    public void on() {
        System.out.println(location + "의 전등이 켜졌습니다.");
    }

    public void off() {
        System.out.println(location + "의 전등이 꺼졌습니다.");
    }
}
```
- 조명과 블루투스 스피커 클래스가 Receiver가 된다.
- 즉 요구 사항을 수행하기 위해 처리해야 할 일을 가지고 있다.

#### Command
```java
public interface Command {

    public void execute();

    public void undo();

}
```
- Command Interface를 만들어 명령에 필요한 책임들을 설정한다.
- 명령을 실행하는 excute()와 이전 명령을 다시 실행하는 undo()가 있다.

#### ConcreteCommand

```java
public class SpeakerOnCommand implements Command {

    private BlueToothSpeaker blueToothSpeaker;

    public SpeakerOnCommand(BlueToothSpeaker blueToothSpeaker) {
        this.blueToothSpeaker = blueToothSpeaker;
    }

    @Override
    public void execute() {
        blueToothSpeaker.on();
        blueToothSpeaker.connectWithPhone();
        blueToothSpeaker.playingMusic();
    }

    @Override
    public void undo() {
        blueToothSpeaker.off();
    }
}

public class SpeakerOffCommand implements Command {

    private BlueToothSpeaker blueToothSpeaker;

    public SpeakerOffCommand(BlueToothSpeaker blueToothSpeaker) {
        this.blueToothSpeaker = blueToothSpeaker;
    }

    @Override
    public void execute() {
        blueToothSpeaker.off();
    }

    @Override
    public void undo() {
        blueToothSpeaker.on();
        blueToothSpeaker.connectWithPhone();
        blueToothSpeaker.playingMusic();
    }
}
```
- ConcreteCommand중 하나인 SpeackerCommand들 이다.
- BlueToothSpeaker를 가지고 있고 명령에 맞게 기능을 작동시킨다.
- onCommand는 excute()에 스피커를 킬 것이도 undo일 때 스피커를 끈다.
- offCommand는 onCommand의 반대이다.

<br>


```java
public class LightOnCommand implements Command {

    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.on();
    }

    @Override
    public void undo() {
        light.off();
    }
}

public class LightOffCommand implements Command {
    private Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.off();
    }

    @Override
    public void undo() {
        light.on();
    }
}
```
- ConcreteCommand중 하나인 LightCommand들 이다.
- Light를 가지고 있고 명령에 맞게 기능을 수행한다.

<br>

```java
public class MacroCommand implements Command {
    private Command[] commands;

    public MacroCommand(Command... commands) {
        this.commands = commands;
    }

    @Override
    public void execute() {
        for (Command command : commands) {
            command.execute();
        }
    }

    @Override
    public void undo() {
        for (Command command : commands) {
            command.undo();
        }
    }
}
```
- **매크로 커맨드도** 생성하였다.
- 매크로 커맨드는 클라이언트가 원하는 커맨드들을 설정하면 연속으로 알아서 실행해주는 기능으로 만들었다.

### Invoker
```java
public class RemoteControl {

    private Command[] onCommands;
    private Command[] offCommands;
    private Stack<Command> undoCommands;

    public RemoteControl() {
        onCommands = new Command[3];
        offCommands = new Command[3];

        Command noCommand = new NoCommand();

        for (int i = 0; i < onCommands.length; i++) {
            onCommands[i] = noCommand;
            offCommands[i] = noCommand;
        }

        undoCommands = new Stack<>();
    }

    public void setCommand(int slot, Command onCommand, Command offCommand) {
        onCommands[slot] = onCommand;
        offCommands[slot] = offCommand;
    }

    public void onButtonClick(int slot) {
        System.out.println("---------------- ON ----------------");
        onCommands[slot].execute();
        undoCommands.push(onCommands[slot]);
        System.out.println();
    }

    public void offButtonClick(int slot) {
        System.out.println("---------------- OFF ----------------");
        offCommands[slot].execute();
        undoCommands.push(offCommands[slot]);
        System.out.println();
    }

    public void undoButtonClick() {

        try {
            undoCommands.pop().undo();
        } catch (EmptyStackException ignored) {
        }
    }
}
```
- 마지막으로 Invoker인 RemoteControl이다.
- on, offCommand들을 배열로 가지고 있다.
- 각 배열 인덱스에 맞게 명령들을 담은 후 클라이언트는 그 인덱스를 통해 기능을 작동 시킨다.
- on, offButtonClick을 통해 slot을 지정하면 그 slot에 맞는 index command를 실행 시킬 것이다.
- undoCommand는 스택으로 구현하여 마지막에 누른 버튼부터 undo가 수행 가능하게 하였다.
- noCommand는 아무 기능이 없으며 사용자가 버튼에 기능을 넣기 전에 버튼을 눌러도 아무 작동을 하지 않기 위함으로 초기에는 noCommand로 명령을 초기화한다.

### 테스트
```java
public class RemoteControlTest {
    public static void main(String[] args) {

        RemoteControl control = new RemoteControl();

        Light roomLight = new Light("안방");
        BlueToothSpeaker blueToothSpeaker = new BlueToothSpeaker("거실");

        LightOnCommand lightOnCommand = new LightOnCommand(roomLight);
        LightOffCommand lightOffCommand = new LightOffCommand(roomLight);

        SpeakerOnCommand speakerOnCommand = new SpeakerOnCommand(blueToothSpeaker);
        SpeakerOffCommand speakerOffCommand = new SpeakerOffCommand(blueToothSpeaker);

        MacroCommand macroOnCommand = new MacroCommand(lightOnCommand, speakerOnCommand);
        MacroCommand macroOffCommand = new MacroCommand(lightOffCommand, speakerOffCommand);

        control.setCommand(0, lightOnCommand, lightOffCommand);
        control.setCommand(1, speakerOnCommand, speakerOffCommand);
        control.setCommand(2, macroOnCommand, macroOffCommand);

        control.onButtonClick(0);
        control.onButtonClick(1);
        control.offButtonClick(2);

        control.undoButtonClick();
    }
}
```
- 우선 Invoker인 RemoteControl를 생성한다.
- 그리고 각 Receiver인 조명과 스피커를 생성한다.
- 생성한 Receiver들을 통해 각 명령을 수행한다.
- MacroCommand는 조명과 스피커의 명령을 동시에 수행할 것이다.
- 그리고 그렇게 생성한 Command들을 RemoteControl에 slot번호와 함께 세팅해준다.

```java
---------------- ON ----------------
안방의 전등이 켜졌습니다.

---------------- ON ----------------
거실의 블루투스 스피커가 켜졌습니다.
휴대폰과 연결 합니다.
음악을 킵니다.

---------------- OFF ----------------
안방의 전등이 꺼졌습니다.
거실의 블루투스 스피커를 꺼졌습니다.

---------------- UNDO ----------------
안방의 전등이 켜졌습니다.
거실의 블루투스 스피커가 켜졌습니다.
휴대폰과 연결 합니다.
음악을 킵니다.

```
- onButtonClick(0)을 통해 조명을 켰다.
- onButtonClick(1)을 통해 블루투스를 켰다.
- offButtonClick(2)을 통해 매크로를 실행하여 조명과 블루투스를 껏다.
- undoButtonClick으로 이전에 실행한 매크로명령의 undo를 실행하여 다시 조명과 블루투스를 킨다.

> 커맨드 패턴을 통해 클라이언트는 구체적인 구현사항을 알 필요가 없어졌다.
> 단지 명령어들을 세팅하기만 하고 버튼을 조작하기만 하면된다.

---
[참고 도서](http://www.yes24.com/Product/Goods/1778966)
