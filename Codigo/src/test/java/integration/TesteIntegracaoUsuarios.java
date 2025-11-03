package integration;

import catalog.CoordinatorCatalog;
import catalog.SupervisorCatalog;
import catalog.StudentCatalog;
import controller.CoordinatorController;
import controller.SupervisorController;
import controller.StudentController;

public class TesteIntegracaoUsuarios {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO TESTE DE INTEGRAÇÃO DE USUÁRIOS ===\n");

        // Instancia os catálogos e controladores
        CoordinatorCatalog coordinatorCatalog = new CoordinatorCatalog();
        SupervisorCatalog supervisorCatalog = new SupervisorCatalog();
        StudentCatalog studentCatalog = new StudentCatalog();

        CoordinatorController coordinatorController = new CoordinatorController(coordinatorCatalog);
        SupervisorController supervisorController = new SupervisorController(supervisorCatalog);
        StudentController studentController = new StudentController(studentCatalog);

        // Registro
        System.out.println("Registrando usuários...");
        boolean c1 = coordinatorController.createCoordinator("Carlos", "carlos@unesp.br", "Senha123!", 1001);
        boolean s1 = supervisorController.createSupervisor("Sonia", "sonia@unesp.br", "Senha123!", "12345678900");
        boolean a1 = studentController.createStudent("Ana", "ana@unesp.br", "Senha123!", "98765432100", 2023001);

        System.out.println("Coordenador registrado: " + c1);
        System.out.println("Supervisor registrado: " + s1);
        System.out.println("Aluno registrado: " + a1);

        // Login
        System.out.println("\nAutenticando usuários...");
        boolean loginC = coordinatorController.loginCoordinator("carlos@unesp.br", "Senha123!");
        boolean loginS = supervisorController.loginSupervisor("sonia@unesp.br", "Senha123!");
        boolean loginA = studentController.loginStudent("ana@unesp.br", "Senha123!");

        System.out.println("Login coordenador: " + loginC);
        System.out.println("Login supervisor: " + loginS);
        System.out.println("Login aluno: " + loginA);

        System.out.println("\n=== FIM DOS TESTES ===");
    }
}
