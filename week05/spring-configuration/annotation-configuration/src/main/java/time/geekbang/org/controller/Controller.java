package time.geekbang.org.controller;

import time.geekbang.org.service.Service;

import javax.annotation.Resource;

@org.springframework.stereotype.Controller
public class Controller {

    @Resource
    private Service service;

    public void remove() {
        service.remove();
        System.out.println(getClass().getName() + " # " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
