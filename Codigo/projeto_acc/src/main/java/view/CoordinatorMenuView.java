package view;

import catalog.ActivityCatalog;
import catalog.StudentCatalog;
import controller.ActivityController;
import controller.ActivityTypeController;

import javax.swing.*;
import java.awt.*;
import repository.ActivityTypeRepository;


public class CoordinatorMenuView extends JFrame {

    public CoordinatorMenuView() {

        StudentCatalog studentCatalog = new StudentCatalog();
        ActivityTypeRepository typeRepo = new ActivityTypeRepository();
        ActivityCatalog aCatalog = new ActivityCatalog(studentCatalog, typeRepo);
        ActivityController controller = new ActivityController(aCatalog);

        setTitle("Menu do Coordenador");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));

        JButton btnConfigLimits = new JButton("Configurar Limites de Horas");
        JButton btnManageActivities = new JButton("Buscar Atividades");
        JButton btnLogout = new JButton("Sair");

        add(btnConfigLimits);
        add(btnManageActivities);
        add(btnLogout);

        btnConfigLimits.addActionListener(e ->
                new ConfigurarLimiteHorasView(new ActivityTypeController()));

        btnManageActivities.addActionListener(e -> 
                new CoordinatorSearchActivityView(controller, studentCatalog)
        );

        btnLogout.addActionListener(e -> dispose());

        setVisible(true);
    }
}

