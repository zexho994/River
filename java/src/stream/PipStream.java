package stream;

import java.util.Objects;
import java.util.stream.Collector;

public class PipStream<T> implements IStream<T> {

    /**
     * head of the stream
     */
    private T head;

    /**
     * next function for calculate
     */
    private NextItemEvalProcess nextItemEvalProcess;

    /**
     * is end of the stream?
     */
    private boolean isEnd;

    public static class Builder<T> {
        private PipStream<T> targer;

        public Builder() {
            this.targer = new PipStream<>();
        }

        public Builder<T> head(T h) {
            Objects.requireNonNull(h);
            targer.head = h;
            return this;
        }

        Builder<T> isEnd(boolean flag) {
            Objects.requireNonNull(flag);
            targer.isEnd = flag;
            return this;
        }

        public Builder<T> nextItemEvalProcess(NextItemEvalProcess nep) {
            Objects.requireNonNull(nep);
            targer.nextItemEvalProcess = nep;
            return this;
        }

        public PipStream<T> build(){
            return targer;
        }


    }

    /**
     * 当前流强制求值
     *
     * @return 求值后返回一个新流
     */
    private PipStream eval() {
        return this.nextItemEvalProcess.eval();
    }

    private boolean isEmptyStream() {
        return this.isEnd;
    }

    @Override
    public <R> PipStream<R> map(Function<R, T> mapper) {
        NextItemEvalProcess lastNextItemEvalProcess = this.nextItemEvalProcess;
//        this.nextItemEvalProcess = new NextItemEvalProcess(
//                () ->{
//                    PipStream myStream = lastNextItemEvalProcess.eval();
//                    return map(mapper, myStream);
//                }
//        );

        // 求值链条 加入一个新的process map
        return new PipStream.Builder<R>()
                .nextItemEvalProcess(this.nextItemEvalProcess)
                .build();
    }

    @Override
    public <R> PipStream<R> flatMap(Function<? extends PipStream<R>, T> mapper) {
        return null;
    }

    @Override
    public PipStream<T> filter(Predicate<T> predicate) {
        return null;
    }

    @Override
    public PipStream<T> limit(int n) {
        return null;
    }

    @Override
    public PipStream<T> distinct() {
        return null;
    }

    @Override
    public PipStream<T> peek(ForEach<T> consumer) {
        return null;
    }

    @Override
    public void forEach(ForEach<T> consumer) {

    }

    @Override
    public <R> R reduce(R initVal, BiFunction<R, R, T> accumulator) {
        return null;
    }

    @Override
    public <R, A> R collect(Collector<T, A, R> collector) {
        return null;
    }

    @Override
    public T max(Comparator<T> comparator) {
        return null;
    }

    @Override
    public T min(Comparator<T> comparator) {
        return null;
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        return false;
    }

    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        return false;
    }


}
