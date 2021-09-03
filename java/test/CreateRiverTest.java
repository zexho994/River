/**
 * @author Zexho
 * @date 2021/9/3 2:40 下午
 */
public class CreateRiverTest {

    public static void main(String[] args) {
        River<String> stringRiver = filterTest();
    }

    /**
     * 从数组中创建River
     */
    public static River<String> createFromArrayTest() {
        return River.of("1", "2", "3");
    }

    public static River<String> filterTest() {
        River<String> filter = createFromArrayTest().filter(e -> e.equals("2"));
        assert filter != null;
        return filter;
    }

}
