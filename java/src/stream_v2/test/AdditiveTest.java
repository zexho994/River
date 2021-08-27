package stream_v2.test;

import stream_v2.Additive;

/**
 * @author Zexho
 * @date 2021/8/27 9:01 下午
 */
public class AdditiveTest {
    public static void main(String[] args) {
        Additive add = n -> n * 10;
        System.out.println(add.calculate(1));
    }
}
