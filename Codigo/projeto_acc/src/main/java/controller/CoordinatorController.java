package controller;

import catalog.CoordinatorCatalog;
import model.Coordinator;
import util.ValidatorUtil;

public class CoordinatorController {

    private CoordinatorCatalog coordinatorCatalog;

    public CoordinatorController(CoordinatorCatalog coordinatorCatalog) {
        this.coordinatorCatalog = coordinatorCatalog;
    }

    public CoordinatorCatalog getCoordinatorCatalog() {
        return coordinatorCatalog;
    }

    public void setCoordinatorCatalog(CoordinatorCatalog coordinatorCatalog) {
        this.coordinatorCatalog = coordinatorCatalog;
    }


    // Validação dos dados de cadastro (usado no registro)
    public boolean validateCoordinator(String name, String email, String password, int rc) {
        boolean validEmail = ValidatorUtil.validateInstitutionalEmail(email);
        boolean validPassword = ValidatorUtil.validatePassword(password);
        boolean validName = (name != null && !name.isEmpty());
        boolean validRc = (rc > 0);

        return validName && validEmail && validPassword && validRc;
    }


    // CE03 - Valida formato de e-mail e senha para login
    public boolean validateCoordinatorLogin(String email, String password) {
        boolean validEmail = ValidatorUtil.validateInstitutionalEmail(email);
        boolean validPassword = ValidatorUtil.validatePassword(password);
        return validEmail && validPassword;
    }

    // CE03 - Efetua o login do coordenador
    public boolean loginCoordinator(String email, String password) {
        // 1. Valida credenciais
        boolean validData = validateCoordinatorLogin(email, password);

        if (!validData) {
            return false;
        }

        // 2. Procura coordenador pelo e-mail
        Coordinator coordinator = coordinatorCatalog.findCoordinatorByEmail(email);

        // 3. Retorna verdadeiro se encontrado e senha confere
        if (coordinator != null && coordinator.getPassword().equals(password)) {
            return true;
        }

        return false;
    }
    
    public boolean createCoordinator(String name, String email, String password, int rc) {
    // 1. Valida os dados do coordenador
    boolean validData = validateCoordinator(name, email, password, rc);

    if (!validData) {
        return false;
    }

    // 2. Cria o objeto Coordinator e adiciona ao catálogo
    Coordinator coordinator = new Coordinator(name, email, password, rc);
    return coordinatorCatalog.addCoordinator(coordinator);
}
    
    public Coordinator authenticateCoordinator(String email, String password) {
    for (Coordinator c : coordinatorCatalog.getAll()) {
        if (c.getEmail().equalsIgnoreCase(email) && c.getPassword().equals(password)) {
            return c;
        }
    }
    return null;
}


}
