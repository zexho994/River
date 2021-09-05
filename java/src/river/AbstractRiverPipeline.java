package river;

import pipeline.Pipeline;
import pipeline.PipelineStage;
import sink.CountSink;
import sink.ForeachSink;

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
        PipelineStage<T> pipelineStage = new PipelineStage<>(this, Op.limit);
        pipelineStage.setMaxCount(size);
        return pipelineStage;
    }

    @Override
    public River<T> sort(Comparator<T> comparator) {
        PipelineStage<T> stage = new PipelineStage<>(this, Op.sort);
        stage.comparator = comparator;
        return stage;
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        PipelineStage<T> pipeFinal = new PipelineStage<>();
        ForeachSink<T> foreachSink = new ForeachSink<>(pipeFinal, consumer);
        launch(this, foreachSink);
    }

    @Override
    public long count() {
        PipelineStage<T> pipeFinal = new PipelineStage<>();
        CountSink<T> countSink = new CountSink<>(pipeFinal);
        launch(this, countSink);
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
}
