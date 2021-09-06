package pipeline;

import river.AbstractRiverPipeline;
import river.Op;

/**
 * @author Zexho
 * @date 2021/9/3 3:15 下午
 */
public class PipelineStage<T> extends AbstractRiverPipeline<T> {

    public PipelineStage(AbstractRiverPipeline<T> river, Op op) {
        this.source = river.source;
        this.previous = river;
        this.next = null;
        river.next = this;
        this.op = op;
    }

    public int getCount() {
        throw new UnsupportedOperationException("to override");
    }

}
