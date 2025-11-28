package view;

import controller.ActivityTypeController;
import model.ActivityType;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentHourLimitsView extends JFrame {

    public StudentHourLimitsView() {
        setTitle("Limites de Horas");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);

        try {
            ActivityTypeController controller = new ActivityTypeController();
            List<ActivityType> tipos = controller.listarTipos();

            if (tipos.isEmpty()) {
                area.setText("Nenhum limite definido até o momento.");
            } else {
                StringBuilder sb = new StringBuilder("Tipo de Atividade\t\tLimite de Horas\n");
                sb.append("===========================================\n");
                for (ActivityType tipo : tipos) {
                    sb.append(tipo.getName()).append("\t\t").append(tipo.getLimit()).append("h\n");
                }
                area.setText(sb.toString());
            }

        } catch (Exception e) {
            area.setText("Erro ao carregar os limites de horas.");
        }

        add(new JScrollPane(area), BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        add(btnFechar, BorderLayout.SOUTH);

        setVisible(true);
    }
}
