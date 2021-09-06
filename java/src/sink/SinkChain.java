package sink;

import river.AbstractRiverPipeline;

import java.util.Spliterator;

/**
 * @author Zexho
 * @date 2021/9/3 5:08 下午
 */
public abstract class SinkChain<E> implements Sink<E> {

    public Spliterator<E> sourceSpliterator;
    public SinkChain<E> next;

    public SinkChain() {
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
    public abstract void accept(E t);

    public Spliterator<E> getSourceSpliterator() {
        return sourceSpliterator;
    }

    public void setSourceSpliterator(Spliterator<E> sourceSpliterator) {
        this.sourceSpliterator = sourceSpliterator;
    }
}
