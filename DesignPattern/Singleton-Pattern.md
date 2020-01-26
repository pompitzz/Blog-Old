# **싱글톤 패턴**
>해당 클래스의 인스턴스가 하나만 만들어지고, 어디서든지 그 인스턴스에 접근할 수 있게 한 패턴

### 심플한 싱글톤 패턴

[##_Image|kage@djZkAx/btqAm3vWZgZ/LL3TiqTEyCTY79xsuZYuP0/img.png|alignCenter|width="693" data-origin-width="0" data-origin-height="0"|||_##]

- 가장 심플한 싱글톤 패턴 구현 방법이다.
- 인스턴스는 오직 static 메서드인 getInstance로만 만들 수 있다.
- 만약 singletonInstance가 null이면 그 때 인스턴스를 한 번 생성하여 반환한다.
- 그 이외에는 전부 이미 생성된 인스턴스를 반환한다.

#### 문제점

- 위의 코드대로 싱글톤을 만들면 **멀티 스레드 환경에서** 문제가 생길수 있다.
- 아직 인스턴스가 만들어지지 않은 상태에서 1번 스레드에서 getInstance를 호출한다고 가정하자.
- 1번 스레드가 if문에 들어가 singletonInstance를 생성하기 전에 2번 스레드가 getInstance를 호출하였다.
- 그렇게 되면 아직 singletonInstance가 생성되지 않았으므로 2번 스레드에서도 singletonInstance를 생성할 것이다.
- 그러므로 단 한개의 인스턴스가 만들어 지지 않게된다.

---

### 해결법

#### 첫번째 방법

[##_Image|kage@dev6zy/btqApj5cxyL/osAJjvrTQnadPzOiK4yf70/img.png|alignCenter|width="693" data-origin-width="0" data-origin-height="0"|||_##]

- synchronized를 이용하여 스레드가 다른 스레드 메서드 사용을 끝낼 때까지 기다리게 하는 방법이다.
- 하지만 처음 인스턴스를 생성할 때만 필요하므로 인스턴스가 생성된 이후에는 필요 없게되므로 비효율적인 메모리사용을 초래할 수 있다.

### 두번째 방법

[##_Image|kage@yQOMd/btqApjxmcw7/O5Rskwz5M6pfiNUOSlTfD1/img.png|alignCenter|width="693" data-origin-width="0" data-origin-height="0"|||_##]

- DCL(Double-Checking Locking)을 이용하여 인스턴스가 없을 때만 동기화를 시킬 수도 있다.
- 조금 복잡하지만 속도가 문제가 될 때 이렇게 구현하면 된다.


### 세번째 방법

[##_Image|kage@dbllnk/btqAnnucwJK/t41BfTQDia09IOLUEOfk80/img.png|alignCenter|width="693" data-origin-width="0" data-origin-height="0"|||_##]

- 애초에 정적 초기화 부분에서 객체를 생성하여 인스턴스를 생성하면 된다.

---
[참고 도서](http://www.yes24.com/Product/Goods/1778966)
