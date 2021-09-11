package pipeline;

import sink.SinkChain;

import java.util.Spliterator;
import java.util.function.BinaryOperator;

/**
 * @author Zexho
 * @date 2021/9/10 3:40 下午
 */
public class ReduceOpStage<O> extends PipelineStage<O, O> {

    private BinaryOperator<O> operator;
    private O identity;
    private O state;

    public ReduceOpStage(AbstractRiverPipeline<?, O> pre, Spliterator spliterator, O identity, BinaryOperator<O> op) {
        super(spliterator);
        this.previous = pre;
        this.operator = op;
        this.identity = identity;
    }

    @Override
    public SinkChain wrapSink(SinkChain<O, ?> sink) {
        SinkChain<O, O> chain = new SinkChain<O, O>() {
            @Override
            public void begin(int n) {
                state = identity;
                super.begin(n);
            }

            @Override
            public void accept(O t) {
                state = operator.apply(state, t);
            }

            @Override
            public O accept(O t1, O t2) {
                return operator.apply(t1, t2);
            }
        };
        chain.setSourceSpliterator(this.sourceSpliterator);
        return chain;
    }

    @Override
    public Object getState() {
        return this.state;
    }

    @Override
    public ReduceOpStage<O> clone() {
        return new ReduceOpStage<>(this.previous, this.sourceSpliterator, identity, operator);
    }
}
