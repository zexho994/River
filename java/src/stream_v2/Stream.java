package stream_v2;

/**
 * @author Zexho
 * @date 2021/8/27 9:19 下午
 */
public interface Stream<T> {

    Stream<T> limit();

    Boolean andMatch(T t);

}
