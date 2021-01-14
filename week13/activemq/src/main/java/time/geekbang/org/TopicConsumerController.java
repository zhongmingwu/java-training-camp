package time.geekbang.org;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopicConsumerController {

    @JmsListener(destination = "amq.topic")
    public void topicConsumer1(Book book) {
        System.out.println("topicConsumer1 receive message : " + book);
    }

    @JmsListener(destination = "amq.topic")
    public void topicConsumer2(Book book) {
        System.out.println("topicConsumer2 receive message : " + book);
    }
}
