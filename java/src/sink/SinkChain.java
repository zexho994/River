package sink;

import river.AbstractRiverPipeline;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Zexho
 * @date 2021/9/3 5:08 下午
 */
public abstract class SinkChain<T> implements Sink<T> {

    public AbstractRiverPipeline<T> river;
    public SinkChain<T> next;
    public final Consumer<T> consumer;
    public Predicate<T> predicate;

    public SinkChain(AbstractRiverPipeline<T> river) {
        this.river = river;
        this.consumer = this;
    }

    public SinkChain(AbstractRiverPipeline<T> river, Consumer<T> consumer) {
        this.consumer = consumer;
        this.river = river;
    }

    public SinkChain<T> wrap(AbstractRiverPipeline<T> river) {
        SinkChain<T> previousSink;
        switch (river.op) {
            case source:
                previousSink = new SourceSink<>(river);
                break;
            case filter:
                previousSink = new FilterSink<>(river, river.getPredicate());
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
    public void begin(int n) {
        if (next != null) {
            this.next.begin(n);
        }
    }

    @Override
    public void end() {
        if (next != null) {
            this.next.end();
        }
    }

    @Override
    public abstract void accept(T t);

}
