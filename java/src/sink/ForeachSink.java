package sink;

import java.util.function.Consumer;

/**
 * @author Zexho
 * @date 2021/9/3 7:39 下午
 */
public class ForeachSink<T> extends SinkChain<T> {

    public ForeachSink(Consumer<T> consumer) {
        super(consumer);
    }

    @Override
    public void begin() {

    }

    @Override
    public void end() {

    }

    @Override
    public void accept(Object o) {

    }
}
