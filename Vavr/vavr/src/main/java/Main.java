import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Queue;
import io.vavr.control.Option;

public class Main {
    public static void main(String[] args) {
        Tuple2<String, Integer> hello10 = Tuple.of("Hello", 10);
        String hello = hello10._1;
        Integer ten = hello10._2;


    }
}
