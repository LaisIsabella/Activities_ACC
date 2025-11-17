package view;

import controller.ActivityController;
import model.Activity;

import javax.swing.*;
import java.awt.*;
import model.Student;

public class DeleteActivityView extends JFrame {

    public DeleteActivityView(Student student, ActivityController controller, java.util.List<Activity> activities) {

        setTitle("Histórico de Atividades");
        setSize(600, 300);
        setLocationRelativeTo(null);

        DefaultListModel<Activity> model = new DefaultListModel<>();
        activities.forEach(model::addElement);

        JList<Activity> list = new JList<>(model);
        JButton btnDelete = new JButton("Excluir");

        btnDelete.addActionListener(e -> {

            Activity selected = list.getSelectedValue();
            if (selected == null) return;

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Excluir atividade?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.deleteActivity(selected);
                    model.removeElement(selected);
                    JOptionPane.showMessageDialog(this, "Atividade excluída!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(new JScrollPane(list), BorderLayout.CENTER);
        add(btnDelete, BorderLayout.SOUTH);

        setVisible(true);
    }
}
