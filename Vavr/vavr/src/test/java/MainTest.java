import io.vavr.*;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    void tuple() throws Exception {
        Tuple2<String, Integer> hello10 = Tuple.of("Hello", 10);

        // 각 요소별로 매핑하기
        Tuple2<String, Integer> helloMap15 =
                hello10.map(
                        s -> s + " Map",
                        i -> i + 5
                );

        assertThat(helloMap15._1).isEqualTo("Hello Map");
        assertThat(helloMap15._2).isEqualTo(15);

        // 한번에 매핑하기
        Tuple2<String, Integer> helloMap15V2 =
                hello10.map((s, i) -> Tuple.of(s + " Map", i + 5));
        assertThat(helloMap15V2._1).isEqualTo("Hello Map");
        assertThat(helloMap15V2._2).isEqualTo(15);

        String hello = hello10.apply((s, i) -> s + i);
        assertThat(hello).isEqualTo("Hello10");
    }

    @Test
    void function() throws Exception {
        Function1<Integer, Integer> addOne = i -> i + 1;
        Function1<Integer, Integer> multiplyByTwo = i -> i * 2;

        // andThen은 호출하는 객체가 먼저 적용된다.
        Function1<Integer, Integer> add1AndMultiplyBy2 = addOne.andThen(multiplyByTwo);
        Integer answer = add1AndMultiplyBy2.apply(3);
        assertThat(answer).isEqualTo(8);
        assertThat(add1AndMultiplyBy2.apply(4)).isEqualTo(10);

        // compose는 넘겨주는 전달인자부터 적용된다.
        Function1<Integer, Integer> add1AndMultiplyBy2V2 = multiplyByTwo.compose(addOne);
        Integer answer2 = add1AndMultiplyBy2V2.apply(3);
        assertThat(answer2).isEqualTo(8);
        assertThat(add1AndMultiplyBy2V2.apply(4)).isEqualTo(10);
    }

    @Test
    void function2() throws Exception {
        Function2<Integer, Integer, Integer> divide = (a, b) -> a / b;
        Function2<Integer, Integer, Option<Integer>> safeDivide = Function2.lift(divide);

        Option<Integer> divide0 = safeDivide.apply(1, 0);
        Integer isNull = divide0.getOrNull();
        assertThat(isNull).isNull();
    }

    @Test
    void function3() throws Exception {
        Function4<Integer, Integer, Integer, Integer, Integer> sum
                = (a, b, c, d) -> a + b + c + d;

        // 부분 적용 함수
        Function2<Integer, Integer, Integer> sum3 = sum.apply(1, 2);
        assertThat(sum3.apply(3, 4)).isEqualTo(10);

        // 커링 함수
        Function1<Integer, Function1<Integer, Integer>> currying = sum.curried().apply(1).apply(2);
        assertThat(currying.apply(3).apply(4)).isEqualTo(10);
    }

    @Test
    void function4() throws Exception {
        Function0<Double> cachedValue = Function0.of(Math::random).memoized();
        Double random1 = cachedValue.apply();
        Double random2 = cachedValue.apply();
        assertThat(random1).isEqualTo(random2);
    }

    @Test
    void option() throws Exception {
        Optional<String> foo = Optional.of("foo");

        // Optional은 NULL이 되면 notpresent가 된다.
        Optional<String> maybeBar = foo
                .map(s -> (String) null)
                .map(s -> s.toUpperCase() + "bar");
        assertThat(maybeBar.isPresent()).isFalse();

        Option<String> foo2 = Option.of("foo");

        // Option은 NULL이여도 Some(Null)과 같이 값으로 취급하기때문에 NPE가 발생한다.
        assertThatThrownBy(() -> {
            Option<String> maybeBar2 = foo2
                    .map(s -> (String) null)
                    .map(s -> s.toUpperCase() + "bar");
        }).isInstanceOf(NullPointerException.class);

    }

    @Test
    void option_flatMap() throws Exception {
        Option<String> foo = Option.of("foo");

        Option<String> maybeBar = foo.map(s -> (String) null)
                .flatMap(s -> Option.of(s))
                .map(s -> s.toUpperCase() + "bar");

        Option<String> maybeBar2 = foo.flatMap(s -> Option.of((String) null))
                .map(s -> s.toUpperCase() + "bar");

        assertThat(maybeBar.isEmpty()).isTrue();
        assertThat(maybeBar2.isEmpty()).isTrue();
    }

    @Test
    void try_vavr() throws Exception {
        String exception_2 = Try.of(this::throwException)
                                .recover(x -> Match(x).of(
                                        Case($(instanceOf(Exception_1.class)), this::somethingWithException),
                                        Case($(instanceOf(Exception_2.class)), this::somethingWithException),
                                        Case($(instanceOf(Exception_3.class)), this::somethingWithException)
                                ))
                                .getOrElse("Else");

        assertThat(exception_2).isEqualTo(Exception_2.class.getSimpleName());
    }

    private String somethingWithException(Exception t) {
        return t.getClass().getSimpleName();
    }


    private String throwException() {
        if (true) throw new Exception_2();
        return "Str";
    }


    static class Exception_1 extends RuntimeException { }

    static class Exception_2 extends RuntimeException { }

    static class Exception_3 extends RuntimeException { }

    @Test
    void lazy() throws Exception{
        Lazy<Double> lazy = Lazy.of(Math::random);

        // 호출이 한 번도 되지 않았으니 fasle이다.
        assertThat(lazy.isEvaluated()).isFalse();
        Double first = lazy.get();

        // 호출되었으니 true가 된다.
        assertThat(lazy.isEvaluated()).isTrue();
        Double second = lazy.get();

        // 값을 캐싱하기 때문에 동일한 값을 가지고 있다.
        assertThat(first).isEqualTo(second);

    }

    @Test
    void list() throws Exception{
        int sum1 = Arrays.asList(1, 2, 3)
                         .stream()
                         .mapToInt(i -> i)
                         .sum();

        int sum2 = Arrays.asList(1, 2, 3)
                          .stream()
                          .reduce(Integer::sum)
                          .get();

        int sum3 = List.of(1, 2, 3).sum().intValue();

        assertThat(sum1).isEqualTo(sum2).isEqualTo(sum3);
    }



}