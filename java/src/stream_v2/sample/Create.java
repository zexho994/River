package stream_v2.sample;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Zexho
 * @date 2021/8/27 8:40 下午
 */
public class Create {

    public static void main(String[] args) {
        Create.createStream();
    }

    public static void createStream() {
        // Head Stream
        Stream<Integer> head = Stream.of(1, 2, 3, 3);

        Stream<Integer> afterFilter = head.filter(e -> e != 2);
        Stream<Integer> afterDistinct = afterFilter.distinct();
        Stream<Integer> afterSort = afterDistinct.sorted();
        Stream<Integer> afterLimit = afterSort.limit(2);

//        long count = afterLimit.count();
//        afterLimit.forEach(System.out::println);
        Optional<Integer> any = afterLimit.findFirst();
//        boolean flag = afterLimit.anyMatch(e -> e == 1);
    }

}
