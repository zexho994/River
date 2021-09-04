package sink;

import river.AbstractRiver;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Zexho
 * @date 2021/9/3 5:18 下午
 */
public class DistinctSink<T> extends SinkChain<T> {

    Set<T> set = null;

    public DistinctSink(AbstractRiver<T> river) {
        super(river);
    }

    @Override
    public void begin(int n) {
        set = new HashSet<>(n < 0 ? 16 : n);
    }

    @Override
    public void end() {
        this.next.end();
    }

    @Override
    public void accept(T t) {
        if (!this.set.add(t)) {
            return;
        }
        this.next.accept(t);
    }

}
