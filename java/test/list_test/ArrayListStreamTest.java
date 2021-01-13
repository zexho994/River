package list_test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ArrayListStreamTest {

    private Stream<String> stream = Stream.of("one", "two", "three", "for", "five", "six", "eight", "nine", "ten", "one", "two");

    public static void main(String[] args) {
        ArrayListStreamTest test = new ArrayListStreamTest();
        test.operations();
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


        System.out.printf("operation result => %s \n", res);
    }




}
