package sink;

import river.AbstractRiverPipeline;

import java.util.function.Predicate;

/**
 * @author Zexho
 * @date 2021/9/3 4:44 下午
 */
public class FilterSink<T> extends SinkChain<T> {

    public FilterSink(AbstractRiverPipeline<T> river, Predicate<T> action) {
        super(river);
        this.predicate = action;
    }

    @Override
    public void accept(T t) {
        Predicate<T> predicate = this.river.getPredicate();
        if (!predicate.test(t)) {
            return;
        }
        this.next.accept(t);
    }
}
