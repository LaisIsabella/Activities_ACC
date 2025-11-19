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

    // Busca estudante por e-mail
    public Student findStudentByEmail(String email) {
        return studentCatalog.findStudentByEmail(email);
    }

    // Cria um novo estudante e adiciona ao catálogo
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
}
