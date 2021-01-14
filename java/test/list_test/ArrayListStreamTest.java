package list_test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ArrayListStreamTest {

    private Stream<String> stream = Stream.of("one", "two", "three", "for", "five", "six", "eight", "nine", "ten", "one", "two");

    public static void main(String[] args) {
        ArrayListStreamTest test = new ArrayListStreamTest();
//        test.operations();
        test.terminal();
    }

    /**
     * 为list创建stream
     */
    public void createStream() {
        // 方法1：直接生成
        List<String> list1 = new ArrayList<>();
        Stream stream = list1.stream();
        Stream parallelStream = list1.parallelStream();

        // 方法2：构造生成
        Stream.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten");

        // 方法3：静态方法生成
        Stream stream2 = Stream.iterate(0, n -> n + 10);
        IntStream stream3 = IntStream.range(0, 100);
    }

    // the all of intermediate operations are lazy,and as a result,no operations will have any effect until the pipeline starts to work
    public void operations() {
        // 1.distinct: 保证输出的流中包含唯一的元素，通过Object.equals(Object) 来检查是否包含相同的元素
//        List<String> res = this.stream.distinct().collect(Collectors.toList());

        // 2.filter: 过滤掉不满足约束的数据
//        List<String> res = this.stream.filter(s -> s.equals("six")).collect(Collectors.toList());
        List<Integer> istream = IntStream.range(1, 20).filter(i -> i * 2 < 10).boxed().collect(Collectors.toList());
        System.out.printf("operation result => %s \n", istream);

        //3. map: 将流中元素进行映射，类型也可以不同
        List<Integer> res = this.stream.map(String::hashCode).collect(Collectors.toList());

        //4. flatmap: 混合流map + flattern  的功能，将映射后的流的元素全部放在一个流中
        String poetry = "Where, before me, are the ages that have gone?\n" +
                "And where, behind me, are the coming generations?\n" +
                "I think of heaven and earth, without limit, without end,\n" +
                "And I am all alone and my tears fall down.";

        Stream<String> lines = Arrays.stream(poetry.split("\n"));
//        Stream<String> word1 = lines.flatMap(line -> Arrays.stream(line.split(" ")));  // 根据空格获取单词
//        Stream<String> word2 = lines.flatMap(dot -> Arrays.stream(dot.split(",")));    // 根据逗号获取句子
//        lines.flatMap(dot -> Arrays.stream(dot.split(","))).map(str -> str.trim() + str.length()).forEach(str -> System.out.printf("%s \n", str));  //获取每个句子的长度

        //5.limit: 限制返回数量，注意对于有序的并行流，性能不好，如果不在意有序，可以转化成无需
//        lines.map(str -> str + str.length()).limit(2).forEach(System.out::println);

        //6.sorted: 排序
//        lines.sorted(Comparator.comparingInt(String::length)).forEach(System.out::println);

        //7.skip: 丢弃前n个元素
//        lines.sorted(Comparator.comparingInt(String::length)).skip(3).forEach(System.out::println);
//        System.out.printf("operation result => %s \n", res);

        //8.peek,the exists mainly to support debugging,where you want to see the elements as the flow past a certain point in a pipeline
//        lines.flatMap(words -> Stream.of(words.split(" "))).peek(e -> System.out.printf("before: %s \n", e)).filter(word -> word.contains("e")).forEach(e -> System.out.printf("after : %s \n", e));

    }

    public void terminal() {
        String poetry = "Where, before me, are the ages that have gone?\n" +
                "And where, behind me, are the coming generations?\n" +
                "I think of heaven and earth, without limit, without end,\n" +
                "And I am all alone and my tears fall down.";
        Stream<String> lines = Arrays.stream(poetry.split("\n"));

        //1. Match: 条件判断
        //1.1 AllMatch:所有元素都要符合条件
//        System.out.println(lines.flatMap(line -> Stream.of(line.split(" ")))
//                .allMatch(line -> line.length() < 20));

        //1.2 anyMatch: 有一个符合要求就返回true
//        System.out.println(lines.flatMap(line -> Stream.of(line.split(" ")))
//                .anyMatch(word -> word.equals("heaven")));


        //1.3 noneMatch: 需要所有的元素都不满足要求
//        lines.flatMap(
//                line -> Stream.of(line.split(" ")).map(nl -> nl.replaceAll("[,?]", ""))
//        ).filter(line -> line.contains("e")).forEach(System.out::println); // 打印所有包含e的单词

//        System.out.println(lines.flatMap(line -> Stream.of(line.split("\n"))).noneMatch(line -> !line.contains("e"))); // 所有句子是否都包含 e

        // 计算字母e的个数
        System.out.println(lines.flatMap(line -> line.chars().mapToObj(c -> (char) c).collect(Collectors.toList()).stream())
                        .filter(c -> c == 'e')
                        .count());

//        Character[] ch = new Character[]{'a', 'b', 'c', 'd', 'e', 'f'};
//        List<Character> chars = Arrays.asList(ch);
//        Stream s = Stream.of(chars);
//        s.forEach(System.out::println);

    }

}
