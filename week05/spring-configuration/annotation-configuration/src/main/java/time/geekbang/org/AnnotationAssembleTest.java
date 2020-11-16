package time.geekbang.org;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import time.geekbang.org.controller.UserController;

public class AnnotationAssembleTest {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("applicationContext.xml").getBean(UserController.class).remove();
    }
}
