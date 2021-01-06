package list_test;

import javafx.print.Printer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ArrayListStreamTest {

    /**
     * 为list创建stream
     */
    public void createStream() {
        // 方法1：直接生成
        List<String> list1 = new ArrayList<>();
        Stream stream = list1.stream();
        Stream parallelStream = list1.parallelStream();

        // 方法2：构造生成
        Arrays.asList("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten").stream();

        // 方法3：静态方法生成
        Stream stream2 = Stream.iterate(0, n -> n + 10);
        IntStream stream3 = IntStream.range(0, 100);
        String[] strArray = new String[]{};
        Stream stream4 = Stream.of(strArray);
        Stream stream5 = Stream.of(new String[]{"one", "two", "three"});

    }

    public static int c() {
        return 0;
    }




}
