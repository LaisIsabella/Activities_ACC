package catalog;

import repository.AuthorizationRepository;
import java.util.List;

public class AuthorizationCatalog {

    private final AuthorizationRepository repository;
    private List<String> authorizedCoordinatorRCs;
    private List<String> authorizedSupervisorEmails;

    public AuthorizationCatalog() {
        this.repository = new AuthorizationRepository();
        this.authorizedCoordinatorRCs = repository.loadAuthorizedCoordinatorRCs();
        this.authorizedSupervisorEmails = repository.loadAuthorizedSupervisorEmails();
    }

    /**
     * Verifica se um RC está autorizado para ser coordenador
     */
    public boolean isCoordinatorRCAuthorized(String rc) {
        if (rc == null || rc.trim().isEmpty()) {
            return false;
        }
        
        String rcTrimmed = rc.trim();
        System.out.println("Verificando RC: [" + rcTrimmed + "]");
        System.out.println("RCs autorizados: " + authorizedCoordinatorRCs);
        
        boolean authorized = authorizedCoordinatorRCs.contains(rcTrimmed);
        System.out.println(authorized ? "✅ RC autorizado!" : " RC NÃO autorizado!");
        
        return authorized;
    }

    /**
     * Verifica se um email está autorizado para ser supervisor
     */
    public boolean isSupervisorEmailAuthorized(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.out.println("Email nulo ou vazio");
            return false;
        }
        
        String emailLower = email.trim().toLowerCase();
        
        boolean authorized = authorizedSupervisorEmails.contains(emailLower);
        
        return authorized;
    }
}
