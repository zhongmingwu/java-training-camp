package time.geekbang.org;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

@Slf4j
@AllArgsConstructor
public class Subscriber {

    private final JedisPool jedisPool;
    private final String channel;

    public void subscribe() {
        new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        log.info("receive : {}", JSON.parseObject(message, Order.class));
                    }
                }, channel);
            }
        }, "Subscriber-Thread").start();
    }
}
