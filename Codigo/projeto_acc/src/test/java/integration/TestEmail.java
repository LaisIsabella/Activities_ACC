package integration;

import util.EmailUtil;

public class TestEmail {
    
    public static void main(String[] args) {
        System.out.println("========== TESTE DE ENVIO DE EMAIL ==========\n");
        
        // Teste 1: Email simples
        System.out.println("Teste 1: Enviando email simples...");
        boolean resultado1 = EmailUtil.sendEmail(
            "p.santos-junior@unesp.br",  // Destinatário
            "Teste do Sistema ACC",     // Assunto
            "Este é um email de teste do Sistema de Atividades Complementares.\n\nSe você recebeu isso, o sistema está funcionando!"
        );
        
        if (resultado1) {
            System.out.println("✅ Email 1 enviado com sucesso!\n");
        } else {
            System.out.println("❌ Falha ao enviar email 1\n");
        }
        
        // Teste 2: Email de atividade aprovada
        System.out.println("Teste 2: Enviando email de atividade aprovada...");
        boolean resultado2 = EmailUtil.sendActivityApprovedEmail(
            "p.santos-junior@unesp.br",
            "Laís Isabella",
            "Curso de Python Avançado",
            20
        );
        
        if (resultado2) {
            System.out.println("✅ Email 2 enviado com sucesso!\n");
        } else {
            System.out.println("❌ Falha ao enviar email 2\n");
        }
        
        // Teste 3: Email de atividade negada
        System.out.println("Teste 3: Enviando email de atividade negada...");
        boolean resultado3 = EmailUtil.sendActivityDeniedEmail(
            "lais.isabella@unesp.br",
            "Laís Isabella",
            "Monitoria de Cálculo",
            "Documento apresentado não comprova a realização da atividade."
        );
        
        if (resultado3) {
            System.out.println("✅ Email 3 enviado com sucesso!\n");
        } else {
            System.out.println("❌ Falha ao enviar email 3\n");
        }
        
        // Resumo
        System.out.println("========== RESUMO ==========");
        System.out.println("Email simples: " + (resultado1 ? "✅" : "❌"));
        System.out.println("Email aprovação: " + (resultado2 ? "✅" : "❌"));
        System.out.println("Email negação: " + (resultado3 ? "✅" : "❌"));
        System.out.println("===========================\n");
        
        if (resultado1 && resultado2 && resultado3) {
            System.out.println("🎉 TODOS OS TESTES PASSARAM! Sistema de email funcionando perfeitamente.");
        } else {
            System.out.println("⚠️  Alguns testes falharam. Verifique as configurações no email.properties");
        }
    }
}