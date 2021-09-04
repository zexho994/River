package sink;

import river.AbstractRiver;

/**
 * @author Zexho
 * @date 2021/9/3 5:43 下午
 */
public class SourceSink<T> extends SinkChain<T> {

    public SourceSink(AbstractRiver<T> river) {
        super(river);
    }

    @Override
    public void begin(int n) {
        this.next.begin(-1);
    }

    @Override
    public void end() {
        this.next.end();
    }

    @Override
    public void accept(T t) {
        this.next.accept(t);
    }
}
