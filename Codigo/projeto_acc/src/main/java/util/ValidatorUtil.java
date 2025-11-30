package util;

import model.*;
import java.util.Date;

public class ValidatorUtil {

    public static boolean validateInstitutionalEmail(String email) {
        return email != null && email.endsWith("@unesp.br");
    }

    public static boolean validatePassword(String password) {
        if (password == null) {
            return false;
        }

        // Regex:
        // (?=.*[A-Z]) -> pelo menos uma maiúscula
        // (?=.*[a-z]) -> pelo menos uma minúscula
        // (?=.*\\d) -> pelo menos um número
        // (?=.*[@$!%*?&]) -> caractere especial
        // .{8,} -> mínimo 8 caracteres
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$";

        return password.matches(regex);
    }

    public static boolean validateCpf(String cpf) {
        if (cpf == null) {
            return false;
        }

        // Remove caracteres não numéricos (permite formatação como 123.456.789-10)
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (ex: 111.111.111-11)
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            // Calcula o primeiro dígito verificador
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito >= 10) {
                primeiroDigito = 0;
            }

            // Verifica o primeiro dígito
            if (Character.getNumericValue(cpf.charAt(9)) != primeiroDigito) {
                return false;
            }

            // Calcula o segundo dígito verificador
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito >= 10) {
                segundoDigito = 0;
            }

            // Verifica o segundo dígito
            return Character.getNumericValue(cpf.charAt(10)) == segundoDigito;

        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validateRc(String rc) {
        if (rc == null) {
            return false;
        }

        // Regex: somente dígitos e de 1 a 4 caracteres
        return rc.matches("^\\d{1,4}$");
    }

    public static boolean validateRa(String ra) {
        return ra != null && ra.matches("\\d{9}");
    }

    public static boolean validateNotNull(Object obj) {
        return obj != null;
    }

    public static int parseAndValidateHourLimit(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Informe um limite.");
        }

        if (!valor.matches("\\d+")) {
            throw new IllegalArgumentException("O limite deve ser numérico.");
        }

        int horas = Integer.parseInt(valor);

        if (horas < 0) {
            throw new IllegalArgumentException("O limite não pode ser negativo.");
        }

        if (horas > 200) {
            throw new IllegalArgumentException("Acima das diretrizes do curso.");
        }

        return horas;
    }

    /**
     * Valida o nome da atividade
     */
    public static boolean validateActivityName(String name) {
        return name != null && !name.isBlank();
    }

    /**
     * Valida o status da atividade
     */
    public static boolean validateStatus(Status status) {
        return status != null;
    }

    /**
     * Valida o tipo de atividade
     */
    public static boolean validateActivityType(ActivityType type) {
        return type != null;
    }

    /**
     * Valida o estudante
     */
    public static boolean validateStudent(Student student) {
        return student != null;
    }

    /**
     * Valida todos os campos de uma atividade
     */
    public static boolean validateActivity(
            String name,
            String description,
            Date date,
            int hours,
            Status status,
            ActivityType activityType,
            Student student,
            Document document) {

        return validateActivityName(name)
                && description != null && !description.isBlank()
                && date != null
                && hours > 0
                && validateStatus(status)
                && validateActivityType(activityType)
                && validateStudent(student)
                && document != null;
    }

    public static boolean isStudentEmailDuplicated(catalog.StudentCatalog catalog, String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return catalog.findStudentByEmail(email.trim()) != null;
    }

    /**
     * Verifica se já existe um estudante com o RA informado
     */
    public static boolean isStudentRADuplicated(catalog.StudentCatalog catalog, String ra) {
        if (ra == null || ra.trim().isEmpty()) {
            return false;
        }

        String raTrimmed = ra.trim();
        for (model.Student s : catalog.getStudents()) {
            if (s.getRa().equals(raTrimmed)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se já existe um estudante com o CPF informado
     */
    public static boolean isStudentCPFDuplicated(catalog.StudentCatalog catalog, String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }

        String cpfTrimmed = cpf.trim();
        for (model.Student s : catalog.getStudents()) {
            if (s.getCpf().equals(cpfTrimmed)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se já existe um supervisor com o email informado
     */
    public static boolean isSupervisorEmailDuplicated(catalog.SupervisorCatalog catalog, String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return catalog.findSupervisorByEmail(email.trim()) != null;
    }

    /**
     * Verifica se já existe um supervisor com o CPF informado
     */
    public static boolean isSupervisorCPFDuplicated(catalog.SupervisorCatalog catalog, String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }

        String cpfTrimmed = cpf.trim();
        for (model.Supervisor s : catalog.getSupervisors()) {
            if (s.getCpf().equals(cpfTrimmed)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se já existe um coordenador com o email informado
     */
    public static boolean isCoordinatorEmailDuplicated(catalog.CoordinatorCatalog catalog, String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return catalog.findCoordinatorByEmail(email.trim()) != null;
    }

    /**
     * Verifica se já existe um coordenador com o RC informado
     */
    public static boolean isCoordinatorRCDuplicated(catalog.CoordinatorCatalog catalog, String rc) {
        if (rc == null || rc.trim().isEmpty()) {
            return false;
        }

        String rcTrimmed = rc.trim();
        for (model.Coordinator c : catalog.getAll()) {
            if (c.getRc().equals(rcTrimmed)) {
                return true;
            }
        }
        return false;
    }

}
