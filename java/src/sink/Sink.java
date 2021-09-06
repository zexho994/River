package sink;

import java.util.function.Consumer;

/**
 * @author Zexho
 * @date 2021/9/3 4:26 下午
 */
public interface Sink<E> extends Consumer<E> {
    void begin(int n);

    void end();
}
