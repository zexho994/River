package river;

import pipeline.Pipeline;
import pipeline.PipelineStage;
import sink.CountSink;
import sink.SinkChain;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Zexho
 * @date 2021/9/3 2:28 下午
 */
public class AbstractRiverPipeline<T> extends Pipeline<T> implements River<T> {

    protected Spliterator<T> sourceSpliterator;
    protected Predicate<T> predicate;
    protected int maxCount;
    protected Comparator<T> comparator;

    /**
     * 追加filter操作
     * 创建一个filter的{@link PipelineStage},然后将该stage追到到Pipeline的尾部
     *
     * @param predicate 过滤的表达式
     * @return 新增的filter对象
     */
    @Override
    public River<T> filter(Predicate<T> predicate) {
        PipelineStage<T> stage = new PipelineStage<>(this, Op.filter);
        stage.setPredicate(predicate);
        return stage;
    }

    @Override
    public River<T> distinct() {
        return new PipelineStage<>(this, Op.distinct);
    }

    @Override
    public River<T> limit(int size) {
        return new PipelineStage<T>(AbstractRiverPipeline.this, Op.limit) {
            @Override
            public SinkChain<T> wrapSink(SinkChain<T> sink) {
                SinkChain<T> chain = new SinkChain<T>() {
                    private int count = 0;

                    @Override
                    public void begin(int n) {
                        this.next.begin(size);
                    }

                    @Override
                    public void accept(T t) {
                        if (this.count == size) {
                            return;
                        }
                        this.count++;
                        this.next.accept(t);
                    }
                };
                chain.next = sink;
                return chain;
            }
        };
    }

    @Override
    public River<T> sort(Comparator<T> comparator) {
        PipelineStage<T> stage = new PipelineStage<>(this, Op.sort);
        stage.comparator = comparator;
        return stage;
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        PipelineStage<T> pipeFinal = new PipelineStage<T>(this, Op.forEach) {
            @Override
            public SinkChain<T> wrapSink(SinkChain<T> sink) {
                SinkChain<T> chain = new SinkChain<T>() {
                    @Override
                    public void accept(T t) {
                        consumer.accept(t);
                    }
                };
                chain.next = sink;
                return chain;
            }
        };
        launch(pipeFinal);
    }

    @Override
    public long count() {
        CountSink<T> countSink = new CountSink<>(new PipelineStage<>());
        launch(this);
        return countSink.getCount();
    }

    public Spliterator<T> getSourceSpliterator() {
        return sourceSpliterator;
    }

    public Predicate<T> getPredicate() {
        return predicate;
    }

    public void setMaxCount(int count) {
        this.maxCount = count;
    }

    public int getMaxCount() {
        return this.maxCount;
    }

    public Comparator<T> getComparator() {
        return this.comparator;
    }

    public SinkChain<T> wrapSink(SinkChain<T> sink) {
        return null;
    }
}
