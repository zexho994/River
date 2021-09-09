package sink;

import java.util.Spliterator;

/**
 * @param <IN>  输入类型
 * @param <OUT> 输出类型
 * @author Zexho
 * @date 2021/9/3 5:08 下午
 */
public abstract class SinkChain<IN, OUT> implements Sink<IN> {

    protected Spliterator<IN> sourceSpliterator;
    protected SinkChain<OUT, ?> next;

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

    /**
     * 接受元素，并由子类进行重写，执行对应的逻辑
     *
     * @param t 接受的元素
     */
    @Override
    public abstract void accept(IN t);

    public Spliterator<IN> getSourceSpliterator() {
        return sourceSpliterator;
    }

    public void setSourceSpliterator(Spliterator<IN> sourceSpliterator) {
        this.sourceSpliterator = sourceSpliterator;
    }

    public SinkChain<OUT, ?> getNext() {
        return next;
    }

    public void setNext(SinkChain<OUT, ?> next) {
        this.next = next;
    }
}
