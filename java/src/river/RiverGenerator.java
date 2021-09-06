package river;

import pipeline.PipelineStage;
import sink.SinkChain;

import java.util.Arrays;
import java.util.Spliterator;

/**
 * 负责River的创建
 *
 * @author Zexho
 * @date 2021/9/3 11:37 上午
 */
final class RiverGenerator {

    @SafeVarargs
    public static <E> River<E> create(E... e) {
        Spliterator<E> spliterator = Arrays.spliterator(e);
        return new PipelineStage<E, E>(spliterator) {
            @Override
            public SinkChain<E> wrapSink(SinkChain<E> sink) {
                SinkChain<E> sinkChain = new SinkChain<E>() {
                    @Override
                    public void accept(E t) {
                        next.accept(t);
                    }
                };
                sinkChain.next = sink;
                sinkChain.setSourceSpliterator(spliterator);
                return sinkChain;
            }
        };
    }

}
