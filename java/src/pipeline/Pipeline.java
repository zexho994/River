package pipeline;

import river.AbstractRiverPipeline;
import river.Op;
import river.River;
import sink.SinkChain;

import java.util.Spliterator;
import java.util.function.Predicate;

/**
 * Pipeline流水线是River内部流程的细节描述，将对River追加的所有操作统称为Pipeline
 * <p>
 * 对于一个Pipeline来说，是由一个{@link PipelineSource}和若干个{@link PipelineStage}组成的, 其中{@link PipelineSource}表示stage_0,
 * 之后对River进行的每一个操作，例如添加一个{@link River#filter(Predicate)}操作，等同于在Pipeline上添加一个stage。
 * 所以Pipeline也可以看成若干个Stage组成的。
 *
 * @author Zexho
 * @date 2021/9/3 3:01 下午
 */
public abstract class Pipeline<T> {
    public AbstractRiverPipeline<T> source;
    public AbstractRiverPipeline<T> previous;
    public AbstractRiverPipeline<T> next;
    public Op op;

    /**
     * 启动River
     *
     * @param stage 最后一个中间操作stage
     * @param tail  终结操作的sink
     */
    public void launch(AbstractRiverPipeline<T> stage, SinkChain<T> tail) {
        SinkChain<T> sinkHead = warpPipeline(stage, tail);

        sinkHead.begin(-1);
        Spliterator<T> sourceSpliterator = sinkHead.river.getSourceSpliterator();
        sourceSpliterator.forEachRemaining(sinkHead);
        sinkHead.end();
    }

    /**
     * 所有stage包装成一条sinkChain
     *
     * @param river 最后一个中间操作
     * @param tail  终结操作sink
     * @return 第一个Sink
     */
    private SinkChain<T> warpPipeline(AbstractRiverPipeline<T> river, SinkChain<T> tail) {
        SinkChain<T> sink = tail;

        for (AbstractRiverPipeline<T> s = river; s != null; s = s.previous) {
            sink = sink.wrap(s);
        }
        return sink;
    }
}
