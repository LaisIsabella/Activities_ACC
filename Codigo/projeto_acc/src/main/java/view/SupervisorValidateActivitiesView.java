package view;

import controller.ActivityController;
import model.Activity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import model.Status;

public class SupervisorValidateActivitiesView extends JFrame {

    public SupervisorValidateActivitiesView(ActivityController controller) {
        setTitle("Validação de Atividades do Supervisor");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        DefaultListModel<Activity> model = new DefaultListModel<>();
        JList<Activity> list = new JList<>(model);
        add(new JScrollPane(list), BorderLayout.CENTER);

        // Carrega apenas atividades ainda não verificadas
        List<Activity> activities = controller.searchFilteredActivities(null, Status.PENDING, null, false);
        for (Activity a : activities) {
            model.addElement(a);
        }

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    Activity selected = list.getSelectedValue();
                    if (selected != null) {
                        new SupervisorActivityDetailsView(selected, controller, model);
                    }
                }
            }
        });

        setVisible(true);
    }
}
