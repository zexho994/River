import java.util.stream.Stream;

/**
 * @author Zexho
 * @date 2021/9/6 9:48 上午
 */
public class StreamSample {

    public static void main(String[] args) {
//        parallelStreamTest();
        parallelStreamTest2();
    }

    public static void parallelStreamTest() {
        long count = Stream.of(1, 2, 3, 4, 5).parallel().filter(integer -> integer > 2).count();
        assert count == 5;
    }

    public static void parallelStreamTest2() {
        long count = Stream.of(1, 2, 3, 4, 5).parallel().reduce((x, y) -> x + y + 10).get();
        assert count == 55;
    }

}
