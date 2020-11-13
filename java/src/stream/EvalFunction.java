package stream;

@FunctionalInterface
public interface EvalFunction<T> {

    PipStream<T> apply();

}
