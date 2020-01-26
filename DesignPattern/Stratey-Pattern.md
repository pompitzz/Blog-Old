### 인터페이스

[##_Image|kage@pohHB/btqAnoMkx6Y/UDy9hcfbaBFWDAQzLbXKgK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]
- Interface를 이용하면 기능에 대한 선언과 구현이 분리 가능하다.

[##_Image|kage@dh12lh/btqAlPKRvHx/kv7ltiQKcisoLUIQsypCBK/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]
- Interface를 통해 기능의 사용 통로가 될 수 있다.

### 델리게이트
- 특정 객체에 기능을 사용하기 위해 다른 객체의 기능을 호출하는 것이다.

[##_Image|kage@bikPEg/btqAmsBFnLH/5VxN5gGacAyghN8YgYD8UK/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]

---

# 스트레티지 패턴(전략 패턴)

- 지정된 알고리즘의 세부 구현을 변경할 필요 없이 쉽게 교환할 수 있게 해주는 디자인 패턴이다.
- 스트래티지를 활용하면 알고리즘을 사용하는 클라이언트와는 독립적으로 알고리즘을 변경할 수 있다.

[##_Image|kage@JEwKg/btqAnnGFDTg/i6LhizBcZdp1imTqGEtA90/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 위의 다이어그램에 맞게 구현을 해볼 것이다.
- Clinet는 GameCharacter 이 된다.
- Strategy Interface는 Weapon이 된다.
- Stratecy Clasee 들은 Knife, Sword, Ax들이 될 것이다.

[##_Image|kage@uv6z6/btqAlZsVxAq/629qnpN78sklKN4krCYuZK/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]

- Weapon이라는 인터페이스를 구현하고 구체적인 행동을 구현하는 Sword, Ax, Knife 클래스를 만든다.
- 다형성을 통해 Weapon하나로 Sword, Ax, Knife모두를 가질수 있게 되었다.
- 즉 Weapon 하나로 모든 무기를 설정할 수 있게 된 것이다.
- GameCharacter는 멤버 변수로 Weapon을 가지고 있다.
- GameCharacter는 새로운 무기가 추가되어도 Weapon만 상속받는 다면 구현을 변경할 필요가 없다.

[##_Image|kage@ba4HpB/btqAkSnvdgJ/LVIyWplGJ5Diq7MjTkDN01/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]


- 실행 중에 무기를 바꾸고 그냥 Weapon을 상속받은 인스턴스를 set해주면 된다.
