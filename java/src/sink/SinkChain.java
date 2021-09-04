package sink;

import river.AbstractRiver;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Zexho
 * @date 2021/9/3 5:08 下午
 */
public abstract class SinkChain<T> implements Sink<T> {

    public AbstractRiver<T> river;
    public SinkChain<T> next;
    public final Consumer<T> consumer;
    public Predicate<T> predicate;

    public SinkChain(AbstractRiver<T> river) {
        this.river = river;
        this.consumer = this;
        this.predicate = null;
    }

    public SinkChain(AbstractRiver<T> river, Consumer<T> consumer) {
        this.consumer = consumer;
        this.river = river;
    }

    public SinkChain(AbstractRiver<T> river, Predicate<T> action) {
        this(river);
        this.predicate = action;
    }

    public SinkChain<T> wrap(AbstractRiver<T> river) {
        SinkChain<T> previousSink;
        switch (river.op) {
            case source:
                previousSink = new SourceSink<>(river);
                break;
            case filter:
                previousSink = new FilterSink<>(river,river.getPredicate());
                break;
            case distinct:
                previousSink = new DistinctSink<>(river);
                break;
            default:
                throw new IllegalArgumentException("river op error");
        }
        previousSink.next = this;
        return previousSink;
    }

    @Override
    public abstract void begin(int n);

    @Override
    public abstract void end();

    @Override
    public abstract void accept(T t);

}
