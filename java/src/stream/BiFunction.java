package stream;

@FunctionalInterface
public interface BiFunction<R, T, U> {
    R apply(T t, U u);
}
