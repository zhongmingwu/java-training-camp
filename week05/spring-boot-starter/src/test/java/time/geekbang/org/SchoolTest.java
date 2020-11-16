package time.geekbang.org;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import time.geekbang.org.bean.School;
import time.geekbang.org.config.SchoolAutoConfiguration;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SchoolAutoConfiguration.class)
public class SchoolTest {

    @Autowired
    School school;

    @Test
    public void test() {
        System.out.println(school.toString());

        // Output
        // School(klasses=[Klass(id=0, name=klass1, students=[Student(id=0, name=student1)]), Klass(id=1, name=klass2, students=[Student(id=1, name=student2)]), Klass(id=2, name=klass2, students=[Student(id=2, name=student3)])])
    }
}
