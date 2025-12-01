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
import java.util.List;

/**
 * Testes de Integração para CE12 - Excluir Atividade
 */
public class ExcluirAtividadeTest {

    public static void main(String[] args) {
        System.out.println("=== Iniciando Testes CE12 - Excluir Atividade ===\n");
        
        testarFluxoPrincipal();
        testarExclusaoAtividadeNaoPendente();
        testarExclusaoAtividadeVerificada();
        
        System.out.println("\n=== Testes CE12 Concluídos ===");
    }
    
    /**
     * Testa o fluxo principal - excluir atividade elegível
     */
    private static void testarFluxoPrincipal() {
        System.out.println("Teste 1: Fluxo Principal - Excluir atividade PENDENTE e não verificada");
        
        // Setup
        StudentCatalog studentCatalog = new StudentCatalog();
        StudentController studentController = new StudentController(studentCatalog);
        ActivityTypeRepository typeRepo = new ActivityTypeRepository();
        ActivityCatalog activityCatalog = new ActivityCatalog(studentCatalog, typeRepo);
        ActivityController activityController = new ActivityController(activityCatalog);
        
        // Cria um estudante
        studentController.createStudent("Aluno Teste", "aluno.excluir@unesp.br", "Senha@123", "12345678901", "123456789");
        Student student = studentCatalog.findStudentByEmail("aluno.excluir@unesp.br");
        
        // Cria uma atividade pendente
        Document doc = new Document("comprovante.pdf", "/path/to/comprovante.pdf");
        List<ActivityType> tipos = typeRepo.loadAll();
        ActivityType tipo = tipos.isEmpty() ? null : tipos.get(0);
        
        activityController.createActivity(
            "Atividade Para Excluir",
            "Esta atividade será excluída",
            new Date(),
            4,
            Status.PENDING,
            tipo,
            student,
            doc
        );
        
        // Pega a atividade criada
        List<Activity> atividades = activityController.getByStudent(student);
        if (atividades.isEmpty()) {
            System.out.println("❌ FALHOU: Atividade não foi criada");
            System.out.println();
            return;
        }
        
        Activity atividade = atividades.get(0);
        
        // Tenta excluir
        boolean resultado = activityController.deleteActivity(atividade);
        
        if (resultado) {
            System.out.println("✅ PASSOU: Atividade excluída com sucesso");
            
            // Verifica se foi realmente excluída
            List<Activity> atividadesAposExclusao = activityController.getByStudent(student);
            if (atividadesAposExclusao.isEmpty() || !atividadesAposExclusao.contains(atividade)) {
                System.out.println("✅ PASSOU: Atividade não está mais no histórico");
            } else {
                System.out.println("❌ FALHOU: Atividade ainda está no histórico");
            }
        } else {
            System.out.println("❌ FALHOU: Não foi possível excluir a atividade");
        }
        System.out.println();
    }
    
    /**
     * Testa A2 - Atividade não elegível (não está pendente)
     */
    private static void testarExclusaoAtividadeNaoPendente() {
        System.out.println("Teste 2: A2 - Tentar excluir atividade APROVADA");
        
        // Setup
        StudentCatalog studentCatalog = new StudentCatalog();
        StudentController studentController = new StudentController(studentCatalog);
        ActivityTypeRepository typeRepo = new ActivityTypeRepository();
        ActivityCatalog activityCatalog = new ActivityCatalog(studentCatalog, typeRepo);
        ActivityController activityController = new ActivityController(activityCatalog);
        activityController.setStudentController(studentController);
        
        // Cria um estudante
        studentController.createStudent("Aluno Teste 2", "aluno.excluir2@unesp.br", "Senha@123", "98765432100", "987654321");
        Student student = studentCatalog.findStudentByEmail("aluno.excluir2@unesp.br");
        
        // Cria uma atividade
        Document doc = new Document("comprovante.pdf", "/path/to/comprovante.pdf");
        List<ActivityType> tipos = typeRepo.loadAll();
        ActivityType tipo = tipos.isEmpty() ? null : tipos.get(0);
        
        activityController.createActivity(
            "Atividade Aprovada",
            "Esta atividade está aprovada",
            new Date(),
            4,
            Status.PENDING,
            tipo,
            student,
            doc
        );
        
        // Pega a atividade e aprova
        List<Activity> atividades = activityController.getByStudent(student);
        if (atividades.isEmpty()) {
            System.out.println("❌ FALHOU: Atividade não foi criada");
            System.out.println();
            return;
        }
        
        Activity atividade = atividades.get(0);
        
        // Marca como verificada e aprova
        try {
            activityController.verifyActivity(atividade);
            activityController.approveActivity(atividade, 4);
        } catch (Exception e) {
            // Ignora erro de aprovação para o teste
        }
        
        // Tenta excluir (não deve permitir)
        try {
            boolean resultado = activityController.deleteActivity(atividade);
            System.out.println(resultado ? "❌ FALHOU: Permitiu excluir atividade aprovada" : "✅ PASSOU: Rejeitou exclusão de atividade aprovada");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ PASSOU: Rejeitou exclusão de atividade aprovada - " + e.getMessage());
        }
        
        System.out.println();
    }
    
    /**
     * Testa tentativa de excluir atividade pendente mas já verificada
     */
    private static void testarExclusaoAtividadeVerificada() {
        System.out.println("Teste 3: Tentar excluir atividade PENDENTE mas VERIFICADA");
        
        // Setup
        StudentCatalog studentCatalog = new StudentCatalog();
        StudentController studentController = new StudentController(studentCatalog);
        ActivityTypeRepository typeRepo = new ActivityTypeRepository();
        ActivityCatalog activityCatalog = new ActivityCatalog(studentCatalog, typeRepo);
        ActivityController activityController = new ActivityController(activityCatalog);
        
        // Cria um estudante
        studentController.createStudent("Aluno Teste 3", "aluno.excluir3@unesp.br", "Senha@123", "11122233344", "111222333");
        Student student = studentCatalog.findStudentByEmail("aluno.excluir3@unesp.br");
        
        // Cria uma atividade
        Document doc = new Document("comprovante.pdf", "/path/to/comprovante.pdf");
        List<ActivityType> tipos = typeRepo.loadAll();
        ActivityType tipo = tipos.isEmpty() ? null : tipos.get(0);
        
        activityController.createActivity(
            "Atividade Verificada",
            "Esta atividade foi verificada",
            new Date(),
            4,
            Status.PENDING,
            tipo,
            student,
            doc
        );
        
        // Pega a atividade e verifica
        List<Activity> atividades = activityController.getByStudent(student);
        if (atividades.isEmpty()) {
            System.out.println("❌ FALHOU: Atividade não foi criada");
            System.out.println();
            return;
        }
        
        Activity atividade = atividades.get(0);
        activityController.verifyActivity(atividade);
        
        // Tenta excluir (não deve permitir)
        try {
            boolean resultado = activityController.deleteActivity(atividade);
            System.out.println(resultado ? "❌ FALHOU: Permitiu excluir atividade verificada" : "✅ PASSOU: Rejeitou exclusão de atividade verificada");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ PASSOU: Rejeitou exclusão de atividade verificada - " + e.getMessage());
        }
        
        System.out.println();
    }
}