package view;

import controller.ActivityTypeController;

import javax.swing.*;
import java.awt.*;

public class CoordinatorMenuView extends JFrame {

    public CoordinatorMenuView() {
        setTitle("Menu do Coordenador");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));

        JButton btnConfigLimits = new JButton("Configurar Limites de Horas");
        JButton btnManageActivities = new JButton("Gerenciar Atividades");
        JButton btnLogout = new JButton("Sair");

        add(btnConfigLimits);
        add(btnManageActivities);
        add(btnLogout);

        // → Abre a tela CE08
        btnConfigLimits.addActionListener(e -> {
            ActivityTypeController controller = new ActivityTypeController();
            new ConfigurarLimiteHorasView(controller);
        });

        btnLogout.addActionListener(e -> dispose());

        setVisible(true);
    }
}
