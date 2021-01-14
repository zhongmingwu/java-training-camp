package time.geekbang.org;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueueConsumerController {

    @JmsListener(destination = "amq.queue")
    public void queueConsumer1(Book book) {
        System.out.println("queueConsumer1 receive message : " + book);
    }

    @JmsListener(destination = "amq.queue")
    public void queueConsumer2(Book book) {
        System.out.println("queueConsumer2 receive message : " + book);
    }
}
