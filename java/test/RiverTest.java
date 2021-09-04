import river.River;

/**
 * @author Zexho
 * @date 2021/9/3 2:40 下午
 */
public class RiverTest {

    public static void main(String[] args) {
        forEachTest();
    }

    /**
     * 从数组中创建River
     */
    public static River<String> createFromArrayTest() {
        return River.of("1","1", "2", "3","3");
    }

    public static River<String> filterTest() {
        River<String> filter = createFromArrayTest().filter(e -> !e.equals("2"));
        assert filter != null;
        return filter;
    }

    public static River<String> distinctTest() {
        River<String> distinct = filterTest().distinct();
        assert distinct != null;
        return distinct;
    }

    public static void forEachTest() {
        distinctTest().forEach(System.out::println);
    }

}