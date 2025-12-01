package integration;

import catalog.StudentCatalog;
import controller.StudentController;

/**
 * Testes de Integração para CE10 - Autenticar Aluno
 */
public class AutenticarAlunoTest {

    public static void main(String[] args) {
        System.out.println("=== Iniciando Testes CE10 - Autenticar Aluno ===\n");
        
        testarFluxoPrincipal();
        testarCredenciaisInvalidas();
        testarAlunoNaoCadastrado();
        
        System.out.println("\n=== Testes CE10 Concluídos ===");
    }
    
    /**
     * Testa o fluxo principal com credenciais válidas
     */
    private static void testarFluxoPrincipal() {
        System.out.println("Teste 1: Fluxo Principal - Autenticação com credenciais válidas");
        
        StudentCatalog catalog = new StudentCatalog();
        StudentController controller = new StudentController(catalog);
        
        // Registra um aluno
        String email = "aluno.teste@unesp.br";
        String senha = "Senha@123";
        controller.createStudent("Aluno Teste", email, senha, "12345678901", "123456789");
        
        // Tenta autenticar
        boolean resultado = controller.authenticateStudent(email, senha);
        
        System.out.println(resultado ? "✅ PASSOU: Autenticação bem-sucedida" : "❌ FALHOU: Autenticação falhou");
        System.out.println();
    }
    
    /**
     * Testa A1 - Credenciais inválidas
     */
    private static void testarCredenciaisInvalidas() {
        System.out.println("Teste 2: A1 - Credenciais inválidas");
        
        StudentCatalog catalog = new StudentCatalog();
        StudentController controller = new StudentController(catalog);
        
        // Registra um aluno
        String email = "aluno.valido@unesp.br";
        String senhaCorreta = "Senha@123";
        controller.createStudent("Aluno Válido", email, senhaCorreta, "98765432100", "987654321");
        
        // Tenta autenticar com senha incorreta
        boolean resultado1 = controller.authenticateStudent(email, "SenhaErrada@123");
        System.out.println(resultado1 ? "❌ FALHOU: Autenticou com senha incorreta" : "✅ PASSOU: Rejeitou senha incorreta");
        
        // Tenta autenticar com email incorreto
        boolean resultado2 = controller.authenticateStudent("email.errado@unesp.br", senhaCorreta);
        System.out.println(resultado2 ? "❌ FALHOU: Autenticou com email incorreto" : "✅ PASSOU: Rejeitou email incorreto");
        
        System.out.println();
    }
    
    /**
     * Testa A2 - Aluno não registrado
     */
    private static void testarAlunoNaoCadastrado() {
        System.out.println("Teste 3: A2 - Aluno não registrado");
        
        StudentCatalog catalog = new StudentCatalog();
        StudentController controller = new StudentController(catalog);
        
        // Tenta autenticar sem ter cadastrado
        boolean resultado = controller.authenticateStudent("naocadastrado@unesp.br", "Senha@123");
        
        System.out.println(resultado ? "❌ FALHOU: Autenticou aluno não cadastrado" : "✅ PASSOU: Rejeitou aluno não cadastrado");
        System.out.println();
    }
}