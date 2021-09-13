package task;

import pipeline.PipelineStage;

import java.util.Spliterator;
import java.util.concurrent.ForkJoinTask;

/**
 * @author Zexho
 * @date 2021/9/9 4:27 下午
 */
public class RiverTask<E> extends ForkJoinTask<E> {
    /**
     * 任务执行的结果
     */
    private E result;
    private Spliterator spliterator;
    private PipelineStage terminalStage;
    private final boolean isShare;

    public RiverTask(Spliterator s, PipelineStage stage) {
        this(s, stage, true);
    }

    public RiverTask(Spliterator s, PipelineStage stage, boolean share) {
        this.spliterator = s;
        this.isShare = share;
        if (share) {
            this.terminalStage = stage.clone();
        } else {
            this.terminalStage = stage;
        }
    }

    /**
     * 获取结果
     */
    @Override
    public E getRawResult() {
        return result;
    }

    /**
     * 设置结果
     *
     * @param value
     */
    @Override
    protected void setRawResult(E value) {
        this.result = value;
    }

    @Override
    protected boolean exec() {
        this.compute();
        return true;
    }

    /**
     * 设置切分逻辑
     */
    public void compute() {
        Spliterator<E> backSpliterator = spliterator;
        Spliterator<E> frontSpliterator;
        if (backSpliterator.estimateSize() > 1 && (frontSpliterator = backSpliterator.trySplit()) != null) {
            RiverTask<E> leftTask = new RiverTask<>(frontSpliterator, terminalStage, isShare);
            RiverTask<E> rightTask = new RiverTask<>(backSpliterator, terminalStage, isShare);
            invokeAll(leftTask, rightTask);

            E left = leftTask.join();
            E right = rightTask.join();
            if (isShare) {
                E res = (E) terminalStage.wrapSink(null).accept(left, right);
                this.setRawResult(res);
            }
        } else {
            terminalStage.evaluate(spliterator, terminalStage);
            if (isShare) {
                setRawResult((E) this.terminalStage.getState());
            }
        }
    }
}
