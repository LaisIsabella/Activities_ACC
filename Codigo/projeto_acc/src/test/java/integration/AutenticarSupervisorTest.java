package integration;

import catalog.SupervisorCatalog;
import controller.SupervisorController;

/**
 * Testes de Integração para CE18 - Autenticar Supervisor
 */
public class AutenticarSupervisorTest {

    public static void main(String[] args) {
        System.out.println("=== Iniciando Testes CE18 - Autenticar Supervisor ===\n");
        
        testarFluxoPrincipal();
        testarCredenciaisInvalidas();
        testarSupervisorNaoCadastrado();
        
        System.out.println("\n=== Testes CE18 Concluídos ===");
    }
    
    /**
     * Testa o fluxo principal com credenciais válidas
     */
    private static void testarFluxoPrincipal() {
        System.out.println("Teste 1: Fluxo Principal - Autenticação com credenciais válidas");
        
        SupervisorCatalog catalog = new SupervisorCatalog();
        SupervisorController controller = new SupervisorController(catalog);
        
        // Registra um supervisor
        String email = "supervisor.teste@unesp.br";
        String senha = "Senha@123";
        controller.createSupervisor("Supervisor Teste", email, senha, "12345678901");
        
        // Tenta autenticar
        boolean resultado = controller.authenticateSupervisor(email, senha);
        
        System.out.println(resultado ? "✅ PASSOU: Autenticação bem-sucedida" : "❌ FALHOU: Autenticação falhou");
        System.out.println();
    }
    
    /**
     * Testa A1 - Credenciais inválidas
     */
    private static void testarCredenciaisInvalidas() {
        System.out.println("Teste 2: A1 - Credenciais inválidas");
        
        SupervisorCatalog catalog = new SupervisorCatalog();
        SupervisorController controller = new SupervisorController(catalog);
        
        // Registra um supervisor
        String email = "supervisor.valido@unesp.br";
        String senhaCorreta = "Senha@123";
        controller.createSupervisor("Supervisor Válido", email, senhaCorreta, "98765432100");
        
        // Tenta autenticar com senha incorreta
        boolean resultado1 = controller.authenticateSupervisor(email, "SenhaErrada@123");
        System.out.println(resultado1 ? "❌ FALHOU: Autenticou com senha incorreta" : "✅ PASSOU: Rejeitou senha incorreta");
        
        // Tenta autenticar com email incorreto
        boolean resultado2 = controller.authenticateSupervisor("email.errado@unesp.br", senhaCorreta);
        System.out.println(resultado2 ? "❌ FALHOU: Autenticou com email incorreto" : "✅ PASSOU: Rejeitou email incorreto");
        
        System.out.println();
    }
    
    /**
     * Testa A2 - Supervisor não registrado
     */
    private static void testarSupervisorNaoCadastrado() {
        System.out.println("Teste 3: A2 - Supervisor não registrado");
        
        SupervisorCatalog catalog = new SupervisorCatalog();
        SupervisorController controller = new SupervisorController(catalog);
        
        // Tenta autenticar sem ter cadastrado
        boolean resultado = controller.authenticateSupervisor("naocadastrado@unesp.br", "Senha@123");
        
        System.out.println(resultado ? "❌ FALHOU: Autenticou supervisor não cadastrado" : "✅ PASSOU: Rejeitou supervisor não cadastrado");
        System.out.println();
    }
}