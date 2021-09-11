package pipeline.op;

import pipeline.AbstractRiverPipeline;
import pipeline.PipelineStage;
import sink.SinkChain;

import java.util.function.BinaryOperator;

/**
 * @author Zexho
 * @date 2021/9/10 3:40 下午
 */
public class ReduceOpStage<O> extends PipelineStage<O, O> {

    private final BinaryOperator<O> operator;
    private O state;
    private final O init;

    public ReduceOpStage(AbstractRiverPipeline<?, O> pre, O identity, BinaryOperator<O> op) {
        super(pre.getSourceSpliterator());
        this.previous = pre;
        this.operator = op;
        this.init = identity;
        this.isParallel = pre.isParallel();
    }

    @Override
    public SinkChain<?, O> wrapSink(SinkChain<O, ?> sink) {
        SinkChain<O, O> chain = new SinkChain<O, O>() {
            @Override
            public void begin(int n) {
                state = init;
                super.begin(n);
            }

            @Override
            public void accept(O t) {
                if (state == null) {
                    state = t;
                } else {
                    state = operator.apply(state, t);
                }
            }

            @Override
            public O accept(O t1, O t2) {
                return operator.apply(t1, t2);
            }
        };
        chain.setSourceSpliterator(sourceSpliterator);
        return chain;
    }

    @Override
    public Object getState() {
        return this.state;
    }

    @Override
    public ReduceOpStage<O> clone() {
        return new ReduceOpStage<>(this.previous, this.init, operator);
    }
}
