package time.geekbang.org.service;

import time.geekbang.org.dao.Dao;

import javax.annotation.Resource;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service {

    @Resource
    private Dao dao;

    @Override
    public void remove() {
        dao.remove();
        System.out.println(getClass().getName() + " # " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
