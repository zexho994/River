package sink;

import river.AbstractRiverPipeline;

import java.util.Spliterator;
import java.util.function.Predicate;

/**
 * @author Zexho
 * @date 2021/9/3 5:08 下午
 */
public abstract class SinkChain<T> implements Sink<T> {

    public AbstractRiverPipeline<T> river;
    public Spliterator<T> sourceSpliterator;
    public SinkChain<T> next;
    public Predicate<T> predicate;

    public SinkChain() {
    }

    public SinkChain(AbstractRiverPipeline<T> river) {
        this.river = river;
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

    public Spliterator<T> getSourceSpliterator() {
        return sourceSpliterator;
    }

    public void setSourceSpliterator(Spliterator<T> sourceSpliterator) {
        this.sourceSpliterator = sourceSpliterator;
    }
}
