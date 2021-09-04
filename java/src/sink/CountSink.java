package sink;

import river.AbstractRiver;

import java.util.function.Consumer;

/**
 * @author Zexho
 * @date 2021/9/3 7:40 下午
 */
public class CountSink<T> extends SinkChain<T> {

    private int count;

    public CountSink(AbstractRiver<T> river) {
        super(river);
    }

    @Override
    public void begin(int n) {
        this.count = 0;
    }

    @Override
    public void end() {
    }

    @Override
    public void accept(T t) {
        count++;
    }

    public int getCount() {
        return count;
    }
}
