package stream;

import java.util.stream.Collector;

public interface IStream<T> {

    <R> PipStream<R> map(Function<R, T> mapper);

    <R> PipStream<R> flatMap(Function<? extends PipStream<R>, T> mapper);

    PipStream<T> filter(Predicate<T> predicate);

    PipStream<T> limit(int n);

    PipStream<T> distinct();

    PipStream<T> peek(ForEach<T> consumer);

    void forEach(ForEach<T> consumer);

    <R> R reduce(R initVal,BiFunction<R,R,T> accumulator);

    <R,A> R collect(Collector<T,A,R> collector);

    T max(Comparator<T> comparator);

    T min(Comparator<T> comparator);

    int count();

    boolean anyMatch(Predicate<? super T> predicate);

    boolean allMatch(Predicate<? super T> predicate);

    static <T> PipStream<T> makeEmptyStream(){
        return new PipStream.Builder<T>().isEnd(true);
    }


}
