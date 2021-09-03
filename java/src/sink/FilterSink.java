package sink;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Zexho
 * @date 2021/9/3 4:44 下午
 */
public class FilterSink<T> extends SinkChain<T> {

    Set<T> set;

    @Override
    public void begin(int n) {
        set = new HashSet<>(n == -1 ? 16 : n);
        this.next.begin(n);
    }

    @Override
    public void end() {
        this.next.end();
        set = null;
    }

    @Override
    public void accept(T t) {
        if (set.contains(t)) {
            return;
        }
        set.add(t);
        this.next.accept(t);
    }
}
