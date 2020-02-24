# 재사용성과 다이나믹 디스패치, 더블 디스패치

```java
import java.util.List;

public class Dispatch {
    interface Post{
        void postOn(SNS sns);
    }

    static class Text implements Post{

        @Override
        public void postOn(SNS sns) {
            System.out.println(sns.getClass().getSimpleName() + " Text Post");
        }
    }

    static class Picture implements Post{

        @Override
        public void postOn(SNS sns) {
            System.out.println(sns.getClass().getSimpleName() + " Picture Post");
        }
    }

    interface SNS{

    }

    static class Facebook implements SNS{

    }

    static class Instagram implements SNS{

    }

    public static void main(String[] args) {
        List<SNS> snsList = List.of(new Facebook(), new Instagram());
        List<Post> postList = List.of(new Text(), new Picture());

        postList.forEach(p -> snsList.forEach(s -> p.postOn(s)));

    }
}
```
- 이 상황에서 각 sns마다 postOn을 달리하고 싶을 때는 어떻게 해야할까요?
- Post에서 instanceof를 이용할 수 있을 것입니다.
- 하지만 이 방법은 유연하지 않고 확장성이 뛰어나지 않습니다.

```java
interface Post{
    void postOn(Facebook sns);
    void postOn(Instagram sns);
}

static class Text implements Post{

    @Override
    public void postOn(Facebook sns) {
        System.out.println("Facebook" + " Text Post");
    }

    @Override
    public void postOn(Instagram sns) {
        System.out.println("Facebook" + " Text Post");
    }
}

static class Picture implements Post{
    @Override
    public void postOn(Facebook sns) {
        System.out.println("Instagram" + " Picture Post");
    }

    @Override
    public void postOn(Instagram sns) {
        System.out.println("Instagram" + " Picture Post");
    }
}
```
- instanceof를 이용하지 않고 좀 더 유연하게 사용해보려고 이렇게 정의하여 봅시다.
- 각 sns별로 기능을 구현하여 유연해보이지만 사실 main 메서드를 확인해보면 컴파일 에러가 나타납니다.

```java
public static void main(String[] args) {
    List<SNS> snsList = List.of(new Facebook(), new Instagram());
    List<Post> postList = List.of(new Text(), new Picture());

    postList.forEach(p -> snsList.forEach(s -> p.postOn(s)));

}
```
- 왜냐하면 여기서 넘기는 s는 SNS타입입니다.
- 허나 Post에서는 SNS가 아닌 해당 하위 타입들만 받는 메서드만 정의되어 있기때문에 컴파일조차 되지 않습니다.
- 이럴때 더블 디스패치를 활용하면 객체지향적으로 설계가 가능합니다.

```java
import java.util.List;

public class Dispatch {
    interface Post{
        void postOn(SNS sns);
    }

    static class Text implements Post{

        @Override
        public void postOn(SNS sns) {
            sns.post(this);
        }
    }

    static class Picture implements Post{

        @Override
        public void postOn(SNS sns) {
            sns.post(this);
        }
    }

    interface SNS{
        void post(Text post);

        void post(Picture post);
    }

    static class Facebook implements SNS{

        @Override
        public void post(Text post) {
            System.out.println("Facebook Text Post");
        }

        @Override
        public void post(Picture post) {
            System.out.println("Facebook Picture Post");
        }
    }

    static class Instagram implements SNS{

        @Override
        public void post(Text post) {
            System.out.println("Instagram Text Post");
        }

        @Override
        public void post(Picture post) {
            System.out.println("Instagram Picture Post");
        }
    }

    public static void main(String[] args) {
        List<SNS> snsList = List.of(new Facebook(), new Instagram());
        List<Post> postList = List.of(new Text(), new Picture());

        postList.forEach(p -> snsList.forEach(s -> p.postOn(s)));

    }
}
```
- postOn에서 한 번더 각 sns의 post를 호출합니다.
- 이를통해 처음 Post에게 dispatch하고 그 후에 Sns에게 dispatch 하게돕니다..
- 이렇게하면 컴파일 에러가 나지 않고 정상적으로 동작합니다..
- 만약 여기서 트위터 클래스가 추가되었다고 가정 해봅시다..
- 트위터 클래스가 추가된다면 우리는 SNS를 상속받아 구현하기만 한다면 Post의 클래스는 변경할 필요가 없게됩니다.
- 이를 통해 유연하고 객체지향적인 설계를 할 수 있습니다.
