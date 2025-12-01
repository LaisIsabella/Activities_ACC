package controller;

import catalog.AuthorizationCatalog;
import catalog.SupervisorCatalog;
import model.Supervisor;
import util.ValidatorUtil;

public class SupervisorController {

    private SupervisorCatalog supervisorCatalog;
    private AuthorizationCatalog authorizationCatalog;

    public SupervisorController(SupervisorCatalog supervisorCatalog) {
        this.supervisorCatalog = supervisorCatalog;
        this.authorizationCatalog = new AuthorizationCatalog();
    }

    public SupervisorCatalog getSupervisorCatalog() {
        return supervisorCatalog;
    }

    public void setSupervisorCatalog(SupervisorCatalog supervisorCatalog) {
        this.supervisorCatalog = supervisorCatalog;
    }

    // Validação dos dados de cadastro (usado no registro)
    public boolean validateSupervisor(String name, String email, String password, String cpf) {
        boolean validEmail = ValidatorUtil.validateInstitutionalEmail(email);
        boolean validPassword = ValidatorUtil.validatePassword(password);
        boolean validName = (name != null && !name.isEmpty());
        boolean validCpf = ValidatorUtil.validateCpf(cpf);

        return validName && validEmail && validPassword && validCpf;
    }

    // Valida formato de e-mail e senha para login
    public boolean validateSupervisorLogin(String email, String password) {
        boolean validEmail = ValidatorUtil.validateInstitutionalEmail(email);
        boolean validPassword = ValidatorUtil.validatePassword(password);
        return validEmail && validPassword;
    }

    // Efetua o login do supervisor
    public boolean authenticateSupervisor(String email, String password) {
        // 1. Valida credenciais
        boolean validData = validateSupervisorLogin(email, password);

        if (!validData) {
            return false;
        }

        // 2. Procura supervisor pelo e-mail
        Supervisor supervisor = supervisorCatalog.findSupervisorByEmail(email);

        // 3. Retorna verdadeiro se encontrado e senha confere
        if (supervisor != null && supervisor.getPassword().equals(password)) {
            return true;
        }

        return false;
    }

    // Busca supervisor por e-mail
    public Supervisor findSupervisorByEmail(String email) {
        return supervisorCatalog.findSupervisorByEmail(email);
    }

    // Cria um novo supervisor e adiciona ao catálogo
    public boolean createSupervisor(String name, String email, String password, String cpf) {
        // 1. Valida os dados do supervisor
        boolean validData = validateSupervisor(name, email, password, cpf);

        if (!validData) {
            System.out.println("❌ Dados inválidos para supervisor");
            return false;
        }

        if (ValidatorUtil.isSupervisorEmailDuplicated(supervisorCatalog, email)) {
            System.out.println("❌ Email já cadastrado: " + email);
            return false;
        }

        if (ValidatorUtil.isSupervisorCPFDuplicated(supervisorCatalog, cpf)) {
            System.out.println("❌ CPF já cadastrado: " + cpf);
            return false;
        }

        if (!authorizationCatalog.isSupervisorEmailAuthorized(email)) {
            System.out.println("❌ Email não autorizado: " + email);
            return false;
        }

        Supervisor supervisor = new Supervisor(name, email, password, cpf);
        return supervisorCatalog.addSupervisor(supervisor);
    }
}