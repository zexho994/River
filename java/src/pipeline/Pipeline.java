package pipeline;

import river.AbstractRiverPipeline;
import river.River;
import sink.SinkChain;

import java.util.Spliterator;
import java.util.function.Predicate;

/**
 * Pipeline流水线是River内部流程的细节描述，将对River追加的所有操作统称为Pipeline
 * <p>
 * 对于一个Pipeline来说，是由若干个{@link PipelineStage}组成的
 * 之后对River进行的每一个操作，例如添加一个{@link River#filter(Predicate)}操作，等同于在Pipeline上添加一个stage。
 * 所以Pipeline也可以看成若干个Stage组成的。
 *
 * @author Zexho
 * @date 2021/9/3 3:01 下午
 */
public abstract class Pipeline<I, O> {
    public AbstractRiverPipeline<?, I, ? extends River<O>> previous;

    /**
     * 启动River
     *
     * @param stage 最后一个中间操作stage
     */
    public void launch(AbstractRiverPipeline<?, O, ? extends River<O>> stage) {
        SinkChain<O> sinkHead = warpPipeline(stage);

        sinkHead.begin(-1);
        Spliterator<O> sourceSpliterator = sinkHead.getSourceSpliterator();
        sourceSpliterator.forEachRemaining(sinkHead);
        sinkHead.end();
    }

    /**
     * 所有stage包装成一条sinkChain
     *
     * @param river 最后一个中间操作
     * @return 第一个Sink
     */
    private SinkChain<O> warpPipeline(AbstractRiverPipeline<?, O, ? extends River<O>> river) {
        SinkChain<O> sink = null;
        for (AbstractRiverPipeline s = river; s != null; s = s.previous) {
            sink = s.wrapSink(sink);
        }
        return sink;
    }

}
