package river;

import pipeline.Pipeline;
import pipeline.PipelineStage;
import sink.CountSink;
import sink.SinkChain;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Zexho
 * @date 2021/9/3 2:28 下午
 */
public class AbstractRiverPipeline<T> extends Pipeline<T> implements River<T> {

    protected Spliterator<T> sourceSpliterator;
    protected int maxCount;

    /**
     * 追加filter操作
     * 创建一个filter的{@link PipelineStage},然后将该stage追到到Pipeline的尾部
     *
     * @param predicate 过滤的表达式
     * @return 新增的filter对象
     */
    @Override
    public River<T> filter(Predicate<T> predicate) {
        return new PipelineStage<T>(this, Op.filter) {
            @Override
            public SinkChain<T> wrapSink(SinkChain<T> sink) {
                SinkChain<T> sinkChain = new SinkChain<T>() {
                    @Override
                    public void accept(T t) {
                        if (!predicate.test(t)) {
                            return;
                        }
                        next.accept(t);
                    }
                };
                sinkChain.next = sink;
                return sinkChain;
            }
        };
    }

    @Override
    public River<T> distinct() {
        return new PipelineStage<T>(this, Op.distinct) {
            @Override
            public SinkChain<T> wrapSink(SinkChain<T> sink) {
                SinkChain<T> sinkChain = new SinkChain<T>() {
                    private HashSet<T> set;

                    @Override
                    public void begin(int n) {
                        this.set = new HashSet<>(n > 0 ? n : 16);
                        super.begin(n);
                    }

                    @Override
                    public void end() {
                        this.set = null;
                        super.end();
                    }

                    @Override
                    public void accept(T t) {
                        if (!set.add(t)) {
                            return;
                        }
                        next.accept(t);
                    }
                };
                sinkChain.next = sink;
                return sinkChain;
            }
        };
    }

    @Override
    public River<T> limit(int size) {
        return new PipelineStage<T>(AbstractRiverPipeline.this, Op.limit) {
            @Override
            public SinkChain<T> wrapSink(SinkChain<T> sink) {
                SinkChain<T> chain = new SinkChain<T>() {
                    private int count = 0;

                    @Override
                    public void begin(int n) {
                        this.next.begin(size);
                    }

                    @Override
                    public void accept(T t) {
                        if (this.count == size) {
                            return;
                        }
                        this.count++;
                        this.next.accept(t);
                    }
                };
                chain.next = sink;
                return chain;
            }
        };
    }

    @Override
    public River<T> sort(Comparator<T> comp) {
        return new PipelineStage<T>(this, Op.sort) {
            @Override
            public SinkChain<T> wrapSink(SinkChain<T> sink) {
                SinkChain<T> sinkChain = new SinkChain<T>() {
                    private Comparator<T> comparator;
                    private List<T> list;

                    @Override
                    public void begin(int n) {
                        this.comparator = comp;
                        this.list = new ArrayList<>(n > 0 ? n : 16);
                        super.begin(n);
                    }

                    @Override
                    public void accept(T t) {
                        this.list.add(t);
                    }

                    @Override
                    public void end() {
                        list.sort(this.comparator);
                        for (T t : list) {
                            this.next.accept(t);
                        }
                        this.list = null;
                        this.comparator = null;
                        super.end();
                    }
                };
                sinkChain.next = sink;
                return sinkChain;
            }
        };
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        PipelineStage<T> pipeFinal = new PipelineStage<T>(this, Op.forEach) {
            @Override
            public SinkChain<T> wrapSink(SinkChain<T> sink) {
                SinkChain<T> chain = new SinkChain<T>() {
                    @Override
                    public void accept(T t) {
                        consumer.accept(t);
                    }
                };
                chain.next = sink;
                return chain;
            }
        };
        launch(pipeFinal);
    }

    @Override
    public long count() {
        CountSink<T> countSink = new CountSink<>(new PipelineStage<>());
        launch(this);
        return countSink.getCount();
    }

    public Spliterator<T> getSourceSpliterator() {
        return sourceSpliterator;
    }

    public void setMaxCount(int count) {
        this.maxCount = count;
    }

    public int getMaxCount() {
        return this.maxCount;
    }

    public SinkChain<T> wrapSink(SinkChain<T> sink) {
        return null;
    }
}
