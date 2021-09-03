package sink;

import river.AbstractRiver;

import java.util.function.Consumer;

/**
 * @author Zexho
 * @date 2021/9/3 5:08 下午
 */
public abstract class SinkChain<T> implements Sink<T> {

    public SinkChain<T> next;
    private final Consumer<T> consumer;

    public SinkChain() {
        this.consumer = null;
    }

    public SinkChain(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    public SinkChain<T> wrap(AbstractRiver<T> river) {
        SinkChain<T> previousSink;
        switch (river.op) {
            case source:
                previousSink = new SourceSink<>();
                break;
            case filter:
                previousSink = new FilterSink<>();
                break;
            case distinct:
                previousSink = new DistinctSink<>();
                break;
            default:
                throw new IllegalArgumentException("river op error");
        }
        previousSink.next = this;
        return previousSink;
    }

    @Override
    public abstract void begin();

    @Override
    public abstract void end();

    @Override
    public abstract void accept(T t);

}
