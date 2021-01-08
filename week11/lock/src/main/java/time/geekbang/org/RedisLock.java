package time.geekbang.org;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;

public class RedisLock {

    private enum EnumSingleton {

        INSTANCE;

        private final RedisLock instance;

        EnumSingleton() {
            instance = new RedisLock();
        }

        public RedisLock getSingleton() {
            return instance;
        }
    }

    public static RedisLock getInstance() {
        return EnumSingleton.INSTANCE.getSingleton();
    }

    private final JedisPool jedisPool = new JedisPool();

    public boolean lock(String lock, int seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            return "OK".equals(jedis.set(lock, lock, "NX", "EX", seconds));
        }
    }

    public boolean release(String lock) {
        String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.eval(luaScript, Collections.singletonList(lock), Collections.singletonList(lock)).equals(1L);
        }
    }
}
