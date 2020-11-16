package time.geekbang.org.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import time.geekbang.org.bean.Klass;
import time.geekbang.org.bean.School;
import time.geekbang.org.bean.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@ConditionalOnClass(School.class)
@EnableConfigurationProperties(SchoolProperties.class)
@ConditionalOnProperty(prefix = "school", value = "enabled", havingValue = "true")
@PropertySource("classpath:application.properties")
public class SchoolAutoConfiguration {

    @Autowired
    private SchoolProperties schoolProperties;

    @Bean
    public School school() {
        List<Integer> studentIds = schoolProperties.getStudentIds();
        List<String> studentNames = schoolProperties.getStudentNames();
        List<Integer> classIds = schoolProperties.getKlassIds();
        List<String> classNames = schoolProperties.getKlassNames();
        List<Map<String, Integer>> studentOfClass = schoolProperties.getStudentOfKlass();

        List<Student> students = new ArrayList<>(studentIds.size());
        for (int i = 0; i < studentIds.size(); i++) {
            students.add(new Student(studentIds.get(i), studentNames.get(i)));
        }

        List<Klass> klasses = new ArrayList<>();
        for (int i = 0; i < classIds.size(); i++) {
            klasses.add(new Klass(classIds.get(i), classNames.get(i)));
        }

        for (Map<String, Integer> info : studentOfClass) {
            klasses.get(info.get("klassId"))
                    .addStudent(students.get(info.get("studentId")));
        }

        return new School(klasses);
    }
}
