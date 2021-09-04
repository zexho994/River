package sink;

import river.AbstractRiver;

import java.util.function.Consumer;

/**
 * @author Zexho
 * @date 2021/9/3 7:40 下午
 */
public class CountSink<T> extends SinkChain<T> {

    public CountSink(AbstractRiver<T> river, Consumer<T> consumer) {
        super(river, consumer);
    }

    @Override
    public void begin(int n) {

    }

    @Override
    public void end() {

    }

    @Override
    public void accept(T t) {

    }
}
