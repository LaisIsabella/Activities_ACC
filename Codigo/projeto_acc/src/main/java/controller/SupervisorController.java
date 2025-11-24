package controller;

import catalog.SupervisorCatalog;
import model.Supervisor;
import util.ValidatorUtil;

public class SupervisorController {

    private SupervisorCatalog supervisorCatalog;

    public SupervisorController(SupervisorCatalog supervisorCatalog) {
        this.supervisorCatalog = supervisorCatalog;
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
    public boolean loginSupervisor(String email, String password) {
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
            return false;
        }

        // 2. Cria o objeto Supervisor e adiciona ao catálogo
        Supervisor supervisor = new Supervisor(name, email, password, cpf);
        return supervisorCatalog.addSupervisor(supervisor);
    }

    public Supervisor authenticateSupervisor(String email, String password) {
        for (Supervisor s : supervisorCatalog.getSupervisors()) {
            if (s.getEmail().equalsIgnoreCase(email) && s.getPassword().equals(password)) {
                return s;
            }
        }
        return null;
    }

}
