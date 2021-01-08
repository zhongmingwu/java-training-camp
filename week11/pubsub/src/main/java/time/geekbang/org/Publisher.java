package time.geekbang.org;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
@AllArgsConstructor
public class Publisher {

    private final JedisPool jedisPool;
    private final String channel;

    public void publish(@NonNull Order order) {
        try (Jedis jedis = jedisPool.getResource()) {
            log.info("publish : {}", order);
            jedis.publish(channel, JSON.toJSONString(order));
        }
    }
}
