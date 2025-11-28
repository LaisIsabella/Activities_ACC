package view;

import controller.ActivityController;
import model.Activity;
import model.Status;

import javax.swing.*;
import java.awt.*;

public class CoordinatorActivityDetailsView extends JFrame {

    public CoordinatorActivityDetailsView(Activity activity, ActivityController controller) {

        setTitle("Avaliação da Atividade");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextArea txtDetails = new JTextArea();
        txtDetails.setEditable(false);
        txtDetails.setText(
                "Aluno: " + activity.getStudent().getName() + "\n"
                + "Atividade: " + activity.getName() + "\n"
                + "Tipo: " + activity.getActivityType().getName() + "\n"
                + "Horas solicitadas: " + activity.getHours() + "\n"
                + "Status atual: " + activity.getStatus()
        );

        add(new JScrollPane(txtDetails), BorderLayout.CENTER);

        JPanel panelButtons = new JPanel(new GridLayout(3, 1, 5, 5));

        JButton btnApprove = new JButton("Aprovar");
        JButton btnDeny = new JButton("Negar");
        JButton btnPartial = new JButton("Negar Parcialmente");

        panelButtons.add(btnApprove);
        panelButtons.add(btnDeny);
        panelButtons.add(btnPartial);

        add(panelButtons, BorderLayout.SOUTH);

        // ============= CE07 – Aprovar =============
        btnApprove.addActionListener(e -> {

            String value = JOptionPane.showInputDialog(
                    this,
                    "Horas a serem aprovadas:",
                    "Aprovar Atividade",
                    JOptionPane.PLAIN_MESSAGE
            );

            // Usuário cancelou
            if (value == null) {
                return;
            }

            value = value.trim();

            // Campo vazio
            if (value.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Valor de horas inválido (campo vazio).",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            int hours;

            // Não numérico
            try {
                hours = Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Valor de horas inválido (não numérico).",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Negativo ou zero
            if (hours <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Valor de horas inválido (deve ser maior que zero).",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Verifica limite do tipo
            int limit = activity.getActivityType().getLimit();

            if (hours > limit) {

                int option = JOptionPane.showConfirmDialog(
                        this,
                        "Horas informadas (" + hours + ") estão acima do limite permitido (" + limit + ").\n"
                        + "Deseja aplicar automaticamente o limite máximo?",
                        "Ajustar para o limite",
                        JOptionPane.YES_NO_OPTION
                );

                if (option == JOptionPane.NO_OPTION) {
                    return; // usuário desistiu
                }

                // Ajusta automaticamente
                hours = limit;
            }

            // Chama aprovação
            try {
                controller.approveActivity(activity, hours);
                JOptionPane.showMessageDialog(this, "Atividade aprovada com " + hours + " horas!");
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ============= CE05 – Negar =============
        btnDeny.addActionListener(e -> {
            String response = JOptionPane.showInputDialog("Justificativa da negação:");
            if (response == null) {
                return;
            }

            try {
                controller.denyActivity(activity, response);
                JOptionPane.showMessageDialog(this, "Atividade negada!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ============= CE06 – Negar parcialmente =============
        btnPartial.addActionListener(e -> {
            String response = JOptionPane.showInputDialog("Descreva os ajustes necessários:");
            if (response == null) {
                return;
            }

            try {
                controller.partiallyDenyActivity(activity, response);
                JOptionPane.showMessageDialog(this, "Atividade negada parcialmente!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
