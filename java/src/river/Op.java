package river;

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
    distinct(2),
    limit(3),
    sort(4),
    peek(5),

    /**
     * 结束操作
     */
    forEach(20),
    count(21);

    /**
     * 操作的code
     */
    int op;

    Op(int op) {
        this.op = op;
    }
}
