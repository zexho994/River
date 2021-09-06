package river;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * River就像一条河流，源源不断的流~
 * <p>
 * 使用构造的方法，给River设置一个源头，River后续将从源头获取元素
 * <p>
 * 在流的过程中可以进行若干的中间操作，例如:
 * - 使用{@link #filter(Predicate)}可以对River进行过滤
 * - 使用{@link #distinct()}对River进行去重
 * <p>
 * River也不是永远没有尽头的，我们需要设置一个终点，进行最终操作。
 * 为了形成一个闭环，只有在我们设置好终点操作时，整个流程才会开始启动。不然设置的中间操作是永远不会执行的。
 * 终结操作的类型很多，例如:
 * - 使用{@link #forEach(Consumer)}遍历所有元素，然后进行特点操作。
 * - 使用{@link #count()}来统计River中有多少元素。
 *
 * @author Zexho
 * @date 2021/9/3 11:01 上午
 */
public interface River<T> {

    /**
     * 构建一个River对象
     *
     * @param t 元素对象
     * @return 构造后的River对象
     * @since V1.0
     */
    @SafeVarargs
    static <T> River<T> of(T... t) {
        return RiverGenerator.create(t);
    }

    /**
     * 过滤操作
     *
     * @param predicate 过滤的表达式
     * @return 过滤后的River
     */
    River<T> filter(Predicate<T> predicate);

    /**
     * 元素去重操作
     *
     * @return 去重后的River
     */
    River<T> distinct();

    /**
     * 限制River的元素数量
     *
     * @param size 元素的最大数量
     * @return River
     */
    River<T> limit(int size);

    /**
     * 排序
     *
     * @param comparable 比较器
     * @return 添加排序后的River
     */
    River<T> sort(Comparator<T> comparable);

    /**
     * 对元素进行预操作
     *
     * @param consumer 执行的操作
     * @return 新River
     */
    River<T> peek(Consumer<T> consumer);

    /**
     * 跳过指定数量的元素
     *
     * @param size 要跳过的元素数
     */
    River<T> skip(int size);

    /**
     * 遍历River所有元素
     *
     * @param consumer 表达式
     */
    void forEach(Consumer<T> consumer);

    /**
     * 计算元素的数量
     *
     * @return River中元素的数量
     */
    long count();

}
