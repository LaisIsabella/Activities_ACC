package repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorizationRepository {

    private final File coordinatorsFile = new File("data/authorized_coordinators.txt");
    private final File supervisorsFile = new File("data/authorized_supervisors.txt");

    public AuthorizationRepository() {
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdir();
        }

    }

    /**
     * Carrega todos os RCs autorizados do arquivo
     */
    public List<String> loadAuthorizedCoordinatorRCs() {
        List<String> result = new ArrayList<>();

        if (!coordinatorsFile.exists()) {
            return result;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(coordinatorsFile))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                String trimmed = line.trim();

                if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                    result.add(trimmed);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Carrega todos os emails autorizados do arquivo
     */
    public List<String> loadAuthorizedSupervisorEmails() {
        List<String> result = new ArrayList<>();

        if (!supervisorsFile.exists()) {
            System.out.println("Arquivo de supervisores não encontrado!");
            return result;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(supervisorsFile))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                String trimmed = line.trim();

                if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                    String emailLower = trimmed.toLowerCase();
                    result.add(emailLower);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo de supervisores:");
            e.printStackTrace();
        }

        System.out.println("?Total de emails carregados: " + result.size());

        return result;
    }
}
