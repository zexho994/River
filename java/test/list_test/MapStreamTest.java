package list_test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Zexho
 * @date 2021/1/14 7:37 下午
 */
public class MapStreamTest {
    private static Stream entryStream;
    private static Stream keyStream;
    private static Stream valStream;

    public static void main(String[] args) {
        create();
        intermediate();
    }

    public static void create() {
        Map<String, Integer> book = new HashMap<>(16);
        book.put("cpp", 1);
        book.put("go", 2);
        book.put("python", 3);
        book.put("java", 8);
        entryStream = book.entrySet().stream();
        keyStream = book.keySet().stream();
        valStream = book.values().stream();
    }

    public static void intermediate() {
        entryStream.peek(System.out::println).count();
        keyStream.peek(System.out::println).count();
        valStream.peek(System.out::println).count();
    }
}
