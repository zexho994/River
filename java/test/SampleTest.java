import java.util.stream.Stream;

/**
 * @author Zexho
 * @date 2021/9/6 9:48 上午
 */
public class SampleTest {

    public static void main(String[] args) {
        peekTest();
    }

    public void Stream() {
        long count = Stream.of(1, 2, 3, 4, 5).filter(integer -> integer > 2).count();
    }

    public static void peekTest() {
        long count = Stream.of(1, 2, 3)
                .peek(e -> {
                    assert e == 1;
                    System.out.println("peek -> " + e);
                }).count();

    }
}
