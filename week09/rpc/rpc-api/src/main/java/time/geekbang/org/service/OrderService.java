package time.geekbang.org.service;

import time.geekbang.org.bean.Order;

public interface OrderService {

    Order findById(Integer id);

    Order findWithException() throws RuntimeException;
}
