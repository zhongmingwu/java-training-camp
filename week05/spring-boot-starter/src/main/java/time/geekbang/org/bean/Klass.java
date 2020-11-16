package time.geekbang.org.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Klass {
    private int id;
    private String name;
    private List<Student> students = new ArrayList<>();

    public Klass(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addStudent(Student student) {
        students.add(student);
    }
}
