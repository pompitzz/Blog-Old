# final
- final을 사용하면 **안전한 개발이 가능하다**
- 개발자가 실수할 여지를 줄여줄 수 있다.
- 그리고 실제로 사용될 일은 없지만 String 같은거에서 메모리 이점이 존재하기도 한다.

```java
for(final int i = 0 ; i < 10; i++){
  i = 0; // final이 아니였다면 무한 루프이다.
}
```

```java
String aaa = "aaa";

String aaaa = "aaaa";

(aaa + "a") == aaaa // false;
// 이 경우 aaa는 상수가 아니다. 그러므로 aaa + "a"의 바이트코드를 확인해보면 StringBuilder를 만들고 append(aaa).append("a").toString()으로 하기 때문에 false이다.

final String aaa = "aaa";

(aaa + "a") == aaaa // true;
// 이 경우 aaa는 final 이므로 상수가 되어 바이트 코드를 확인해보면 이미 컴파일시 계산이되어 true가 나타나 있다.
// 왜냐하면 상수에 리터럴"a"를 더하므로 인터닝 풀의 값을 사용하게 되는 것이다.
```
