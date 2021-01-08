package time.geekbang.org;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class Counter {
    private final static String LOCK_NAME = "count_lock";
    private final static int EXPIRE_SECONDS = 3;

    private volatile int count;

    public void decrease() throws InterruptedException {
        if (count <= 0) {
            throw new RuntimeException("count <= 0");
        }

        if (!RedisLock.getInstance().lock(LOCK_NAME, EXPIRE_SECONDS)) {
            return;
        }

        log.info("decrease successfully, current count: {}", --count);
        RedisLock.getInstance().release(LOCK_NAME);
    }
}
