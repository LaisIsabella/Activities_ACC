package integration;

import catalog.ActivityCatalog;
import catalog.StudentCatalog;
import controller.ActivityController;
import controller.StudentController;
import model.Activity;
import model.ActivityType;
import model.Document;
import model.Status;
import model.Student;
import repository.ActivityTypeRepository;

import java.util.Date;
import java.util.Calendar;
import java.util.List;

/**
 * Testes de Integração para CE11 - Adicionar Atividade
 */
public class AdicionarAtividadeTest {

    public static void main(String[] args) {
        System.out.println("=== Iniciando Testes CE11 - Adicionar Atividade ===\n");
        
        testarFluxoPrincipal();
        testarDadosInvalidos();
        testarDataFutura();
        
        System.out.println("\n=== Testes CE11 Concluídos ===");
    }
    
    /**
     * Testa o fluxo principal com dados válidos
     */
    private static void testarFluxoPrincipal() {
        System.out.println("Teste 1: Fluxo Principal - Adicionar atividade com dados válidos");
        
        // Setup
        StudentCatalog studentCatalog = new StudentCatalog();
        StudentController studentController = new StudentController(studentCatalog);
        ActivityTypeRepository typeRepo = new ActivityTypeRepository();
        ActivityCatalog activityCatalog = new ActivityCatalog(studentCatalog, typeRepo);
        ActivityController activityController = new ActivityController(activityCatalog);
        
        // Cria um estudante
        studentController.createStudent("Teste Aluno", "teste@unesp.br", "Senha@123", "12345678901", "123456789");
        Student student = studentCatalog.findStudentByEmail("teste@unesp.br");
        
        // Cria um documento
        Document doc = new Document("comprovante.pdf", "/path/to/comprovante.pdf");
        
        // Pega um tipo de atividade
        List<ActivityType> tipos = typeRepo.loadAll();
        if (tipos.isEmpty()) {
            System.out.println("❌ FALHOU: Nenhum tipo de atividade disponível");
            System.out.println();
            return;
        }
        ActivityType tipo = tipos.get(0);
        
        // Data válida (hoje)
        Date data = new Date();
        
        // Cria a atividade
        boolean resultado = activityController.createActivity(
            "Palestra sobre Java",
            "Participação em palestra sobre programação Java",
            data,
            4,
            Status.PENDING,
            tipo,
            student,
            doc
        );
        
        if (resultado) {
            System.out.println("✅ PASSOU: Atividade criada com sucesso");
            
            // Verifica se foi cadastrada
            List<Activity> atividades = activityController.getByStudent(student);
            if (!atividades.isEmpty()) {
                System.out.println("✅ PASSOU: Atividade encontrada no histórico do aluno");
            } else {
                System.out.println("❌ FALHOU: Atividade não encontrada no histórico");
            }
        } else {
            System.out.println("❌ FALHOU: Não foi possível criar a atividade");
        }
        System.out.println();
    }
    
    /**
     * Testa A1 - Dados inválidos
     */
    private static void testarDadosInvalidos() {
        System.out.println("Teste 2: A1 - Dados inválidos");
        
        // Setup
        StudentCatalog studentCatalog = new StudentCatalog();
        StudentController studentController = new StudentController(studentCatalog);
        ActivityTypeRepository typeRepo = new ActivityTypeRepository();
        ActivityCatalog activityCatalog = new ActivityCatalog(studentCatalog, typeRepo);
        ActivityController activityController = new ActivityController(activityCatalog);
        
        // Cria um estudante
        studentController.createStudent("Teste Aluno 2", "teste2@unesp.br", "Senha@123", "98765432100", "987654321");
        Student student = studentCatalog.findStudentByEmail("teste2@unesp.br");
        
        Document doc = new Document("comprovante.pdf", "/path/to/comprovante.pdf");
        List<ActivityType> tipos = typeRepo.loadAll();
        ActivityType tipo = tipos.isEmpty() ? null : tipos.get(0);
        Date data = new Date();
        
        // Nome vazio
        boolean resultado1 = activityController.createActivity(
            "",
            "Descrição válida",
            data,
            4,
            Status.PENDING,
            tipo,
            student,
            doc
        );
        System.out.println(resultado1 ? "❌ FALHOU: Aceitou nome vazio" : "✅ PASSOU: Rejeitou nome vazio");
        
        // Horas zero ou negativas
        boolean resultado2 = activityController.createActivity(
            "Atividade Teste",
            "Descrição válida",
            data,
            0,
            Status.PENDING,
            tipo,
            student,
            doc
        );
        System.out.println(resultado2 ? "❌ FALHOU: Aceitou horas zero" : "✅ PASSOU: Rejeitou horas zero");
        
        // Estudante null
        boolean resultado3 = activityController.createActivity(
            "Atividade Teste",
            "Descrição válida",
            data,
            4,
            Status.PENDING,
            tipo,
            null,
            doc
        );
        System.out.println(resultado3 ? "❌ FALHOU: Aceitou estudante null" : "✅ PASSOU: Rejeitou estudante null");
        
        System.out.println();
    }
    
    /**
     * Testa data futura (não permitida)
     */
    private static void testarDataFutura() {
        System.out.println("Teste 3: Data futura (não permitida)");
        
        // Setup
        StudentCatalog studentCatalog = new StudentCatalog();
        StudentController studentController = new StudentController(studentCatalog);
        ActivityTypeRepository typeRepo = new ActivityTypeRepository();
        ActivityCatalog activityCatalog = new ActivityCatalog(studentCatalog, typeRepo);
        ActivityController activityController = new ActivityController(activityCatalog);
        
        // Cria um estudante
        studentController.createStudent("Teste Aluno 3", "teste3@unesp.br", "Senha@123", "11122233344", "111222333");
        Student student = studentCatalog.findStudentByEmail("teste3@unesp.br");
        
        Document doc = new Document("comprovante.pdf", "/path/to/comprovante.pdf");
        List<ActivityType> tipos = typeRepo.loadAll();
        ActivityType tipo = tipos.isEmpty() ? null : tipos.get(0);
        
        // Data futura
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 10);
        Date dataFutura = cal.getTime();
        
        boolean resultado = activityController.createActivity(
            "Atividade Futura",
            "Tentativa com data futura",
            dataFutura,
            4,
            Status.PENDING,
            tipo,
            student,
            doc
        );
        
        System.out.println(resultado ? "❌ FALHOU: Aceitou data futura" : "✅ PASSOU: Rejeitou data futura");
        System.out.println();
    }
}