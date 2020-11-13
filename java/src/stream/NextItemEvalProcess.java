package stream;

/**
 * 下一个元素求值过程
 */
public class NextItemEvalProcess {

    /**
     * 求值方法
     */
    private EvalFunction evalFunction;

    public NextItemEvalProcess(EvalFunction evalFunction){
        this.evalFunction = evalFunction;
    }

    PipStream eval(){
        return evalFunction.apply();
    }

}
