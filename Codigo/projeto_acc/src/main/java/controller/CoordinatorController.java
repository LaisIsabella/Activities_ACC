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
    public boolean validateCoordinator(String name, String email, String password, String rc) {
        boolean validEmail = ValidatorUtil.validateInstitutionalEmail(email);
        boolean validPassword = ValidatorUtil.validatePassword(password);
        boolean validName = (name != null && !name.isEmpty());
        boolean validRc = ValidatorUtil.validateRc(rc);

        return validName && validEmail && validPassword && validRc;
    }


    // CE03 - Valida formato de e-mail e senha para login
    public boolean validateCoordinatorLogin(String email, String password) {
        boolean validEmail = ValidatorUtil.validateInstitutionalEmail(email);
        boolean validPassword = ValidatorUtil.validatePassword(password);
        return validEmail && validPassword;
    }

    // CE03 - Efetua o login do coordenador
    public boolean authenticateCoordinator(String email, String password) {
        // 1. Valida credenciais
        boolean validData = validateCoordinatorLogin(email, password);

        if (!validData) {
            System.out.println("Formato de dados inválidos no Login");
            return false;
        }

        // 2. Procura coordenador pelo e-mail
        Coordinator coordinator = coordinatorCatalog.findCoordinatorByEmail(email);

        // 3. Retorna verdadeiro se encontrado e senha confere
        if (coordinator != null && coordinator.getPassword().equals(password)) {
            return true;
        }
        
        System.out.println("Coordenador não encontrado ou senha inválida");
        return false;
    }
    
    public boolean createCoordinator(String name, String email, String password, String rc) {
    // 1. Valida os dados do coordenador
    boolean validData = validateCoordinator(name, email, password, rc);

    if (!validData) {
        return false;
    }

    // 2. Cria o objeto Coordinator e adiciona ao catálogo
    Coordinator coordinator = new Coordinator(name, email, password, rc);
    return coordinatorCatalog.addCoordinator(coordinator);
}


}
