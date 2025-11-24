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
                "Aluno: " + activity.getStudent().getName() + "\n" +
                "Atividade: " + activity.getName() + "\n" +
                "Tipo: " + activity.getActivityType().getName() + "\n" +
                "Horas solicitadas: " + activity.getHours() + "\n" +
                "Status atual: " + activity.getStatus()
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
            String value = JOptionPane.showInputDialog("Horas a serem aprovadas:");

            if (value == null) return;

            try {
                int hours = Integer.parseInt(value);

                controller.approveActivity(activity, hours);

                JOptionPane.showMessageDialog(this, "Atividade aprovada!");
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ============= CE05 – Negar =============
        btnDeny.addActionListener(e -> {
            String response = JOptionPane.showInputDialog("Justificativa da negação:");
            if (response == null) return;

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
            if (response == null) return;

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
