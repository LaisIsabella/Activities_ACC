package view;

import model.Activity;

import javax.swing.*;
import java.awt.*;

public class StudentActivityDetailsView extends JFrame {

    public StudentActivityDetailsView(Activity activity) {
        setTitle("Detalhes da Atividade");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setText(
            "Nome: " + activity.getName() + "\n" +
            "Descrição: " + activity.getDescription() + "\n" +
            "Tipo: " + activity.getActivityType().getName() + "\n" +
            "Data: " + activity.getDate() + "\n" +
            "Horas: " + activity.getHours() + "\n" +
            "Status: " + activity.getStatus() + "\n" +
            "Resposta: " + (activity.getResponse() == null || activity.getResponse().isBlank() ? "-" : activity.getResponse())
        );

        add(new JScrollPane(area), BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        add(btnFechar, BorderLayout.SOUTH);

        setVisible(true);
    }
}
