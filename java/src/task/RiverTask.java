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
    private RiverTask leftChild;
    private RiverTask rightChild;
    private SinkChain sink;

    public RiverTask(Spliterator s, PipelineStage stage) {
        this.spliterator = s;
        this.terminalStage = stage;
        this.terminalStage.setSourceSpliterator(null);
    }

    public RiverTask(Spliterator s, PipelineStage stage, SinkChain chain) {
        this.spliterator = s;
        this.sink = chain;
        this.terminalStage = stage;
        this.terminalStage.setSourceSpliterator(null);
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
            this.leftChild = new RiverTask<>(frontSpliterator, this.terminalStage, sink);
            this.rightChild = new RiverTask<>(backSpliterator, this.terminalStage, sink);
            invokeAll(leftChild, rightChild);
            this.leftChild.result = this.leftChild.join();
            this.rightChild.result = this.rightChild.join();
            E accept = (E) sink.accept(this.leftChild.getRawResult(), this.rightChild.getRawResult());
            this.setRawResult(accept);
        } else {
            this.terminalStage.setSourceSpliterator(this.spliterator);
            this.terminalStage.launch(this.terminalStage);
            this.setRawResult((E) this.terminalStage.getState());
        }
    }
}
