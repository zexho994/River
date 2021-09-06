package river;

import pipeline.Pipeline;
import pipeline.PipelineStage;
import sink.SinkChain;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Zexho
 * @date 2021/9/3 2:28 下午
 */
public class AbstractRiverPipeline<I, O, S extends River<O>>
        extends Pipeline<I, O> implements River<O> {

    protected Spliterator<O> sourceSpliterator;

    /**
     * 追加filter操作
     * 创建一个filter的{@link PipelineStage},然后将该stage追到到Pipeline的尾部
     *
     * @param predicate 过滤的表达式
     * @return 新增的filter对象
     */
    @Override
    public River<O> filter(Predicate<O> predicate) {
        return new PipelineStage<I, O>(this) {
            @Override
            public SinkChain<O> wrapSink(SinkChain<O> sink) {
                SinkChain<O> sinkChain = new SinkChain<O>() {
                    @Override
                    public void accept(O t) {
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
    public River<O> distinct() {
        return new PipelineStage<I, O>(this) {
            @Override
            public SinkChain<O> wrapSink(SinkChain<O> sink) {
                SinkChain<O> sinkChain = new SinkChain<O>() {
                    private HashSet<O> set;

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
                    public void accept(O t) {
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
    public River<O> limit(int size) {
        return new PipelineStage<I, O>(AbstractRiverPipeline.this) {
            @Override
            public SinkChain<O> wrapSink(SinkChain<O> sink) {
                SinkChain<O> chain = new SinkChain<O>() {
                    private int count = 0;

                    @Override
                    public void begin(int n) {
                        this.next.begin(size);
                    }

                    @Override
                    public void accept(O t) {
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
    public River<O> sort(Comparator<O> comp) {
        return new PipelineStage<I, O>(this) {
            @Override
            public SinkChain<O> wrapSink(SinkChain<O> sink) {
                SinkChain<O> sinkChain = new SinkChain<O>() {
                    private Comparator<O> comparator;
                    private List<O> list;

                    @Override
                    public void begin(int n) {
                        this.comparator = comp;
                        this.list = new ArrayList<>(n > 0 ? n : 16);
                        super.begin(n);
                    }

                    @Override
                    public void accept(O t) {
                        this.list.add(t);
                    }

                    @Override
                    public void end() {
                        list.sort(this.comparator);
                        for (O t : list) {
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
    public River<O> peek(Consumer<O> consumer) {
        return new PipelineStage<I, O>(this) {

            @Override
            public SinkChain<O> wrapSink(SinkChain<O> sink) {
                SinkChain<O> chain = new SinkChain<O>() {
                    @Override
                    public void accept(O t) {
                        consumer.accept(t);
                        next.accept(t);
                    }
                };
                chain.next = sink;
                return chain;
            }
        };
    }

    @Override
    public River<O> skip(int size) {
        return new PipelineStage<I, O>(this) {
            @Override
            public SinkChain<O> wrapSink(SinkChain<O> sink) {
                SinkChain<O> chain = new SinkChain<O>() {
                    private int num;

                    @Override
                    public void begin(int n) {
                        num = size;
                        super.begin(Math.max(n - num, 0));
                    }

                    @Override
                    public void accept(O t) {
                        if (num > 0) {
                            num--;
                            return;
                        }
                        this.next.accept(t);
                    }
                };
                chain.next = sink;
                return chain;
            }
        };
    }

    @Override
    public <E_OUT> River<E_OUT> map(Function<O, E_OUT> function) {
        return null;
    }

    @Override
    public void forEach(Consumer<O> consumer) {
        PipelineStage<I, O> pipeFinal = new PipelineStage<I, O>(this) {
            @Override
            public SinkChain<O> wrapSink(SinkChain<O> sink) {
                SinkChain<O> chain = new SinkChain<O>() {
                    @Override
                    public void accept(O t) {
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
        PipelineStage<I, O> pipeFinal = new PipelineStage<I, O>(this) {
            private int count;

            @Override
            public SinkChain<O> wrapSink(SinkChain<O> sink) {
                return new SinkChain<O>() {
                    @Override
                    public void begin(int n) {
                        count = 0;
                        super.begin(n);
                    }

                    @Override
                    public void end() {
                        super.end();
                    }

                    @Override
                    public void accept(O t) {
                        count++;
                    }
                };
            }

            @Override
            public int getCount() {
                return this.count;
            }

        };
        launch(pipeFinal);
        return pipeFinal.getCount();
    }

    public SinkChain<O> wrapSink(SinkChain<O> sink) {
        return null;
    }
}
