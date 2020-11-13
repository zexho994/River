package stream;

@FunctionalInterface
public interface ForEach<T> {

    void apply(T item);

}
