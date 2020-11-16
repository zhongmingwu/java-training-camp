package time.geekbang.org.week04;

import com.google.common.base.Stopwatch;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class AsyncExecutionTest {

    private static final int N = 36;
    private static final long FIBONACCI_N = 14930352L;
    private static final long SLEEP_MS = 500L;
    private static final Stopwatch STOP_WATCH = Stopwatch.createUnstarted();

    private String name;
    private long fibonacci;
    private volatile boolean isDone;

    @Before
    public void setUp() {
        fibonacci = Long.MIN_VALUE;
        STOP_WATCH.start();
    }

    @After
    public void destroy() {
        System.out.printf("invoked_method=[%s], elapsed_ms=%s\n", name, STOP_WATCH.elapsed(TimeUnit.MILLISECONDS));
        assertEquals(FIBONACCI_N, fibonacci);
        STOP_WATCH.reset();
    }

    @Test
    public void m_01_volatile() {
        new Thread(() -> {
            fibonacci = FibonacciUtil.fibonacci(N);
            isDone = true;
        }, name = getMethodName()).start();

        while (!isDone) {
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(50));
        }
        isDone = false;
    }

    @Test
    public void m_02_join() throws InterruptedException {
        Thread thread = new Thread(() -> fibonacci = FibonacciUtil.fibonacci(N), name = getMethodName());
        thread.start();
        thread.join();
    }

    @Test
    public void m_03_synchronized() throws InterruptedException {
        new Thread(() -> {
            synchronized (this) {
                try {
                    TimeUnit.MILLISECONDS.sleep(SLEEP_MS * 2);
                    fibonacci = FibonacciUtil.fibonacci(N);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, name = getMethodName()).start();

        TimeUnit.MILLISECONDS.sleep(SLEEP_MS);
        synchronized (this) {
        }
    }

    @Test
    public void m_04_notify() throws InterruptedException {
        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(SLEEP_MS);
                synchronized (this) {
                    fibonacci = FibonacciUtil.fibonacci(N);
                    notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, name = getMethodName()).start();

        synchronized (this) {
            wait();
        }
    }

    @Test(expected = InterruptedException.class)
    public void m_05_interrupt() throws InterruptedException {
        Thread thread = Thread.currentThread();
        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(SLEEP_MS);
                fibonacci = FibonacciUtil.fibonacci(N);
                thread.interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, name = getMethodName()).start();

        synchronized (this) {
            wait();
        }
    }

    @Test
    public void m_06_lock() throws InterruptedException {
        Lock lock = new ReentrantLock();
        new Thread(() -> {
            try {
                lock.lock();
                TimeUnit.MILLISECONDS.sleep(SLEEP_MS * 2);
                fibonacci = FibonacciUtil.fibonacci(N);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, name = getMethodName()).start();

        TimeUnit.MILLISECONDS.sleep(SLEEP_MS);
        try {
            lock.lock();
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void m_07_condition() throws InterruptedException {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(SLEEP_MS);
                lock.lock();
                fibonacci = FibonacciUtil.fibonacci(N);
                condition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, name = getMethodName()).start();

        try {
            lock.lock();
            condition.await();
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void m_08_semaphore() throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);
        new Thread(() -> {
            try {
                semaphore.acquire();
                TimeUnit.MILLISECONDS.sleep(SLEEP_MS * 2);
                fibonacci = FibonacciUtil.fibonacci(N);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }, name = getMethodName()).start();

        TimeUnit.MILLISECONDS.sleep(SLEEP_MS);
        try {
            semaphore.acquire();
        } finally {
            semaphore.release();
        }
    }

    @Test
    public void m_09_countDownLatch() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(SLEEP_MS);
                fibonacci = FibonacciUtil.fibonacci(N);
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, name = getMethodName()).start();

        countDownLatch.await();
    }

    @Test
    public void m_10_cyclicBarrier() throws InterruptedException {
        Object o = new Object();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(1, () -> {
            synchronized (o) {
                try {
                    TimeUnit.MILLISECONDS.sleep(SLEEP_MS * 2);
                    fibonacci = FibonacciUtil.fibonacci(N);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        new Thread(() -> {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }, name = getMethodName()).start();

        TimeUnit.MILLISECONDS.sleep(SLEEP_MS);
        synchronized (o) {
        }
    }

    @Test
    public void m_11_futureTask() throws ExecutionException, InterruptedException {
        FutureTask<Long> task = new FutureTask<>(() -> FibonacciUtil.fibonacci(N));
        new Thread(task, name = getMethodName()).start();
        fibonacci = task.get();
    }

    @Test
    public void m_12_executorService() throws ExecutionException, InterruptedException {
        name = getMethodName();
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = new ThreadPoolExecutor(
                processors, processors * 2,
                30, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
        Future<Long> future = executorService.submit(() -> FibonacciUtil.fibonacci(N));
        executorService.shutdown();
        fibonacci = future.get();
    }

    @Test
    public void m_13_completableFuture() {
        name = getMethodName();
        fibonacci = CompletableFuture.supplyAsync(() -> FibonacciUtil.fibonacci(N)).join();
    }

    private static String getMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}
