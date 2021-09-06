package pipeline;

import river.AbstractRiverPipeline;

import java.util.Spliterator;

/**
 * @author Zexho
 * @date 2021/9/3 3:15 下午
 */
public class PipelineStage<I, O> extends AbstractRiverPipeline<I, O> {

    public PipelineStage(Spliterator<O> spliterator) {
        this.sourceSpliterator = spliterator;
    }

    public PipelineStage(AbstractRiverPipeline<I, O> river) {
        this.previous = river;
        this.next = null;
        river.next = this;
    }

    public int getCount() {
        throw new UnsupportedOperationException("to override");
    }

}
