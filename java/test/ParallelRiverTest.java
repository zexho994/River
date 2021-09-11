import java.util.Objects;
import java.util.stream.Stream;

import static river.River.of;

/**
 * @author Zexho
 * @date 2021/9/9 4:09 下午
 */
public class ParallelRiverTest {
    public static void main(String[] args) {
        reduceTest();
        countTest();
        ;
    }

    public static void reduceTest() {
        System.out.print("=>test filter : ");
        Integer reduce0 = of(1, 2, 3, 4, 5).parallel().reduce(0, Integer::sum);
        Integer reduce1 = Stream.of(1, 2, 3, 4, 5).parallel().reduce(0, Integer::sum);
        assert Objects.equals(reduce0, reduce1);

        Integer reduce2 = of(1, 2, 3, 4, 5).parallel().reduce(1, (a, b) -> a + b + 1);
        Integer reduce3 = Stream.of(1, 2, 3, 4, 5).parallel().reduce(1, (a, b) -> a + b + 1);
        assert reduce2.equals(reduce3);

        Integer reduce4 = of(1, 2, 3, 4, 5, 10, 39, 38, 20, 57, 19, 38, 3, 981).parallel().reduce(0, (a, b) -> ((a * 2) + b));
        Integer reduce5 = Stream.of(1, 2, 3, 4, 5, 10, 39, 38, 20, 57, 19, 38, 3, 981).parallel().reduce(0, (a, b) -> ((a * 2) + b));
        assert reduce4.equals(reduce5);


        String a1 = of("1", "2", "3").parallel().reduce("a", (a, b) -> a + b);
        String a2 = Stream.of("1", "2", "3").parallel().reduce("a", (a, b) -> a + b);
        assert a1.equals(a2);

        Integer res1 = of(1, 2, 3, 5)
                .reduce(0, (a, b) -> {
                    try {
                        System.out.println("io耗时操作,thread = " + Thread.currentThread().getName());
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return a + b;
                });
        Integer res2 = of(1, 2, 3, 5)
                .parallel()
                .reduce(0, (a, b) -> {
                    try {
                        System.out.println("io耗时操作,thread = " + Thread.currentThread().getName());
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return a + b;
                });
        assert res1 == res2;
        System.out.println("success");
    }

    public static void countTest() {
        System.out.print("=>test count : ");
        long count1 = Stream.of(1, 2, 3, 4, 5, 10, 39, 38, 20, 57, 19, 38, 3, 981).parallel().count();
        long count = of(1, 2, 3, 4, 5, 10, 39, 38, 20, 57, 19, 38, 3, 981).parallel().count();
        assert count == count1;


        long n1 = of("1", "2", "3").parallel().count();
        long n2 = Stream.of("1", "2", "3").parallel().count();
        assert n1 == n2;
        System.out.println("success");
    }

}
