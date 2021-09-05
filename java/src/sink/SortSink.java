package sink;

import river.AbstractRiverPipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zexho
 * @date 2021/9/5 10:51 下午
 */
public class SortSink<T> extends SinkChain<T> {

    private List<T> list;

    public SortSink(AbstractRiverPipeline<T> river) {
        super(river);
    }

    @Override
    public void begin(int n) {
        if (n > 0) {
            this.list = new ArrayList<>(n);
        } else {
            this.list = new ArrayList<>(16);
        }
    }

    @Override
    public void end() {
        list.sort(river.getComparator());
        next.begin(list.size());
        for (T t : list) {
            next.accept(t);
        }
        next.end();
    }

    @Override
    public void accept(T t) {
        list.add(t);
    }
}
