/**
 * 表示操作类型
 *
 * @author Zexho
 * @date 2021/9/3 3:41 下午
 */
public enum Op {
    /**
     * source
     */
    source(0),

    /**
     * 中间操作
     */
    filter(1),
    distinct(2);

    /**
     * 操作的code
     */
    final int op;

    Op(int op) {
        this.op = op;
    }
}
