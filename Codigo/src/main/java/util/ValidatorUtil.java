package util;

public class ValidatorUtil {

    public static boolean validateInstitutionalEmail(String email) {
        return email != null && email.endsWith("@unesp.br");
    }

    public static boolean validatePassword(String password) {
        // Exemplo simples de verificação: mínimo 8 caracteres
        return password != null && password.length() >= 8;
    }
    public static boolean validateCpf(String cpf) {
        return cpf != null && cpf.matches("\\d{11}");
}

    public static boolean validateRa(int ra) {
        return ra > 0;
    }

}
