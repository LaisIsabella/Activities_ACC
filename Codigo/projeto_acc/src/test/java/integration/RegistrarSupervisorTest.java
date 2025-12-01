package integration;

import catalog.SupervisorCatalog;
import controller.SupervisorController;
import model.Supervisor;

/**
 * Testes de Integração para CE17 - Registrar Supervisor
 */
public class RegistrarSupervisorTest {

    public static void main(String[] args) {
        System.out.println("=== Iniciando Testes CE17 - Registrar Supervisor ===\n");
        
        testarFluxoPrincipal();
        testarDadosInvalidos();
        testarEmailDuplicado();
        testarCPFDuplicado();
        
        System.out.println("\n=== Testes CE17 Concluídos ===");
    }
    
    /**
     * Testa o fluxo principal com dados válidos
     */
    private static void testarFluxoPrincipal() {
        System.out.println("Teste 1: Fluxo Principal - Cadastro com dados válidos");
        
        SupervisorCatalog catalog = new SupervisorCatalog();
        SupervisorController controller = new SupervisorController(catalog);
        
        String nome = "Dr. João Supervisor";
        String email = "joao.supervisor@unesp.br";
        String senha = "Senha@123";
        String cpf = "12345678901";
        
        boolean resultado = controller.createSupervisor(nome, email, senha, cpf);
        
        if (resultado) {
            System.out.println("✅ PASSOU: Supervisor registrado com sucesso");
            
            // Verifica se foi cadastrado
            Supervisor supervisor = catalog.findSupervisorByEmail(email);
            if (supervisor != null && supervisor.getName().equals(nome)) {
                System.out.println("✅ PASSOU: Supervisor encontrado no catálogo");
            } else {
                System.out.println("❌ FALHOU: Supervisor não encontrado no catálogo");
            }
        } else {
            System.out.println("❌ FALHOU: Não foi possível registrar o supervisor");
        }
        System.out.println();
    }
    
    /**
     * Testa A1 - Dados inválidos
     */
    private static void testarDadosInvalidos() {
        System.out.println("Teste 2: A1 - Dados inválidos");
        
        SupervisorCatalog catalog = new SupervisorCatalog();
        SupervisorController controller = new SupervisorController(catalog);
        
        // Email não institucional
        boolean resultado1 = controller.createSupervisor("Dr. Maria", "maria@gmail.com", "Senha@123", "98765432100");
        System.out.println(resultado1 ? "❌ FALHOU: Aceitou email não institucional" : "✅ PASSOU: Rejeitou email não institucional");
        
        // Senha inválida (sem maiúscula)
        boolean resultado2 = controller.createSupervisor("Dr. Pedro", "pedro@unesp.br", "senha@123", "11122233344");
        System.out.println(resultado2 ? "❌ FALHOU: Aceitou senha inválida" : "✅ PASSOU: Rejeitou senha inválida");
        
        // CPF inválido
        boolean resultado3 = controller.createSupervisor("Dra. Ana", "ana@unesp.br", "Senha@123", "123");
        System.out.println(resultado3 ? "❌ FALHOU: Aceitou CPF inválido" : "✅ PASSOU: Rejeitou CPF inválido");
        
        System.out.println();
    }
    
    /**
     * Testa A2 - Email duplicado
     */
    private static void testarEmailDuplicado() {
        System.out.println("Teste 3: A2 - Email duplicado");
        
        SupervisorCatalog catalog = new SupervisorCatalog();
        SupervisorController controller = new SupervisorController(catalog);
        
        String email = "duplicado.sup@unesp.br";
        
        // Primeiro cadastro
        controller.createSupervisor("Primeiro Supervisor", email, "Senha@123", "11111111111");
        
        // Tentativa de duplicar
        boolean resultado = controller.createSupervisor("Segundo Supervisor", email, "Senha@456", "22222222222");
        
        System.out.println(resultado ? "❌ FALHOU: Permitiu email duplicado" : "✅ PASSOU: Rejeitou email duplicado");
        System.out.println();
    }
    
    /**
     * Testa A2 - CPF duplicado
     */
    private static void testarCPFDuplicado() {
        System.out.println("Teste 4: A2 - CPF duplicado");
        
        SupervisorCatalog catalog = new SupervisorCatalog();
        SupervisorController controller = new SupervisorController(catalog);
        
        String cpf = "33333333333";
        
        // Primeiro cadastro
        controller.createSupervisor("Primeiro Supervisor", "sup1@unesp.br", "Senha@123", cpf);
        
        // Tentativa de duplicar CPF
        boolean resultado = controller.createSupervisor("Segundo Supervisor", "sup2@unesp.br", "Senha@456", cpf);
        
        System.out.println(resultado ? "❌ FALHOU: Permitiu CPF duplicado" : "✅ PASSOU: Rejeitou CPF duplicado");
        System.out.println();
    }
}