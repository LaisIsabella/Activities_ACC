package controller;

import catalog.StudentCatalog;
import model.Student;
import util.ValidatorUtil;

public class StudentController {

    private StudentCatalog studentCatalog;

    public StudentController(StudentCatalog studentCatalog) {
        this.studentCatalog = studentCatalog;
    }

    public StudentCatalog getStudentCatalog() {
        return studentCatalog;
    }

    public void setStudentCatalog(StudentCatalog studentCatalog) {
        this.studentCatalog = studentCatalog;
    }

    public boolean createStudent(String name, String email, String password, String cpf, int ra) {
        // 1. Valida os dados do estudante
        boolean validData = validateStudent(name, email, password, cpf, ra);

        if (!validData) {
            return false;
        }

        // 2. Cria o objeto Student e adiciona ao catálogo
        Student student = new Student(name, email, password, cpf, ra);
        return studentCatalog.addStudent(student);
    }

    // Validação dos dados de cadastro (usado no registro)
    public boolean validateStudent(String name, String email, String password, String cpf, int ra) {
        boolean validEmail = ValidatorUtil.validateInstitutionalEmail(email);
        boolean validPassword = ValidatorUtil.validatePassword(password);
        boolean validName = (name != null && !name.isEmpty());
        boolean validCpf = ValidatorUtil.validateCpf(cpf);
        boolean validRa = ValidatorUtil.validateRa(ra);

        return validName && validEmail && validPassword && validCpf && validRa;
    }

    // Valida formato de e-mail e senha para login
    public boolean validateStudentLogin(String email, String password) {
        boolean validEmail = ValidatorUtil.validateInstitutionalEmail(email);
        boolean validPassword = ValidatorUtil.validatePassword(password);
        return validEmail && validPassword;
    }

    // Efetua o login do estudante
    public boolean loginStudent(String email, String password) {
        // 1. Valida credenciais
        boolean validData = validateStudentLogin(email, password);

        if (!validData) {
            return false;
        }

        // 2. Procura estudante pelo e-mail
        Student student = studentCatalog.findStudentByEmail(email);

        // 3. Retorna verdadeiro se encontrado e senha confere
        if (student != null && student.getPassword().equals(password)) {
            return true;
        }

        return false;
    }

    public Student authenticateStudent(String email, String password) {
        for (Student s : studentCatalog.getStudents()) {
            if (s.getEmail().equalsIgnoreCase(email) && s.getPassword().equals(password)) {
                return s;
            }
        }
        return null;
    }

    public boolean addStudentHours(Student student, int hours) {
        if (student == null || hours <= 0) {
            return false;
        }

        int newTotal = student.getTotalHours() + hours;
        student.setTotalHours(newTotal);
        studentCatalog.updateStudent(student);
        return true;
    }

}
