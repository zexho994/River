import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * @author Zexho
 * @date 2021/9/8 2:08 下午
 */
public class ForkJoinSample {

    public static void main(String[] args) {
        final ForkJoinPool pool = new ForkJoinPool();
        Future<Integer> result = pool.submit(new Task(1, 100));
        try {
            System.out.println(result.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    static class Task extends RecursiveTask<Integer> {
        private final int left;
        private final int right;

        public Task(int l, int r) {
            this.left = l;
            this.right = r;
        }

        @Override
        protected Integer compute() {
            int result = 0;
            int threshold = 2;
            if ((right - left) < threshold) {
                for (int i = left; i <= right; i++) {
                    result += i;
                }
            } else {
                int mid = (left + right) / 2;

                //拆分
                Task lTask = new Task(left, mid);
                Task rTask = new Task(mid + 1, right);

                //执行
                lTask.fork();
                rTask.fork();

                //结合
                Integer lJoin = lTask.join();
                Integer rJoin = rTask.join();

                result = lJoin + rJoin;
            }

            //结果
            return result;
        }
    }
}
