package stream_v2.sample;

import java.util.stream.Stream;

/**
 * @author Zexho
 * @date 2021/8/27 8:40 下午
 */
public class Create {

    public static void main(String[] args) {
        Create create = new Create();
        create.createStream();
    }

    public void createStream() {
        Stream<Integer> stream = Stream.of(1, 2, 3);
//        stream.forEach(System.out::println);

        System.out.println(stream.count());
    }

}
