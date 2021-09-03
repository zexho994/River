/**
 * @author Zexho
 * @date 2021/9/3 2:40 下午
 */
public class CreateRiverTest {


    public static void main(String[] args) {
        createFromArray();
    }

    /**
     * 从数组中创建River
     */
    public static void createFromArray() {
        River<String> river = River.of("1", "2", "3");
    }

}
