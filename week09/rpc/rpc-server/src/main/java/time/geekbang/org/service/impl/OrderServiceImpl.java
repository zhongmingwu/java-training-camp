package time.geekbang.org.service.impl;


import time.geekbang.org.bean.Order;
import time.geekbang.org.service.OrderService;

public class OrderServiceImpl implements OrderService {

    @Override
    public Order findById(Integer id) {
        return new Order(System.currentTimeMillis());
    }

    @Override
    public Order findWithException() throws RuntimeException {
        throw new RuntimeException("server timestamp : " + System.currentTimeMillis());
    }
}
