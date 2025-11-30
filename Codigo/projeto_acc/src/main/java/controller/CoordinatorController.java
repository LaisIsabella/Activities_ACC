package controller;

import catalog.AuthorizationCatalog;
import catalog.CoordinatorCatalog;
import model.Coordinator;
import util.ValidatorUtil;

public class CoordinatorController {

    private CoordinatorCatalog coordinatorCatalog;
    private AuthorizationCatalog authorizationCatalog;

    public CoordinatorController(CoordinatorCatalog coordinatorCatalog) {
        this.coordinatorCatalog = coordinatorCatalog;
        this.authorizationCatalog = new AuthorizationCatalog();
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
            System.out.println("❌ Dados inválidos para coordenador");
            return false;
        }

        // 2. ✅ VERIFICA DUPLICIDADE DE EMAIL
        if (ValidatorUtil.isCoordinatorEmailDuplicated(coordinatorCatalog, email)) {
            System.out.println("❌ Email já cadastrado: " + email);
            return false;
        }

        // 3. ✅ VERIFICA DUPLICIDADE DE RC
        if (ValidatorUtil.isCoordinatorRCDuplicated(coordinatorCatalog, rc)) {
            System.out.println("❌ RC já cadastrado: " + rc);
            return false;
        }

        // 4. ✅ VERIFICA SE O RC ESTÁ AUTORIZADO
        if (!authorizationCatalog.isCoordinatorRCAuthorized(rc)) {
            System.out.println("❌ RC não autorizado: " + rc);
            return false;
        }

        // 5. Cria o objeto Coordinator e adiciona ao catálogo
        Coordinator coordinator = new Coordinator(name, email, password, rc);
        return coordinatorCatalog.addCoordinator(coordinator);
    }
}