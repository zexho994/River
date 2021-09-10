package river;

import pipeline.PipelineStage;
import sink.SinkChain;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
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
        return create(spliterator);
    }

    public static <E> River<E> create(Collection<E> collection) {
        Spliterator<E> spliterator = Objects.requireNonNull(collection).spliterator();
        return create(spliterator);
    }

    private static <E> River<E> create(Spliterator<E> spliterator) {
        PipelineStage<E, E> head = new PipelineStage<E, E>(spliterator) {
            @Override
            public SinkChain<E, E> wrapSink(SinkChain<E, ?> sink) {
                SinkChain<E, E> sinkChain = new SinkChain<E, E>() {
                    @Override
                    public void accept(E t) {
                        next.accept(t);
                    }
                };
                sinkChain.setNext(sink);
                sinkChain.setSourceSpliterator(this.sourceSpliterator);
                return sinkChain;
            }
        };
        head.setSourceSpliterator(spliterator);
        return head;
    }

}
