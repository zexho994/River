package sink;

import river.AbstractRiverPipeline;

/**
 * @author Zexho
 * @date 2021/9/5 8:03 下午
 */
public class LimitSink<T> extends SinkChain<T> {

    int count;

    public LimitSink(AbstractRiverPipeline<T> river) {
        super(river);
    }

    @Override
    public void begin(int n) {
        this.count = 0;
        super.begin(n);
    }

    @Override
    public void accept(T t) {
        if (this.count == this.river.getMaxCount()) {
            return;
        }
        this.count++;
        this.next.accept(t);
    }

}
