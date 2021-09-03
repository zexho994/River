/**
 * @author Zexho
 * @date 2021/9/3 3:15 下午
 */
public class PipelineStage<T> extends RefRiver<T> {

    public PipelineStage(RefRiver<T> river, Op op) {
        this.source = river.source;
        this.previous = river;
        this.next = null;
        river.next = this;

        this.op = op;
    }

}
