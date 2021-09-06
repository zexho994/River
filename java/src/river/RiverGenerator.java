package river;

import pipeline.PipelineSource;
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
    public static <T> River<T> create(T... t) {
        Spliterator<T> spliterator = Arrays.spliterator(t);
        return new PipelineSource<T>(spliterator) {
            @Override
            public SinkChain<T> wrapSink(SinkChain<T> sink) {
                SinkChain<T> sinkChain = new SinkChain<T>() {
                    @Override
                    public void accept(T t) {
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
