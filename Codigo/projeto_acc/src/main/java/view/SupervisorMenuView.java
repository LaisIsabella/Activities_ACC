package view;

import controller.ActivityController;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import model.Supervisor;
import view.SupervisorValidateActivitiesView;


public class SupervisorMenuView extends JFrame {

    public SupervisorMenuView(ActivityController activityController, Supervisor supervisor) {
        setTitle("Menu Supervisor - " + supervisor.getName());
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10));

        JButton btnVerificar = new JButton("Validar Atividades");
        JButton btnSair = new JButton("Sair");

        btnVerificar.addActionListener(e -> new SupervisorValidateActivitiesView(activityController));
        btnSair.addActionListener(e -> dispose());
        add(btnVerificar);
        add(new JLabel());
        add(btnSair);

        setVisible(true);
    }
}
