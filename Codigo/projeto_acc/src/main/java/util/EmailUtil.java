package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtil {

    private static String SMTP_HOST;
    private static String SMTP_PORT;
    private static String EMAIL_FROM;
    private static String EMAIL_PASSWORD;
    private static boolean EMAIL_ENABLED = false;

    // Carrega configurações ao iniciar
    static {
        loadEmailConfig();
    }

    private static void loadEmailConfig() {
        Properties config = new Properties();
        try (FileInputStream input = new FileInputStream("config/email.properties")) {
            config.load(input);
            
            SMTP_HOST = config.getProperty("smtp.host", "smtp.gmail.com");
            SMTP_PORT = config.getProperty("smtp.port", "587");
            EMAIL_FROM = config.getProperty("email.from");
            EMAIL_PASSWORD = config.getProperty("email.password");
            EMAIL_ENABLED = config.getProperty("email.enabled", "false").equals("true");
            
            if (EMAIL_ENABLED && (EMAIL_FROM == null || EMAIL_PASSWORD == null)) {
                System.err.println("AVISO: Email habilitado mas credenciais não configuradas!");
                EMAIL_ENABLED = false;
            }
            
        } catch (IOException e) {
            System.err.println("Arquivo email.properties não encontrado. Emails desabilitados.");
            EMAIL_ENABLED = false;
        }
    }

    /**
     * Envia email para o destinatário
     * @param receiverEmail Email do destinatário (@unesp.br)
     * @param subject Assunto do email
     * @param body Corpo da mensagem
     * @return true se enviado com sucesso, false caso contrário
     */
    public static boolean sendEmail(String receiverEmail, String subject, String body) {
        
        // Validação de parâmetros obrigatórios
        if (receiverEmail == null || receiverEmail.isBlank()) {
            System.err.println("ERRO: Email do destinatário ausente ou inválido");
            return false;
        }
        
        if (subject == null || subject.isBlank()) {
            System.err.println("ERRO: Assunto do email ausente");
            return false;
        }
        
        if (body == null || body.isBlank()) {
            System.err.println("ERRO: Corpo do email ausente");
            return false;
        }
        
        // Valida formato do email
        if (!receiverEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            System.err.println("ERRO: Formato de email inválido: " + receiverEmail);
            return false;
        }

        // Se email não está habilitado, apenas simula
        if (!EMAIL_ENABLED) {
            System.out.println("\n========== EMAIL (SIMULADO) ==========");
            System.out.println("Para: " + receiverEmail);
            System.out.println("Assunto: " + subject);
            System.out.println("Mensagem:\n" + body);
            System.out.println("=====================================\n");
            return true;
        }

        // Configurações do servidor SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.trust", SMTP_HOST);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // Cria sessão com autenticação
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
            }
        });

        try {
            // Cria a mensagem
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));
            message.setSubject(subject);
            message.setText(body);

            // Envia a mensagem
            Transport.send(message);

            System.out.println("✅ Email enviado com sucesso para: " + receiverEmail);
            return true;

        } catch (MessagingException e) {
            System.err.println("❌ ERRO ao enviar email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Envia email de notificação de atividade aprovada
     */
    public static boolean sendActivityApprovedEmail(String studentEmail, String studentName, 
                                                     String activityName, int approvedHours) {
        String subject = "Atividade Aprovada - Sistema ACC";
        String body = "Olá " + studentName + ",\n\n" +
                     "Sua atividade complementar foi APROVADA!\n\n" +
                     "Atividade: " + activityName + "\n" +
                     "Horas aprovadas: " + approvedHours + "h\n\n" +
                     "Parabéns! As horas já foram contabilizadas no seu histórico.\n\n" +
                     "Atenciosamente,\n" +
                     "Sistema de Atividades Complementares - UNESP";
        
        return sendEmail(studentEmail, subject, body);
    }

    /**
     * Envia email de notificação de atividade negada (pelo Coordenador)
     */
    public static boolean sendActivityDeniedEmail(String studentEmail, String studentName, 
                                                   String activityName, String reason) {
        String subject = "Atividade Negada - Sistema ACC";
        String body = "Olá " + studentName + ",\n\n" +
                     "Infelizmente sua atividade complementar foi NEGADA pelo Coordenador.\n\n" +
                     "Atividade: " + activityName + "\n" +
                     "Motivo: " + reason + "\n\n" +
                     "Você pode submeter uma nova atividade corrigindo os problemas apontados.\n\n" +
                     "Atenciosamente,\n" +
                     "Sistema de Atividades Complementares - UNESP";
        
        return sendEmail(studentEmail, subject, body);
    }

    /**
     * ✅ NOVO: Envia email de notificação de atividade negada pelo SUPERVISOR
     */
    public static boolean sendActivityDeniedBySupervisorEmail(String studentEmail, String studentName, 
                                                               String activityName, String reason) {
        String subject = "Atividade Negada na Validação - Sistema ACC";
        String body = "Olá " + studentName + ",\n\n" +
                     "Sua atividade complementar foi NEGADA pelo Supervisor durante a validação.\n\n" +
                     "Atividade: " + activityName + "\n" +
                     "Motivo da negação: " + reason + "\n\n" +
                     "O supervisor identificou problemas que impedem o encaminhamento desta atividade " +
                     "para aprovação do coordenador.\n\n" +
                     "Você pode submeter uma nova atividade corrigindo os problemas apontados.\n\n" +
                     "Observação: Esta atividade não chegou à etapa de avaliação do coordenador.\n\n" +
                     "Atenciosamente,\n" +
                     "Sistema de Atividades Complementares - UNESP";
        
        return sendEmail(studentEmail, subject, body);
    }

    /**
     * Envia email de notificação de atividade negada parcialmente
     */
    public static boolean sendActivityPartiallyDeniedEmail(String studentEmail, String studentName, 
                                                           String activityName, String adjustments) {
        String subject = "Atividade Necessita Ajustes - Sistema ACC";
        String body = "Olá " + studentName + ",\n\n" +
                     "Sua atividade complementar necessita de ajustes.\n\n" +
                     "Atividade: " + activityName + "\n" +
                     "Ajustes necessários: " + adjustments + "\n\n" +
                     "Por favor, revise a atividade e faça os ajustes solicitados.\n\n" +
                     "Atenciosamente,\n" +
                     "Sistema de Atividades Complementares - UNESP";
        
        return sendEmail(studentEmail, subject, body);
    }
}