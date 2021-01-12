package time.geekbang.org;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

import static time.geekbang.org.Constant.TOPIC;

@Slf4j
@Component
public class Consumer {

    @JmsListener(destination = TOPIC)
    public void receive(final Map<?, ?> message) {
        log.info("receive from {}, message={}", TOPIC, message);
    }
}
