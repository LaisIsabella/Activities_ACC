package integration;

import controller.ActivityTypeController;
import model.ActivityType;
import java.util.List;

/**
 * Testes de Integração para CE08 - Configurar Limite de Horas
 */
public class ConfigurarLimiteHorasTest {

    public static void main(String[] args) {
        System.out.println("=== Iniciando Testes CE08 - Configurar Limite de Horas ===\n");
        
        testarFluxoPrincipal();
        testarValorInvalido();
        testarLimiteMuitoAlto();
        
        System.out.println("\n=== Testes CE08 Concluídos ===");
    }
    
    /**
     * Testa o fluxo principal - configurar limite válido
     */
    private static void testarFluxoPrincipal() {
        System.out.println("Teste 1: Fluxo Principal - Configurar limite válido");
        
        ActivityTypeController controller = new ActivityTypeController();
        List<ActivityType> tipos = controller.listTypes();
        
        if (tipos.isEmpty()) {
            System.out.println("❌ FALHOU: Nenhum tipo de atividade disponível");
            System.out.println();
            return;
        }
        
        ActivityType tipo = tipos.get(0);
        int limiteOriginal = tipo.getLimit();
        int novoLimite = 50;
        
        try {
            controller.setHourLimit(tipo, String.valueOf(novoLimite));
            
            // Recarrega para verificar
            List<ActivityType> tiposAtualizados = controller.listTypes();
            ActivityType tipoAtualizado = tiposAtualizados.stream()
                .filter(t -> t.getName().equals(tipo.getName()))
                .findFirst()
                .orElse(null);
            
            if (tipoAtualizado != null && tipoAtualizado.getLimit() == novoLimite) {
                System.out.println("✅ PASSOU: Limite atualizado com sucesso");
            } else {
                System.out.println("❌ FALHOU: Limite não foi atualizado corretamente");
            }
        } catch (Exception e) {
            System.out.println("❌ FALHOU: Exceção ao configurar limite - " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * Testa A1 - Valor inválido
     */
    private static void testarValorInvalido() {
        System.out.println("Teste 2: A1 - Valor inválido");
        
        ActivityTypeController controller = new ActivityTypeController();
        List<ActivityType> tipos = controller.listTypes();
        
        if (tipos.isEmpty()) {
            System.out.println("❌ FALHOU: Nenhum tipo de atividade disponível");
            System.out.println();
            return;
        }
        
        ActivityType tipo = tipos.get(0);
        
        // Valor vazio
        try {
            controller.setHourLimit(tipo, "");
            System.out.println("❌ FALHOU: Aceitou valor vazio");
        } catch (Exception e) {
            System.out.println("✅ PASSOU: Rejeitou valor vazio");
        }
        
        // Valor não numérico
        try {
            controller.setHourLimit(tipo, "abc");
            System.out.println("❌ FALHOU: Aceitou valor não numérico");
        } catch (Exception e) {
            System.out.println("✅ PASSOU: Rejeitou valor não numérico");
        }
        
        // Valor negativo
        try {
            controller.setHourLimit(tipo, "-10");
            System.out.println("❌ FALHOU: Aceitou valor negativo");
        } catch (Exception e) {
            System.out.println("✅ PASSOU: Rejeitou valor negativo");
        }
        
        System.out.println();
    }
    
    /**
     * Testa limite muito alto (acima das diretrizes)
     */
    private static void testarLimiteMuitoAlto() {
        System.out.println("Teste 3: Limite acima das diretrizes (>200)");
        
        ActivityTypeController controller = new ActivityTypeController();
        List<ActivityType> tipos = controller.listTypes();
        
        if (tipos.isEmpty()) {
            System.out.println("❌ FALHOU: Nenhum tipo de atividade disponível");
            System.out.println();
            return;
        }
        
        ActivityType tipo = tipos.get(0);
        
        try {
            controller.setHourLimit(tipo, "250");
            System.out.println("❌ FALHOU: Aceitou limite acima de 200 horas");
        } catch (Exception e) {
            System.out.println("✅ PASSOU: Rejeitou limite acima das diretrizes - " + e.getMessage());
        }
        
        System.out.println();
    }
}