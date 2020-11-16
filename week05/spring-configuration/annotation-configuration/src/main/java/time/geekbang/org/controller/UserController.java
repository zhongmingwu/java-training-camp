package time.geekbang.org.controller;

import org.springframework.stereotype.Controller;
import time.geekbang.org.service.UserService;

import javax.annotation.Resource;

@Controller
public class UserController {

    @Resource(name = "userService")
    private UserService userService;

    public void remove() {
        userService.remove();
        System.out.println(getClass().getName() + " # " + Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
