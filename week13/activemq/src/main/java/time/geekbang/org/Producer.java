package time.geekbang.org;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class Producer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(final String topic, final Map<?, ?> message) {
        log.info("send to {}, message={}", topic, message);
        jmsTemplate.convertAndSend(topic, message);
    }
}
