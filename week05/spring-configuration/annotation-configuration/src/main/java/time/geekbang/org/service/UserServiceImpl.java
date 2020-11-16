package time.geekbang.org.service;

import org.springframework.stereotype.Service;
import time.geekbang.org.dao.UserDao;

import javax.annotation.Resource;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource(name = "userDao")
    private UserDao userDao;

    @Override
    public void remove() {
        userDao.remove();
        System.out.println(getClass().getName() + " # " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
