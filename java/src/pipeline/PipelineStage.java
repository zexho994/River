package pipeline;

import river.AbstractRiverPipeline;

import java.util.Spliterator;

/**
 * @author Zexho
 * @date 2021/9/3 3:15 下午
 */
public class PipelineStage<I, O> extends AbstractRiverPipeline<I, O> {

    public PipelineStage(Spliterator<I> spliterator) {
        this.sourceSpliterator = spliterator;
    }

    /**
     * @param river 上一个stage
     */
    public PipelineStage(AbstractRiverPipeline<?, I> river) {
        this.previous = river;
    }

    public int getCount() {
        throw new UnsupportedOperationException("to override");
    }

    public Object[] getArray() {
        throw new UnsupportedOperationException("to override");
    }

    public O getState() {
        throw new UnsupportedOperationException("to override");
    }

}
