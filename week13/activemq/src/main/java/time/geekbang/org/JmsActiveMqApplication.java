package time.geekbang.org;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JmsActiveMqApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(JmsActiveMqApplication.class);
        application.run(args);
    }
}
