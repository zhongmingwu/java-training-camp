package time.geekbang.org;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisLockTest {

    @Test
    public void test() throws InterruptedException {
        int count = 20;
        Counter counter = new Counter(count);
        ExecutorService pool = Executors.newFixedThreadPool(4);

        for (int i = 0; i < count; i++) {
            pool.execute(() -> {
                while (true) {
                    try {
                        counter.decrease();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (RuntimeException e) {
                        break;
                    }
                }
            });
        }

        pool.shutdown();
        while (!pool.isTerminated()) {
            TimeUnit.SECONDS.sleep(1);
        }

        // output
        //  19:54:22.309 [pool-1-thread-3] INFO time.geekbang.org.Counter - decrease successfully, current count: 19
        //  19:54:22.322 [pool-1-thread-2] INFO time.geekbang.org.Counter - decrease successfully, current count: 18
        //  19:54:22.322 [pool-1-thread-3] INFO time.geekbang.org.Counter - decrease successfully, current count: 17
        //  19:54:22.323 [pool-1-thread-4] INFO time.geekbang.org.Counter - decrease successfully, current count: 16
        //  19:54:22.323 [pool-1-thread-2] INFO time.geekbang.org.Counter - decrease successfully, current count: 15
        //  19:54:22.324 [pool-1-thread-4] INFO time.geekbang.org.Counter - decrease successfully, current count: 14
        //  19:54:22.324 [pool-1-thread-2] INFO time.geekbang.org.Counter - decrease successfully, current count: 13
        //  19:54:22.324 [pool-1-thread-1] INFO time.geekbang.org.Counter - decrease successfully, current count: 12
        //  19:54:22.325 [pool-1-thread-4] INFO time.geekbang.org.Counter - decrease successfully, current count: 11
        //  19:54:22.325 [pool-1-thread-2] INFO time.geekbang.org.Counter - decrease successfully, current count: 10
        //  19:54:22.326 [pool-1-thread-3] INFO time.geekbang.org.Counter - decrease successfully, current count: 9
        //  19:54:22.326 [pool-1-thread-2] INFO time.geekbang.org.Counter - decrease successfully, current count: 8
        //  19:54:22.327 [pool-1-thread-1] INFO time.geekbang.org.Counter - decrease successfully, current count: 7
        //  19:54:22.327 [pool-1-thread-2] INFO time.geekbang.org.Counter - decrease successfully, current count: 6
        //  19:54:22.327 [pool-1-thread-4] INFO time.geekbang.org.Counter - decrease successfully, current count: 5
        //  19:54:22.328 [pool-1-thread-2] INFO time.geekbang.org.Counter - decrease successfully, current count: 4
        //  19:54:22.328 [pool-1-thread-4] INFO time.geekbang.org.Counter - decrease successfully, current count: 3
        //  19:54:22.329 [pool-1-thread-4] INFO time.geekbang.org.Counter - decrease successfully, current count: 2
        //  19:54:22.330 [pool-1-thread-2] INFO time.geekbang.org.Counter - decrease successfully, current count: 1
        //  19:54:22.330 [pool-1-thread-4] INFO time.geekbang.org.Counter - decrease successfully, current count: 0
    }

}