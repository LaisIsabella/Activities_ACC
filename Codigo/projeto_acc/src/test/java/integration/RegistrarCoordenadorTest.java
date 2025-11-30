package integration;

import catalog.CoordinatorCatalog;
import catalog.AuthorizationCatalog;
import controller.CoordinatorController;
import model.Coordinator;

/**
 * Testes de Integração para CE02 - Registrar Coordenador
 */
public class RegistrarCoordenadorTest {

    public static void main(String[] args) {
        System.out.println("=== Iniciando Testes CE02 - Registrar Coordenador ===\n");
        
        testarFluxoPrincipal();
        testarDadosInvalidos();
        testarEmailDuplicado();
        testarRCDuplicado();
        testarRCNaoAutorizado();
        
        System.out.println("\n=== Testes CE02 Concluídos ===");
    }
    
    /**
     * Testa o fluxo principal com dados válidos
     */
    private static void testarFluxoPrincipal() {
        System.out.println("Teste 1: Fluxo Principal - Cadastro com dados válidos");
        
        CoordinatorCatalog catalog = new CoordinatorCatalog();
        CoordinatorController controller = new CoordinatorController(catalog);
        
        // Dados válidos
        String nome = "João Silva";
        String email = "joao.silva@unesp.br";
        String senha = "Senha@123";
        String rc = "1234"; // RC que deve estar autorizado
        
        boolean resultado = controller.createCoordinator(nome, email, senha, rc);
        
        if (resultado) {
            System.out.println("✅ PASSOU: Coordenador registrado com sucesso");
            
            // Verificar se foi realmente cadastrado
            Coordinator coord = catalog.findCoordinatorByEmail(email);
            if (coord != null && coord.getName().equals(nome)) {
                System.out.println("✅ PASSOU: Coordenador encontrado no catálogo");
            } else {
                System.out.println("❌ FALHOU: Coordenador não encontrado no catálogo");
            }
        } else {
            System.out.println("❌ FALHOU: Não foi possível registrar o coordenador");
        }
        System.out.println();
    }
    
    /**
     * Testa A1 - Dados inválidos
     */
    private static void testarDadosInvalidos() {
        System.out.println("Teste 2: A1 - Dados inválidos");
        
        CoordinatorCatalog catalog = new CoordinatorCatalog();
        CoordinatorController controller = new CoordinatorController(catalog);
        
        // Email inválido (não institucional)
        boolean resultado1 = controller.createCoordinator("Maria Santos", "maria@gmail.com", "Senha@123", "5678");
        System.out.println(resultado1 ? "❌ FALHOU: Aceitou email não institucional" : "✅ PASSOU: Rejeitou email não institucional");
        
        // Senha inválida (sem caractere especial)
        boolean resultado2 = controller.createCoordinator("Pedro Oliveira", "pedro@unesp.br", "Senha123", "9012");
        System.out.println(resultado2 ? "❌ FALHOU: Aceitou senha inválida" : "✅ PASSOU: Rejeitou senha inválida");
        
        // RC inválido (formato incorreto)
        boolean resultado3 = controller.createCoordinator("Ana Costa", "ana@unesp.br", "Senha@123", "ABCD");
        System.out.println(resultado3 ? "❌ FALHOU: Aceitou RC inválido" : "✅ PASSOU: Rejeitou RC inválido");
        
        System.out.println();
    }
    
    /**
     * Testa E1 - Email duplicado
     */
    private static void testarEmailDuplicado() {
        System.out.println("Teste 3: E1 - Email duplicado");
        
        CoordinatorCatalog catalog = new CoordinatorCatalog();
        CoordinatorController controller = new CoordinatorController(catalog);
        
        String email = "duplicado@unesp.br";
        
        // Primeiro cadastro
        controller.createCoordinator("Primeiro Coordenador", email, "Senha@123", "1111");
        
        // Tentativa de duplicar email
        boolean resultado = controller.createCoordinator("Segundo Coordenador", email, "Senha@456", "2222");
        
        System.out.println(resultado ? "❌ FALHOU: Permitiu email duplicado" : "✅ PASSOU: Rejeitou email duplicado");
        System.out.println();
    }
    
    /**
     * Testa E1 - RC duplicado
     */
    private static void testarRCDuplicado() {
        System.out.println("Teste 4: E1 - RC duplicado");
        
        CoordinatorCatalog catalog = new CoordinatorCatalog();
        CoordinatorController controller = new CoordinatorController(catalog);
        
        String rc = "9999";
        
        // Primeiro cadastro
        controller.createCoordinator("Primeiro Coordenador", "coord1@unesp.br", "Senha@123", rc);
        
        // Tentativa de duplicar RC
        boolean resultado = controller.createCoordinator("Segundo Coordenador", "coord2@unesp.br", "Senha@456", rc);
        
        System.out.println(resultado ? "❌ FALHOU: Permitiu RC duplicado" : "✅ PASSOU: Rejeitou RC duplicado");
        System.out.println();
    }
    
    /**
     * Testa verificação de RC não autorizado
     */
    private static void testarRCNaoAutorizado() {
        System.out.println("Teste 5: RC não autorizado");
        
        CoordinatorCatalog catalog = new CoordinatorCatalog();
        CoordinatorController controller = new CoordinatorController(catalog);
        
        // RC que provavelmente não está autorizado
        String rcNaoAutorizado = "0001";
        
        boolean resultado = controller.createCoordinator("Coordenador Não Autorizado", "naoaut@unesp.br", "Senha@123", rcNaoAutorizado);
        
        System.out.println(resultado ? "❌ FALHOU: Permitiu RC não autorizado" : "✅ PASSOU: Rejeitou RC não autorizado");
        System.out.println();
    }
}