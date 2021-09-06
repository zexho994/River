package pipeline;

import river.AbstractRiverPipeline;

/**
 * @author Zexho
 * @date 2021/9/3 3:15 下午
 */
public class PipelineStage<T> extends AbstractRiverPipeline<T> {

    public PipelineStage(AbstractRiverPipeline<T> river) {
        this.source = river.source;
        this.previous = river;
        this.next = null;
        river.next = this;
    }

    public int getCount() {
        throw new UnsupportedOperationException("to override");
    }

}
