package time.geekbang.org.week04;

import com.google.common.base.Stopwatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;

public class AsyncExecutionTest {

    private static final int N = 36;
    private final Stopwatch stopwatch = Stopwatch.createUnstarted();

    private String method;
    private long result = Long.MIN_VALUE;

    @Before
    public void setUp() {
        result = Integer.MIN_VALUE;
        stopwatch.start();
    }

    @After
    public void destroy() {
        System.out.printf("method=%s, result=%s, elapsed=%sms\n", method, result, stopwatch.elapsed(TimeUnit.MILLISECONDS));
        stopwatch.reset();
        assertEquals(14930352L, result);
    }

    @Test
    public void thread() throws InterruptedException {
        method = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        final long[] tmpArray = new long[1];
        Thread thread = new Thread(() -> tmpArray[0] = FibonacciUtil.cal(N));
        thread.start();
        thread.join();
        result = tmpArray[0];
    }

    @Test
    public void futureTask() throws ExecutionException, InterruptedException {
        method = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        FutureTask<Long> task = new FutureTask<>(() -> FibonacciUtil.cal(N));
        new Thread(task).start();
        result = task.get();
    }

    @Test
    public void executorService() throws ExecutionException, InterruptedException {
        method = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        ExecutorService executorService = Executors.newCachedThreadPool();
        FutureTask<Long> task = new FutureTask<>(() -> FibonacciUtil.cal(N));
        executorService.submit(task);
        executorService.shutdown();
        result = task.get();
    }

    @Test
    public void completableFuture() throws ExecutionException, InterruptedException {
        method = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        result = CompletableFuture.supplyAsync(() -> FibonacciUtil.cal(N)).join();
    }
}
