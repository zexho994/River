import river.River;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Zexho
 * @date 2021/9/3 2:40 下午
 */
public class RiverTest {

    public static void main(String[] args) {
        createFromListTest();
        countTest();
        filterTest();
        distinctTest();
        limitTest();
        sortTest();
        peekTest();
        skipTest();
        mapTest();
        toArrayTest();
        reduceTest();
        collectionTest();
        minTest();
        maxTest();
        anyMatch();
        allMatch();
        noneMatch();
        findFirstTest();
    }

    /**
     * 从数组中创建River
     */
    public static River<String> createFromArrayTest() {
        return River.of("1", "1", "2", "3", "3");
    }

    public static void createFromListTest() {
        List<String> strList = Arrays.asList("1", "2", "3");
        assert River.of(strList).count() == 3;
        assert River.of(strList).findFirst().get().equals("1");
    }

    public static void filterTest() {
        System.out.print("=>test filter : ");
        assert createFromArrayTest().filter(e -> !e.equals("2")).count() == 4;
        assert createFromArrayTest().filter(e -> e.equals("2")).count() == 1;
        System.out.println("success");
    }

    public static void distinctTest() {
        System.out.print("=>test distinct : ");
        assert River.of("1", "1", "2", "3", "3").distinct().count() == 3;
        System.out.println("success");
    }

    public static void countTest() {
        System.out.print("=>test count : ");
        assert River.of("java", "c++", "go", "python", "c", "java")
                .distinct()
                .filter(e -> !e.equals("go") && !e.equals("c++"))
                .count() == 3;
        System.out.println("success");
    }

    public static void limitTest() {
        System.out.print("=>test limit : ");
        assert River.of("1", "2", "3", "4", "5", "2", "1", "2", "1", "2", "1", "2")
                .limit(5)
                .count() == 5;
    }

    public static void sortTest() {
        System.out.print("=>test sort : ");
        assert River.of(2, 1, 5, 4, 0)
                .sort((n1, n2) -> {
                    if (Objects.equals(n1, n2)) {
                        return 0;
                    } else if (n1 > n2) {
                        return 1;
                    } else {
                        return -1;
                    }
                }).findFirst().get() == 0;

        assert River.of(2, -1, 5, 4, 0)
                .sort((n1, n2) -> {
                    if (Objects.equals(n1, n2)) {
                        return 0;
                    } else if (n1 > n2) {
                        return 1;
                    } else {
                        return -1;
                    }
                }).findFirst().get() == -1;
        System.out.println("success");
    }

    public static void peekTest() {
        System.out.print("=>test peek :");
        assert River.of(2, 1, 3, 4, 4, 0, 5)
                .distinct()
                .sort(Integer::compare)
                .peek(e -> System.out.println("peek: " + e))
                .count() == 6;
        System.out.println("success");
    }

    public static void skipTest() {
        System.out.print("=>test peek :");
        assert River.of(1, 2, 3, 4, 5)
                .skip(2)
                .peek(e -> System.out.println("peek: " + e))
                .count() == 3;
        System.out.println("success");
    }

    public static void mapTest() {
        System.out.print("=>test map :");
        assert River.of(1, 2, 3, 4, 5)
                .skip(2)
                .peek(e -> System.out.println("before map: " + e))
                .map(e -> e * 10)
                .peek(e -> System.out.println("after map:" + e))
                .count() == 3;
        System.out.println("success");
    }

    public static void toArrayTest() {
        System.out.print("=>test toArray :");
        String[] arr = new String[5];
        River.of(1, 2, 3, 4, 5)
                .map(e -> "arr : " + e)
                .toArray(arr);
        Object[] arr1 = River.of(1, 2, 3, 4, 5)
                .map(e -> "arr1 : " + e)
                .toArray();
        assert arr1 != null;
        assert arr1.length == 5;
        System.out.println("success");
    }

    public static void reduceTest() {
        System.out.print("=>test reduce : ");
        Integer reduce = River.of(1, 2, 3, 4, 5)
                .reduce(0, (n1, n2) -> n1 + n2 + 10);
        assert reduce == 65;

        String reduce1 = River.of("A", "B", "C", "D", "E")
                .reduce("start", (n1, n2) -> n1 + "-" + n2);
        assert reduce1.equals("start-A-B-C-D-E");
        System.out.println("success");
    }

    public static void collectionTest() {
        System.out.print("=>test collection :");
        List<String> collect = River.of("A", "B", "C", "D", "E")
                .map(e -> e + ".")
                .collect(Collectors.toList());
        assert collect != null;
        assert collect.size() == 5;
        assert collect.get(2).equals("C.");

        List<Integer> collect2 = River.of("1", "2", "3", "4", "5")
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        assert collect2 != null;
        assert collect2.size() == 5;
        assert collect2.get(2) == 3;
        for (int i = 0; i < 5; i++) {
            assert collect2.get(i) == i + 1;
        }

        Set<Integer> collect1 = River.of(1, 2, 2, 4, 5)
                .collect(Collectors.toSet());
        assert collect1 != null;
        assert collect1.size() == 4;
        System.out.println("success");
    }

    public static void minTest() {
        System.out.print("=>test min : ");
        Optional<Integer> min = River.of(1, 2, 2, 4, 0, 5, -2)
                .min((a, b) -> {
                    if (a >= b) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
        assert min.isPresent();
        Integer minVal = min.get();
        assert minVal == -2;
        System.out.println("success");
    }

    public static void maxTest() {
        System.out.print("=>test max : ");
        Optional<Integer> max = River.of(1, 2, 2, 4, 0, 5, -2)
                .max((a, b) -> {
                    if (a >= b) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
        assert max.isPresent();
        Integer maxVal = max.get();
        assert maxVal == 5;
        System.out.println("success");
    }

    public static void anyMatch() {
        System.out.print("=>test anyMatch : ");
        boolean b1 = River.of(1, 2, 2, 4, 0, 5)
                .anyMatch(e -> e == 5);
        assert b1;
        boolean b2 = River.of(1, 2, 2, 4, 0, 5)
                .anyMatch(e -> e == 3);
        assert !b2;
        System.out.println("success");
    }

    public static void allMatch() {
        System.out.print("=>test allMatch : ");
        boolean b1 = River.of(1, 2, 2, 4, 0, 5)
                .allMatch(e -> e <= 5);
        assert b1;
        boolean b2 = River.of(1, 2, 2, 4, 0, 5)
                .anyMatch(e -> e > 5);
        assert !b2;
        boolean b3 = River.of(1, 2, 2, 4, 0, 5)
                .anyMatch(e -> e > 0 && e < 5.1);
        assert b3;
        System.out.println("success");
    }

    public static void noneMatch() {
        System.out.print("=>test noneMatch : ");
        boolean b1 = River.of(1, 2, 2, 4, 0, 5)
                .noneMatch(e -> e == 3);
        assert b1;
        boolean b2 = River.of(1, 2, 2, 4, 0, 5)
                .noneMatch(e -> e > 6);
        assert b2;
        boolean b3 = River.of(1, 2, 2, 4, 0, 5)
                .noneMatch(e -> e == 4);
        assert !b3;
        System.out.println("success");
    }

    public static void findFirstTest() {
        System.out.print("=>test findFirst : ");
        Optional<String> first = River.of("one", "two")
                .findFirst();
        assert first.isPresent();
        assert first.get().equals("one");

        Optional<Integer> first1 = River.of(-1, 2, 2, 4, 0, 5)
                .findFirst();
        assert first1.isPresent();
        assert first1.get().equals(-1);

        Optional<Object> first2 = River.of()
                .findFirst();
        assert !first2.isPresent();

        Optional<String> first3 = River.of(-1, 2, 2, 4, 0, 5)
                .map(e -> e + "!")
                .findFirst();
        assert first3.isPresent();
        assert first3.get().equals("-1!");

        System.out.println("success");
    }

}
