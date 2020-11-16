package time.geekbang.org;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import time.geekbang.org.controller.Controller;

public class AnnotationAssembleTest {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        applicationContext.getBean(Controller.class).remove();
    }
}
