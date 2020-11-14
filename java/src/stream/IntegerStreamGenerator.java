package stream;

/**
 * @author Zexho
 * @date 2020/11/14 9:37 上午
 */
public class IntegerStreamGenerator {
    public static PipStream getIntegerStream(int low, int high) {
        return getIntegerStreamInner(low, high, true);
    }

    private static PipStream<Integer> getIntegerStreamInner(int low, int high, boolean isStart) {
        if (low > high) {
            return IStream.makeEmptyStream();
        }

        if(isStart) {
            return new PipStream.Builder<Integer>()
                    .process(new NextItemEvalProcess(() -> getIntegerStreamInner(low, high, false)))
                    .build();
        }else{
            return new PipStream.Builder<>()
                    .head(low)
                    .process(new NextItemEvalProcess(() -> getIntegerStreamInner(low + 1, high, false)))
                    .build();
        }
    }

}
