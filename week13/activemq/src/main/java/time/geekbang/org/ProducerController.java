package time.geekbang.org;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import javax.jms.Topic;

@RestController
public class ProducerController {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue queue;

    @Autowired
    private Topic topic;

    @GetMapping("/sendQueueMsg")
    public void sendQueueMsg(Book book) {
        jmsMessagingTemplate.convertAndSend(queue, book);
    }

    @GetMapping("/sendTopicMsg")
    public void sendTopicMsg(Book book) {
        jmsMessagingTemplate.convertAndSend(topic, book);
    }
}
