import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Zexho
 * @date 2021/9/3 2:28 下午
 */
public class RefRiver<T> implements River<T>{

    @Override
    public River<T> filter(Predicate<T> predicate) {
        return null;
    }

    @Override
    public River<T> distinct() {
        return null;
    }

    @Override
    public void forEach(Consumer<T> consumer) {

    }

    @Override
    public long count() {
        return 0;
    }
}
