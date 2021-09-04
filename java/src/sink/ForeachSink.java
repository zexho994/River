package sink;

import river.AbstractRiver;

import java.util.function.Consumer;

/**
 * @author Zexho
 * @date 2021/9/3 7:39 下午
 */
public class ForeachSink<T> extends SinkChain<T> {

    public ForeachSink(AbstractRiver<T> river, Consumer<T> consumer) {
        super(river, consumer);
    }

    @Override
    public void begin(int n) {
    }

    @Override
    public void end() {
    }

    @Override
    public void accept(T o) {
        consumer.accept(o);
    }
}
