package view;

import controller.ActivityController;
import model.Activity;


import javax.swing.*;
import java.awt.*;

public class SupervisorActivityDetailsView extends JFrame {

    public SupervisorActivityDetailsView(Activity activity,
                                         ActivityController controller,
                                         DefaultListModel<Activity> model) {
        setTitle("Avaliação da Atividade");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ================================
        // PAINEL DE DETALHES
        // ================================
        JTextArea txtDetails = new JTextArea();
        txtDetails.setEditable(false);
        txtDetails.setText(
            "Aluno: " + activity.getStudent().getName() + "\n" +
            "Atividade: " + activity.getName() + "\n" +
            "Tipo: " + activity.getActivityType().getName() + "\n" +
            "Horas solicitadas: " + activity.getHours() + "\n" +
            "Status atual: " + activity.getStatus()
        );
        add(new JScrollPane(txtDetails), BorderLayout.CENTER);

        // ================================
        // BOTÕES
        // ================================
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton btnValidar = new JButton("Validar e encaminhar");
        JButton btnNegar = new JButton("Negar");
        JButton btnVerArquivo = new JButton("Abrir Documento");

        buttonPanel.add(btnValidar);
        buttonPanel.add(btnNegar);
        buttonPanel.add(btnVerArquivo);

        add(buttonPanel, BorderLayout.SOUTH);

        // ================================
        // AÇÕES
        // ================================

        // Abrir documento
        btnVerArquivo.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(activity.getDocument().getFile());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir o documento.");
            }
        });

        // Validar e encaminhar
        btnValidar.addActionListener(e -> {
            boolean success = controller.verifyActivity(activity);
            if (success) {
                JOptionPane.showMessageDialog(this, "Atividade encaminhada ao coordenador.");
                model.removeElement(activity);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao registrar validação.");
            }
        });

        // Negar com justificativa
        btnNegar.addActionListener(e -> {
            String response = JOptionPane.showInputDialog(this, "Justificativa da negação:");
            if (response == null || response.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Justificativa obrigatória!");
                return;
            }

            boolean success = controller.denyActivitySupervisor(activity, response);
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Atividade negada.\n(Notificação simulada para o aluno: "
                    + activity.getStudent().getName() + ")");
                model.removeElement(activity);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao negar atividade.");
            }
        });

        setVisible(true);
    }
}
