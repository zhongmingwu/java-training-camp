package time.geekbang.org;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

import java.util.HashMap;
import java.util.Map;

import static time.geekbang.org.Constant.TOPIC;

@Slf4j
@EnableJms
@SpringBootApplication
public class Application implements ApplicationRunner {

    @Autowired
    private Producer producer;

    @Override
    public void run(ApplicationArguments args) {
        Map<String, String> map = new HashMap<>(1);
        map.put("name", "tom");
        producer.send(TOPIC, map);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // output 
    //  2021-01-12 11:38:44.349  INFO 21736 --- [           main] time.geekbang.org.Application            : No active profile set, falling back to default profiles: default
    //  2021-01-12 11:38:45.004  INFO 21736 --- [           main] time.geekbang.org.Application            : Started Application in 0.937 seconds (JVM running for 1.483)
    //  2021-01-12 11:38:45.006  INFO 21736 --- [           main] time.geekbang.org.Producer               : send to test_topic, message={name=tom}
    //  2021-01-12 11:38:45.121  INFO 21736 --- [enerContainer-1] time.geekbang.org.Consumer               : receive from test_topic, message={name=tom}
}
