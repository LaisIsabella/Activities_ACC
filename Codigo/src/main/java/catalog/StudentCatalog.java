package catalog;

import java.util.ArrayList;
import java.util.List;
import model.Student;

public class StudentCatalog {
    private List<Student> students;

    public StudentCatalog() {
        this.students = new ArrayList<>();
    }

    public StudentCatalog(List<Student> students) {
        this.students = students;
    }

    public boolean addStudent(Student student) {
        if (student != null) {
            students.add(student);
            return true;
        }
        return false;
    }

    public Student findStudentByEmail(String email) {
        for (Student student : students) {
            if (student.getEmail().equalsIgnoreCase(email)) {
                return student;
            }
        }
        return null;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
