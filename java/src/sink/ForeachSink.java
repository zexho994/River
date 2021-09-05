package sink;

import river.AbstractRiverPipeline;

import java.util.function.Consumer;

/**
 * @author Zexho
 * @date 2021/9/3 7:39 下午
 */
public class ForeachSink<T> extends SinkChain<T> {

    public ForeachSink(AbstractRiverPipeline<T> river, Consumer<T> consumer) {
        super(river, consumer);
    }

    @Override
    public void accept(T o) {
        consumer.accept(o);
    }
}
