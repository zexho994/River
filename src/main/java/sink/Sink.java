package sink;

import java.util.function.Consumer;

/**
 * Sink是对Consumer的一个增强，扩展了begin()和end()方法。
 * <p>
 * begin()方法的内容可以定义一些预操作实现懒加载类似的需求，例如在begin里初始化容器，赋值等。
 * begin()方法应该在所有其他方法之前调用。
 * <p>
 * end()方法里面可以定义一些收尾操作，主要是对资源的释放，例如创建的容器设为null、变量的值复原等操作。
 *
 * @author Zexho
 * @date 2021/9/3 4:26 下午
 */
public interface Sink<E> extends Consumer<E> {
    /**
     * 在流来之前，通过begin可以进行预处理
     *
     * @param n 流的大小,未知为-1
     */
    void begin(int n);

    /**
     * 在流程处理完后调用
     */
    void end();
}
