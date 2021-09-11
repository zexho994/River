package task;

import pipeline.PipelineStage;
import sink.SinkChain;

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
    private SinkChain sink;

    public RiverTask(Spliterator s, PipelineStage stage, SinkChain chain) {
        this.spliterator = s;
        this.sink = chain;
        this.terminalStage = stage;
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
        if (backSpliterator.estimateSize() > 2 && (frontSpliterator = backSpliterator.trySplit()) != null) {
            PipelineStage leftStage = terminalStage.clone();
            PipelineStage rightStage = terminalStage.clone();
            leftStage.setSourceSpliterator(frontSpliterator);
            rightStage.setSourceSpliterator(backSpliterator);

            RiverTask<E> leftTask = new RiverTask<>(frontSpliterator, leftStage, sink);
            RiverTask<E> rightTask = new RiverTask<>(backSpliterator, rightStage, sink);

            leftTask.fork();
            rightTask.fork();

            E right = rightTask.join();
            E left = leftTask.join();

            E res = (E) sink.accept(left, right);
            this.setRawResult(res);
        } else {
            this.terminalStage.launch(this.terminalStage);
            this.setRawResult((E) this.terminalStage.getState());
        }
    }
}
