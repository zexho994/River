package sink;

import java.util.Spliterator;

/**
 * @param <IN>  输入类型
 * @param <OUT> 输出类型
 * @author Zexho
 * @date 2021/9/3 5:08 下午
 */
public abstract class SinkChain<IN, OUT> implements Sink<IN> {

    public Spliterator sourceSpliterator;
    public SinkChain next;

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
    public abstract void accept(IN t);

    public Spliterator getSourceSpliterator() {
        return sourceSpliterator;
    }

    public void setSourceSpliterator(Spliterator sourceSpliterator) {
        this.sourceSpliterator = sourceSpliterator;
    }
}
