package lambda;

/**
 * @author Zexho
 * @date 2021/8/28 11:39 上午
 */
public class Create {

    public static void main(String[] args) {
        final String[] array = {"1", "2"};

        int x = 10;
        Foo1 f1 = (n) -> x + n + 1;

        int r1 = f1.to(1);
        System.out.println("r1:" + r1);
    }

    @FunctionalInterface
    interface Foo1 {
        int to(int n);
    }

}
