import java.util.function.Predicate;

/**
 * Pipeline流水线是River内部流程的细节描述，将对River追加的所有操作统称为Pipeline
 * <p>
 * 对于一个Pipeline来说，是由一个{@link PipelineSource}和若干个{@link PipelineStage}组成的, 其中{@link PipelineSource}表示stage_0,
 * 之后对River进行的每一个操作，例如添加一个{@link River#filter(Predicate)}操作，等同于在Pipeline上添加一个stage。
 * 所以Pipeline也可以看成若干个Stage组成的。
 *
 * @author Zexho
 * @date 2021/9/3 3:01 下午
 */
public abstract class Pipeline<T> {
    River<T> source;
    River<T> previous;
    River<T> next;

    Op op;
}
