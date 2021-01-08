package time.geekbang.org;

import org.junit.Test;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;

public class PubSubTest {

    @Test
    public void test() throws InterruptedException {
        JedisPool jedisPool = new JedisPool();
        String channel = "ORDER";

        new Subscriber(jedisPool, channel).subscribe();

        Publisher publisher = new Publisher(jedisPool, channel);
        for (int i = 0; i < 10; i++) {
            Order order = Order.builder().id(i).build();
            publisher.publish(order);
        }

        TimeUnit.SECONDS.sleep(1);

        // output
        //  20:08:01.972 [main] INFO time.geekbang.org.Publisher - publish : Order(id=0)
        //  20:08:02.072 [main] INFO time.geekbang.org.Publisher - publish : Order(id=1)
        //  20:08:02.072 [main] INFO time.geekbang.org.Publisher - publish : Order(id=2)
        //  20:08:02.072 [main] INFO time.geekbang.org.Publisher - publish : Order(id=3)
        //  20:08:02.072 [main] INFO time.geekbang.org.Publisher - publish : Order(id=4)
        //  20:08:02.072 [main] INFO time.geekbang.org.Publisher - publish : Order(id=5)
        //  20:08:02.073 [main] INFO time.geekbang.org.Publisher - publish : Order(id=6)
        //  20:08:02.073 [main] INFO time.geekbang.org.Publisher - publish : Order(id=7)
        //  20:08:02.073 [main] INFO time.geekbang.org.Publisher - publish : Order(id=8)
        //  20:08:02.073 [main] INFO time.geekbang.org.Publisher - publish : Order(id=9)
        //  20:08:02.090 [Subscriber-Thread] INFO time.geekbang.org.Subscriber - receive : Order(id=0)
        //  20:08:02.090 [Subscriber-Thread] INFO time.geekbang.org.Subscriber - receive : Order(id=1)
        //  20:08:02.090 [Subscriber-Thread] INFO time.geekbang.org.Subscriber - receive : Order(id=2)
        //  20:08:02.090 [Subscriber-Thread] INFO time.geekbang.org.Subscriber - receive : Order(id=3)
        //  20:08:02.090 [Subscriber-Thread] INFO time.geekbang.org.Subscriber - receive : Order(id=4)
        //  20:08:02.090 [Subscriber-Thread] INFO time.geekbang.org.Subscriber - receive : Order(id=5)
        //  20:08:02.090 [Subscriber-Thread] INFO time.geekbang.org.Subscriber - receive : Order(id=6)
        //  20:08:02.091 [Subscriber-Thread] INFO time.geekbang.org.Subscriber - receive : Order(id=7)
        //  20:08:02.091 [Subscriber-Thread] INFO time.geekbang.org.Subscriber - receive : Order(id=8)
        //  20:08:02.091 [Subscriber-Thread] INFO time.geekbang.org.Subscriber - receive : Order(id=9)
    }
}
