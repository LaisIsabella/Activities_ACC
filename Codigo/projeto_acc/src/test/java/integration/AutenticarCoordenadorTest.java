package integration;

import catalog.CoordinatorCatalog;
import controller.CoordinatorController;

/**
 * Testes de Integração para CE03 - Autenticar Coordenador
 */
public class AutenticarCoordenadorTest {

    public static void main(String[] args) {
        System.out.println("=== Iniciando Testes CE03 - Autenticar Coordenador ===\n");
        
        testarFluxoPrincipal();
        testarCredenciaisInvalidas();
        testarCoordenadorNaoCadastrado();
        
        System.out.println("\n=== Testes CE03 Concluídos ===");
    }
    
    /**
     * Testa o fluxo principal com credenciais válidas
     */
    private static void testarFluxoPrincipal() {
        System.out.println("Teste 1: Fluxo Principal - Autenticação com credenciais válidas");
        
        CoordinatorCatalog catalog = new CoordinatorCatalog();
        CoordinatorController controller = new CoordinatorController(catalog);
        
        // Primeiro registra um coordenador
        String email = "coord.teste@unesp.br";
        String senha = "Senha@123";
        controller.createCoordinator("Coordenador Teste", email, senha, "1234");
        
        // Tenta autenticar
        boolean resultado = controller.authenticateCoordinator(email, senha);
        
        System.out.println(resultado ? "✅ PASSOU: Autenticação bem-sucedida" : "❌ FALHOU: Autenticação falhou");
        System.out.println();
    }
    
    /**
     * Testa A1 - Credenciais inválidas
     */
    private static void testarCredenciaisInvalidas() {
        System.out.println("Teste 2: A1 - Credenciais inválidas");
        
        CoordinatorCatalog catalog = new CoordinatorCatalog();
        CoordinatorController controller = new CoordinatorController(catalog);
        
        // Registra um coordenador
        String email = "coord.valido@unesp.br";
        String senhaCorreta = "Senha@123";
        controller.createCoordinator("Coordenador Válido", email, senhaCorreta, "5678");
        
        // Tenta autenticar com senha incorreta
        boolean resultado1 = controller.authenticateCoordinator(email, "SenhaErrada@123");
        System.out.println(resultado1 ? "❌ FALHOU: Autenticou com senha incorreta" : "✅ PASSOU: Rejeitou senha incorreta");
        
        // Tenta autenticar com email incorreto
        boolean resultado2 = controller.authenticateCoordinator("email.errado@unesp.br", senhaCorreta);
        System.out.println(resultado2 ? "❌ FALHOU: Autenticou com email incorreto" : "✅ PASSOU: Rejeitou email incorreto");
        
        System.out.println();
    }
    
    /**
     * Testa A2 - Coordenador não cadastrado
     */
    private static void testarCoordenadorNaoCadastrado() {
        System.out.println("Teste 3: A2 - Coordenador não cadastrado");
        
        CoordinatorCatalog catalog = new CoordinatorCatalog();
        CoordinatorController controller = new CoordinatorController(catalog);
        
        // Tenta autenticar sem ter cadastrado
        boolean resultado = controller.authenticateCoordinator("naocadastrado@unesp.br", "Senha@123");
        
        System.out.println(resultado ? "❌ FALHOU: Autenticou coordenador não cadastrado" : "✅ PASSOU: Rejeitou coordenador não cadastrado");
        System.out.println();
    }
}