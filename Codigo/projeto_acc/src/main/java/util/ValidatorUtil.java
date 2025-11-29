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
        return cpf != null && cpf.matches("\\d{11}");
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

    // ============================
    // ✅ VALIDAÇÕES DE ACTIVITY
    // ============================

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

}