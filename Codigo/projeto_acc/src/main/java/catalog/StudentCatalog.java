package catalog;

import model.Student;
import repository.StudentRepository;
import java.util.List;

public class StudentCatalog {

    private List<Student> students;
    private StudentRepository repo;

    public StudentCatalog() {
        this.repo = new StudentRepository();
        this.students = repo.loadAll(); // ⬅️ agora carrega do TXT
    }

    public boolean addStudent(Student student) {
        if (student != null) {
            students.add(student);
            repo.saveAll(students); // ⬅️ agora salva no TXT
            return true;
        }
        return false;
    }

    public Student findStudentByEmail(String email) {
        for (Student s : students) {
            if (s.getEmail().equalsIgnoreCase(email)) {
                return s;
            }
        }
        return null;
    }

    public void updateStudent(Student student) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getEmail().equalsIgnoreCase(student.getEmail())) {
                students.set(i, student);
                repo.saveAll(students); // Salva a lista atualizada
                return;
            }
        }
    }

    public Student findByQuery(String query) {
        if (query == null || query.isBlank()) {
            return null;
        }

        query = query.toLowerCase();

        for (Student s : students) {

            if (s.getEmail().equalsIgnoreCase(query)) {
                return s;
            }

            if (s.getName().toLowerCase().contains(query)) {
                return s;
            }

            if (String.valueOf(s.getRa()).contains(query)) {
                return s;
            }
        }

        return null;
    }

    public List<Student> getStudents() {
        return students;
    }
}
