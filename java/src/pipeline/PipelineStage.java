package pipeline;

import river.AbstractRiverPipeline;
import river.Op;

import java.util.function.Predicate;

/**
 * @author Zexho
 * @date 2021/9/3 3:15 下午
 */
public class PipelineStage<T> extends AbstractRiverPipeline<T> {

    public PipelineStage() {
    }

    public PipelineStage(AbstractRiverPipeline<T> river, Op op) {
        this.source = river.source;
        this.previous = river;
        this.next = null;
        river.next = this;
        this.op = op;
    }

}
