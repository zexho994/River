package pipeline;

import pipeline.op.ReduceOpStage;
import river.River;
import sink.SinkChain;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;

/**
 * @author Zexho
 * @date 2021/9/3 2:28 下午
 */
public class AbstractRiverPipeline<I, O>
        extends Pipeline<I, O> implements River<O> {

    protected Spliterator sourceSpliterator;
    protected boolean isParallel;

    @Override
    public River<O> parallel() {
        this.isParallel = true;
        return this;
    }

    @Override
    public River<O> sequential() {
        this.isParallel = false;
        return this;
    }

    /**
     * 追加filter操作
     * 创建一个filter的{@link PipelineStage},然后将该stage追到到Pipeline的尾部
     *
     * @param predicate 过滤的表达式
     * @return 新增的filter对象
     */
    @Override
    public River<O> filter(Predicate<O> predicate) {
        return new PipelineStage<O, O>(this) {
            @Override
            public SinkChain<O, O> wrapSink(SinkChain<O, ?> sink) {
                SinkChain<O, O> sinkChain = new SinkChain<O, O>() {
                    @Override
                    public void accept(O t) {
                        if (!predicate.test(t)) {
                            return;
                        }
                        getNext().accept(t);
                    }
                };
                sinkChain.setNext(sink);
                return sinkChain;
            }
        };
    }

    @Override
    public River<O> distinct() {
        return new PipelineStage<O, O>(this) {
            @Override
            public SinkChain<O, O> wrapSink(SinkChain<O, ?> sink) {
                SinkChain<O, O> sinkChain = new SinkChain<O, O>() {
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
                        getNext().accept(t);
                    }
                };
                sinkChain.setNext(sink);
                return sinkChain;
            }
        };
    }

    @Override
    public River<O> limit(int size) {
        return new PipelineStage<O, O>(AbstractRiverPipeline.this) {
            @Override
            public SinkChain<O, O> wrapSink(SinkChain<O, ?> sink) {
                SinkChain<O, O> chain = new SinkChain<O, O>() {
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
                chain.setNext(sink);
                return chain;
            }
        };
    }

    @Override
    public River<O> sort(Comparator<O> comp) {
        return new PipelineStage<O, O>(this) {
            @Override
            public SinkChain<O, O> wrapSink(SinkChain<O, ?> sink) {
                SinkChain<O, O> sinkChain = new SinkChain<O, O>() {
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
                sinkChain.setNext(sink);
                return sinkChain;
            }
        };
    }

    @Override
    public River<O> peek(Consumer<O> consumer) {
        return new PipelineStage<O, O>(this) {

            @Override
            public SinkChain<O, O> wrapSink(SinkChain<O, ?> sink) {
                SinkChain<O, O> chain = new SinkChain<O, O>() {
                    @Override
                    public void accept(O t) {
                        consumer.accept(t);
                        next.accept(t);
                    }
                };
                chain.setNext(sink);
                return chain;
            }
        };
    }

    @Override
    public River<O> skip(int size) {
        return new PipelineStage<O, O>(this) {
            @Override
            public SinkChain<O, O> wrapSink(SinkChain<O, ?> sink) {
                SinkChain<O, O> chain = new SinkChain<O, O>() {
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
                chain.setNext(sink);
                return chain;
            }
        };
    }

    @Override
    public <E_OUT> River<E_OUT> map(Function<? super O, ? extends E_OUT> function) {
        return new PipelineStage<O, E_OUT>(this) {
            @Override
            public SinkChain<O, E_OUT> wrapSink(SinkChain<E_OUT, ?> sink) {
                SinkChain<O, E_OUT> chain = new SinkChain<O, E_OUT>() {
                    @Override
                    public void accept(O o) {
                        next.accept(function.apply(o));
                    }
                };
                chain.setNext(sink);
                return chain;
            }
        };
    }

    @Override
    public void forEach(Consumer<O> consumer) {
        PipelineStage<O, O> stage = new PipelineStage<O, O>(this) {
            @Override
            public SinkChain<O, O> wrapSink(SinkChain<O, ?> sink) {
                return new SinkChain<O, O>() {
                    @Override
                    public void accept(O t) {
                        consumer.accept(t);
                    }
                };
            }
        };
        evaluate(stage, false);
    }

    @Override
    public Object[] toArray() {
        PipelineStage<O, O> stage = new PipelineStage<O, O>(this) {
            private List<O> list;

            @Override
            public SinkChain<O, O> wrapSink(SinkChain<O, ?> sink) {
                return new SinkChain<O, O>(this.sourceSpliterator) {
                    @Override
                    public void begin(int n) {
                        list = new ArrayList<>(n > 0 ? n : 16);
                        super.begin(n);
                    }

                    @Override
                    public void accept(O t) {
                        list.add(t);
                    }
                };
            }

            @Override
            public Object[] getArray() {
                return list.toArray();
            }
        };
        evaluate(stage, false);
        return stage.getArray();
    }

    @Override
    public void toArray(O[] arr) {
        PipelineStage<O, O> stage = new PipelineStage<O, O>(this) {
            private List<O> list;

            @Override
            public SinkChain<O, O> wrapSink(SinkChain<O, ?> sink) {
                return new SinkChain<O, O>(this.sourceSpliterator) {
                    @Override
                    public void begin(int n) {
                        list = new ArrayList<>(n > 0 ? n : 16);
                        super.begin(n);
                    }

                    @Override
                    public void accept(O t) {
                        list.add(t);
                    }

                    @Override
                    public void end() {
                        list.toArray(arr);
                        super.end();
                    }
                };
            }
        };
        evaluate(stage, false);
    }

    @Override
    public long count() {
        River<Integer> map = map(e -> 1);
        return map.reduce(0, Integer::sum);
    }

    @Override
    public O reduce(O identity, BinaryOperator<O> accumulator) {
        PipelineStage<O, O> stage = new ReduceOpStage<>(this, identity, accumulator);
        evaluate(stage, true);
        return (O) stage.getState();
    }

    @Override
    public <R, A> R collect(Collector<? super O, A, R> collector) {
        PipelineStage<O, O> stage = new PipelineStage<O, O>(this) {
            private A state;

            @Override
            public SinkChain<O, O> wrapSink(SinkChain<O, ?> sink) {
                return new SinkChain<O, O>(this.sourceSpliterator) {
                    @Override
                    public void begin(int n) {
                        state = collector.supplier().get();
                    }

                    @Override
                    public void accept(O t) {
                        collector.accumulator().accept(state, t);
                    }
                };
            }

            @Override
            public A getState() {
                return state;
            }
        };
        evaluate(stage, false);
        return collector.finisher().apply((A) stage.getState());
    }

    @Override
    public Optional<O> min(Comparator<? super O> comparator) {
        PipelineStage<O, O> stage = new PipelineStage<O, O>(this) {
            private O state;

            @Override
            public SinkChain<O, O> wrapSink(SinkChain<O, ?> sink) {
                return new SinkChain<O, O>(this.sourceSpliterator) {
                    @Override
                    public void accept(O t) {
                        state = state == null ? t : comparator.compare(state, t) < 0 ? state : t;
                    }
                };
            }

            @Override
            public Optional<O> getState() {
                if (state == null) {
                    return Optional.empty();
                } else {
                    return Optional.of(state);
                }
            }
        };
        evaluate(stage, false);
        return (Optional<O>) stage.getState();
    }

    @Override
    public Optional<O> max(Comparator<? super O> comparator) {
        PipelineStage<O, O> stage = new PipelineStage<O, O>(this) {
            private O state;

            @Override
            public SinkChain<O, O> wrapSink(SinkChain<O, ?> sink) {
                return new SinkChain<O, O>(sourceSpliterator) {
                    @Override
                    public void accept(O t) {
                        state = state == null ? t : comparator.compare(state, t) > 0 ? state : t;
                    }
                };
            }

            @Override
            public Optional<O> getState() {
                if (state == null) {
                    return Optional.empty();
                } else {
                    return Optional.of(state);
                }
            }
        };
        evaluate(stage, false);
        return (Optional<O>) stage.getState();
    }

    @Override
    public boolean anyMatch(Predicate<? super O> predicate) {
        PipelineStage<O, O> stage = new PipelineStage<O, O>(this) {
            private boolean state = false;

            @Override
            public SinkChain<O, O> wrapSink(SinkChain<O, ?> sink) {
                return new SinkChain<O, O>(sourceSpliterator) {
                    @Override
                    public void accept(O t) {
                        if (state) {
                            return;
                        }
                        if (predicate.test(t)) {
                            state = true;
                        }
                    }
                };
            }

            @Override
            public Object getState() {
                return state;
            }
        };
        evaluate(stage, false);
        return (boolean) stage.getState();
    }

    @Override
    public boolean allMatch(Predicate<? super O> predicate) {
        PipelineStage<O, O> stage = new PipelineStage<O, O>(this) {
            private volatile boolean state = true;

            @Override
            public SinkChain<O, O> wrapSink(SinkChain<O, ?> sink) {
                return new SinkChain<O, O>(sourceSpliterator) {
                    @Override
                    public void accept(O t) {
                        if (!state) {
                            return;
                        }
                        if (!predicate.test(t)) {
                            state = false;
                        }
                    }
                };
            }

            @Override
            public Object getState() {
                return state;
            }
        };
        evaluate(stage, false);
        return (boolean) stage.getState();
    }

    @Override
    public boolean noneMatch(Predicate<? super O> predicate) {
        PipelineStage<O, O> stage = new PipelineStage<O, O>(this) {
            private boolean state = true;

            @Override
            public SinkChain<O, O> wrapSink(SinkChain<O, ?> sink) {
                return new SinkChain<O, O>(sourceSpliterator) {
                    @Override
                    public void accept(O t) {
                        if (!state) {
                            return;
                        }
                        if (predicate.test(t)) {
                            state = false;
                        }
                    }
                };
            }

            @Override
            public Object getState() {
                return state;
            }
        };
        evaluate(stage, false);
        return (boolean) stage.getState();
    }

    @Override
    public Optional<O> findFirst() {
        PipelineStage<O, O> stage = new PipelineStage<O, O>(this) {
            private O state;

            @Override
            public SinkChain<O, O> wrapSink(SinkChain<O, ?> sink) {
                SinkChain<O, O> sinkChain = new SinkChain<O, O>(this.sourceSpliterator) {
                    @Override
                    public void accept(O t) {
                        if (state == null) {
                            state = t;
                        }
                    }
                };
                return sinkChain;
            }

            @Override
            public Object getState() {
                if (state == null) {
                    return Optional.empty();
                } else {
                    Optional<O> res = Optional.of(this.state);
                    this.state = null;
                    return res;
                }
            }
        };
        evaluate(stage, false);
        return (Optional<O>) stage.getState();
    }

    public void setSourceSpliterator(Spliterator sourceSpliterator) {
        this.sourceSpliterator = sourceSpliterator;
    }

    public Spliterator getSourceSpliterator() {
        return sourceSpliterator;
    }

    public boolean isParallel() {
        return isParallel;
    }

    public void setParallel(boolean parallel) {
        isParallel = parallel;
    }
}
