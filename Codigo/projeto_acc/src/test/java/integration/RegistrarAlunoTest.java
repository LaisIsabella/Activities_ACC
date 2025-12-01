package integration;

import catalog.StudentCatalog;
import controller.StudentController;
import model.Student;

/**
 * Testes de Integração para CE09 - Registrar Aluno
 */
public class RegistrarAlunoTest {

    public static void main(String[] args) {
        System.out.println("=== Iniciando Testes CE09 - Registrar Aluno ===\n");
        
        testarFluxoPrincipal();
        testarDadosInvalidos();
        testarEmailDuplicado();
        testarCPFDuplicado();
        testarRADuplicado();
        
        System.out.println("\n=== Testes CE09 Concluídos ===");
    }
    
    /**
     * Testa o fluxo principal com dados válidos
     */
    private static void testarFluxoPrincipal() {
        System.out.println("Teste 1: Fluxo Principal - Cadastro com dados válidos");
        
        StudentCatalog catalog = new StudentCatalog();
        StudentController controller = new StudentController(catalog);
        
        String nome = "Maria Silva";
        String email = "maria.silva@unesp.br";
        String senha = "Senha@123";
        String cpf = "12345678901";
        String ra = "123456789";
        
        boolean resultado = controller.createStudent(nome, email, senha, cpf, ra);
        
        if (resultado) {
            System.out.println("✅ PASSOU: Aluno registrado com sucesso");
            
            // Verifica se foi cadastrado
            Student student = catalog.findStudentByEmail(email);
            if (student != null && student.getName().equals(nome)) {
                System.out.println("✅ PASSOU: Aluno encontrado no catálogo");
            } else {
                System.out.println("❌ FALHOU: Aluno não encontrado no catálogo");
            }
        } else {
            System.out.println("❌ FALHOU: Não foi possível registrar o aluno");
        }
        System.out.println();
    }
    
    /**
     * Testa A1 - Dados inválidos
     */
    private static void testarDadosInvalidos() {
        System.out.println("Teste 2: A1 - Dados inválidos");
        
        StudentCatalog catalog = new StudentCatalog();
        StudentController controller = new StudentController(catalog);
        
        // Email não institucional
        boolean resultado1 = controller.createStudent("João Santos", "joao@gmail.com", "Senha@123", "98765432100", "987654321");
        System.out.println(resultado1 ? "❌ FALHOU: Aceitou email não institucional" : "✅ PASSOU: Rejeitou email não institucional");
        
        // Senha inválida (sem número)
        boolean resultado2 = controller.createStudent("Pedro Oliveira", "pedro@unesp.br", "Senha@Abc", "11122233344", "111222333");
        System.out.println(resultado2 ? "❌ FALHOU: Aceitou senha inválida" : "✅ PASSOU: Rejeitou senha inválida");
        
        // CPF inválido
        boolean resultado3 = controller.createStudent("Ana Costa", "ana@unesp.br", "Senha@123", "12345", "555666777");
        System.out.println(resultado3 ? "❌ FALHOU: Aceitou CPF inválido" : "✅ PASSOU: Rejeitou CPF inválido");
        
        // RA inválido (não tem 9 dígitos)
        boolean resultado4 = controller.createStudent("Carlos Lima", "carlos@unesp.br", "Senha@123", "44455566677", "12345");
        System.out.println(resultado4 ? "❌ FALHOU: Aceitou RA inválido" : "✅ PASSOU: Rejeitou RA inválido");
        
        System.out.println();
    }
    
    /**
     * Testa A2 - Email duplicado
     */
    private static void testarEmailDuplicado() {
        System.out.println("Teste 3: A2 - Email duplicado");
        
        StudentCatalog catalog = new StudentCatalog();
        StudentController controller = new StudentController(catalog);
        
        String email = "duplicado@unesp.br";
        
        // Primeiro cadastro
        controller.createStudent("Primeiro Aluno", email, "Senha@123", "11111111111", "111111111");
        
        // Tentativa de duplicar
        boolean resultado = controller.createStudent("Segundo Aluno", email, "Senha@456", "22222222222", "222222222");
        
        System.out.println(resultado ? "❌ FALHOU: Permitiu email duplicado" : "✅ PASSOU: Rejeitou email duplicado");
        System.out.println();
    }
    
    /**
     * Testa A2 - CPF duplicado
     */
    private static void testarCPFDuplicado() {
        System.out.println("Teste 4: A2 - CPF duplicado");
        
        StudentCatalog catalog = new StudentCatalog();
        StudentController controller = new StudentController(catalog);
        
        String cpf = "33333333333";
        
        // Primeiro cadastro
        controller.createStudent("Primeiro Aluno", "aluno1@unesp.br", "Senha@123", cpf, "333333333");
        
        // Tentativa de duplicar CPF
        boolean resultado = controller.createStudent("Segundo Aluno", "aluno2@unesp.br", "Senha@456", cpf, "444444444");
        
        System.out.println(resultado ? "❌ FALHOU: Permitiu CPF duplicado" : "✅ PASSOU: Rejeitou CPF duplicado");
        System.out.println();
    }
    
    /**
     * Testa A2 - RA duplicado
     */
    private static void testarRADuplicado() {
        System.out.println("Teste 5: A2 - RA duplicado");
        
        StudentCatalog catalog = new StudentCatalog();
        StudentController controller = new StudentController(catalog);
        
        String ra = "555555555";
        
        // Primeiro cadastro
        controller.createStudent("Primeiro Aluno", "aluno3@unesp.br", "Senha@123", "55555555555", ra);
        
        // Tentativa de duplicar RA
        boolean resultado = controller.createStudent("Segundo Aluno", "aluno4@unesp.br", "Senha@456", "66666666666", ra);
        
        System.out.println(resultado ? "❌ FALHOU: Permitiu RA duplicado" : "✅ PASSOU: Rejeitou RA duplicado");
        System.out.println();
    }
}