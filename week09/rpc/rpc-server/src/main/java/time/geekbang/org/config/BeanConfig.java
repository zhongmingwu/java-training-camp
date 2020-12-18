package time.geekbang.org.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import time.geekbang.org.service.OrderService;
import time.geekbang.org.service.impl.OrderServiceImpl;

@Configuration
public class BeanConfig {

    @Bean("time.geekbang.org.service.OrderService")
    public OrderService orderService() {
        return new OrderServiceImpl();
    }
}
