package stream;

@FunctionalInterface
public interface Predicate<T> {

    boolean satisfy(T item);

}
