import river.River;

/**
 * @author Zexho
 * @date 2021/9/9 4:09 下午
 */
public class ParallelRiverTest {
    public static void main(String[] args) {
        convertTest();
    }

    public static void convertTest() {
        River<String> parallel1 = River.of("1", "1", "2", "3", "3").parallel();
        River<String> parallel2 = River.of("1", "1", "2", "3", "3").parallel().sequential();
    }
}
