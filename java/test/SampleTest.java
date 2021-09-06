import java.util.stream.Stream;

/**
 * @author Zexho
 * @date 2021/9/6 9:48 上午
 */
public class SampleTest {
    public void Stream() {
        long count = Stream.of(1, 2, 3, 4, 5).filter(integer -> integer > 2).count();
    }
}
