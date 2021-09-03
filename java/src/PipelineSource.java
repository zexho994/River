import java.util.Spliterator;

/**
 * 表示River的Source，也是Pipeline的第一个阶段(stage_0)
 *
 * @author Zexho
 * @date 2021/9/3 2:28 下午
 */
public class PipelineSource<T> extends RefRiver<T> {

    public final Spliterator<T> sourceSpliterator;

    public PipelineSource(Spliterator<T> source) {
        this.sourceSpliterator = source;
    }

}
