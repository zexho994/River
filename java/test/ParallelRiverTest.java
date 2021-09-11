import river.River;

/**
 * @author Zexho
 * @date 2021/9/9 4:09 下午
 */
public class ParallelRiverTest {
    public static void main(String[] args) {
        reduceTest();
    }

    public static void reduceTest() {
        System.out.print("=>test filter : ");
        Integer reduce1 = River.of(1, 2, 3, 4, 5).parallel().reduce(0, Integer::sum);
        System.out.println("reduce1 = " + reduce1);
        assert reduce1 == 15;


        Integer reduce2 = River.of(1, 2, 3, 4, 5).parallel().reduce(1, (a, b) -> a + b + 1);
        Integer reduce3 = River.of(1, 2, 3, 4, 5).sequential().reduce(1, (a, b) -> a + b + 1);
        assert reduce2 == reduce3;
        System.out.println("success");
    }
}
