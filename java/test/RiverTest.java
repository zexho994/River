import river.River;

import java.util.Objects;

/**
 * @author Zexho
 * @date 2021/9/3 2:40 下午
 */
public class RiverTest {

    public static void main(String[] args) {
        countTest();
        filterTest();
        distinctTest();
        limitTest();
        sortTest();
        peekTest();
        skipTest();
    }

    /**
     * 从数组中创建River
     */
    public static River<String> createFromArrayTest() {
        return River.of("1", "1", "2", "3", "3");
    }

    public static void filterTest() {
        System.out.println("filter test:");
        System.out.println("==> !equals(2)");
        createFromArrayTest().filter(e -> !e.equals("2")).forEach(System.out::println);
        System.out.println("==> equals(2)");
        createFromArrayTest().filter(e -> e.equals("2")).forEach(System.out::println);
    }

    public static void distinctTest() {
        System.out.println("distinct test");
        River.of("1", "1", "2", "3", "3").distinct().forEach(System.out::println);
    }

    public static void countTest() {
        long count2 = River.of("java", "c++", "go", "python", "c", "java")
                .distinct()
                .filter(e -> !e.equals("go") && !e.equals("c++"))
                .count();
        assert count2 == 3;
    }

    public static void limitTest() {
        long count = River.of("1", "2", "3", "4", "5", "2", "1", "2", "1", "2", "1", "2")
                .limit(5)
                .count();
        assert count == 5 : "limit test fail,count = " + count;

        System.out.println("limit test:");
        River.of("1", "2", "3", "4", "5", "2", "1", "2", "1", "2", "1", "2")
                .limit(5)
                .forEach(System.out::println);
    }

    public static void sortTest() {
        System.out.println("sorted test:");
        River.of(2, 1, 5, 4, 0)
                .sort((n1, n2) -> {
                    if (Objects.equals(n1, n2)) {
                        return 0;
                    } else if (n1 > n2) {
                        return 1;
                    } else {
                        return -1;
                    }
                })
                .forEach(System.out::println);
    }

    public static void peekTest() {
        System.out.println("peek test:");
        long count = River.of(2, 1, 3, 4, 4, 0, 5)
                .distinct()
                .sort(Integer::compare)
                .peek(e -> System.out.println("peek: " + e))
                .count();
        assert count == 6;
    }

    public static void skipTest() {
        System.out.println("skip test:");
        long count = River.of(1, 2, 3, 4, 5)
                .skip(2)
                .peek(e -> System.out.println("peek: " + e))
                .count();
        assert count == 3;
    }

    public static void mapTest() {
        System.out.println("map test:");
        long count = River.of(1, 2, 3, 4, 5)
                .skip(2)
                .peek(e -> System.out.println("peek: " + e))
                .map(e -> e * 10)
                .count();
    }

}
