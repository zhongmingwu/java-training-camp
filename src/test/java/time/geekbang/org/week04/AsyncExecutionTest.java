package time.geekbang.org.week04;

import com.google.common.base.Stopwatch;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class AsyncExecutionTest {

    private static final int N = 36;
    private static final long FIBONACCI_N = 14930352L;

    private final Stopwatch stopwatch = Stopwatch.createUnstarted();
    private String name;
    private long result;
    private volatile boolean isDone;

    @Before
    public void setUp() {
        result = Long.MIN_VALUE;
        stopwatch.start();
    }

    @After
    public void destroy() {
        System.out.printf("invoked_method=[%s], elapsed_ms=%s\n", name, stopwatch.elapsed(TimeUnit.MILLISECONDS));
        assertEquals(FIBONACCI_N, result);
        stopwatch.reset();
    }

    @Test
    public void m_1_join() throws InterruptedException {
        name = getMethodName();

        Thread thread = new Thread(() -> result = FibonacciUtil.fibonacci(N), name);
        thread.start();
        thread.join();
    }

    @Test
    public void m_2_notification() throws InterruptedException {
        name = getMethodName();

        new Thread(() -> {
            synchronized (this) {
                result = FibonacciUtil.fibonacci(N);
                notifyAll();
            }
        }, name).start();

        TimeUnit.MILLISECONDS.sleep(100);
        synchronized (this) {
        }
    }

    @Test(expected = InterruptedException.class)
    public void m_3_interrupt() throws InterruptedException {
        name = getMethodName();

        Thread thread = Thread.currentThread();
        new Thread(() -> {
            result = FibonacciUtil.fibonacci(N);
            thread.interrupt();
        }, name).start();

        synchronized (this) {
            wait();
        }
    }

    @Test
    public void m_4_park() {
        name = getMethodName();

        Thread thread = Thread.currentThread();
        new Thread(() -> {
            result = FibonacciUtil.fibonacci(N);
            LockSupport.unpark(thread);
        }, name).start();

        LockSupport.park();
    }

    @Test
    public void m_5_volatile() {
        name = getMethodName();

        new Thread(() -> {
            result = FibonacciUtil.fibonacci(N);
            isDone = true;
        }, name).start();

        while (!isDone) {
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10));
        }
        isDone = false;
    }

    //@Test
    //public void futureTask() throws ExecutionException, InterruptedException {
    //    method = new Object() {
    //    }.getClass().getEnclosingMethod().getName();
    //
    //    FutureTask<Long> task = new FutureTask<>(() -> FibonacciUtil.fibonacci(N));
    //    new Thread(task).start();
    //    result = task.get();
    //}
    //
    //@Test
    //public void executorService() throws ExecutionException, InterruptedException {
    //    method = new Object() {
    //    }.getClass().getEnclosingMethod().getName();
    //
    //    ExecutorService executorService = Executors.newCachedThreadPool();
    //    FutureTask<Long> task = new FutureTask<>(() -> FibonacciUtil.fibonacci(N));
    //    executorService.submit(task);
    //    executorService.shutdown();
    //    result = task.get();
    //}
    //
    //@Test
    //public void semaphore() throws InterruptedException {
    //    method = new Object() {
    //    }.getClass().getEnclosingMethod().getName();
    //
    //    Semaphore semaphore = new Semaphore(1);
    //    new Thread(() -> {
    //        try {
    //            semaphore.acquire();
    //            result = FibonacciUtil.fibonacci(N);
    //        } catch (InterruptedException e) {
    //            e.printStackTrace();
    //        } finally {
    //            semaphore.release();
    //        }
    //    }, method).start();
    //
    //    TimeUnit.MILLISECONDS.sleep(100);
    //    semaphore.acquire();
    //    semaphore.release();
    //}
    //
    //@Test
    //public void countDownLatch() throws InterruptedException {
    //    method = new Object() {
    //    }.getClass().getEnclosingMethod().getName();
    //
    //    CountDownLatch countDownLatch = new CountDownLatch(1);
    //    new Thread(() -> {
    //        result = FibonacciUtil.fibonacci(N);
    //        countDownLatch.countDown();
    //    }, method).start();
    //    countDownLatch.await();
    //}
    //
    //@Test
    //public void cyclicBarrier() throws InterruptedException {
    //    method = new Object() {
    //    }.getClass().getEnclosingMethod().getName();
    //
    //    Object o = new Object();
    //    CyclicBarrier cyclicBarrier = new CyclicBarrier(1, () -> result = FibonacciUtil.fibonacci(N));
    //    new Thread(() -> {
    //        try {
    //            synchronized (o) {
    //                result = FibonacciUtil.fibonacci(N);
    //                cyclicBarrier.await();
    //            }
    //        } catch (InterruptedException | BrokenBarrierException e) {
    //            e.printStackTrace();
    //        }
    //    }, method).start();
    //
    //    TimeUnit.MILLISECONDS.sleep(100);
    //    synchronized (o) {
    //        // do nothing, just wait!
    //    }
    //}
    //
    //@Test
    //public void completableFuture() {
    //    method = new Object() {
    //    }.getClass().getEnclosingMethod().getName();
    //
    //    result = CompletableFuture.supplyAsync(() -> FibonacciUtil.fibonacci(N)).join();
    //}

    private String getMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}
