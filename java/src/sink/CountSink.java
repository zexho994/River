package sink;

import river.AbstractRiverPipeline;

/**
 * @author Zexho
 * @date 2021/9/3 7:40 下午
 */
public class CountSink<T> extends SinkChain<T> {

    /**
     * 记录元素数量
     */
    private int count;

    public CountSink(AbstractRiverPipeline<T> river) {
        super(river);
    }

    @Override
    public void begin(int n) {
        this.count = 0;
    }

    @Override
    public void accept(T t) {
        this.count++;
    }

    public int getCount() {
        return count;
    }
}
