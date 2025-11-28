package view;

import controller.ActivityController;
import model.Activity;
import model.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class StudentHistoryView extends JFrame {

    public StudentHistoryView(Student student, ActivityController controller) {
        setTitle("Histórico de Atividades");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        DefaultListModel<Activity> model = new DefaultListModel<>();
        JList<Activity> list = new JList<>(model);
        add(new JScrollPane(list), BorderLayout.CENTER);

        // Busca as atividades do aluno
        try {
            List<Activity> atividades = controller.getByStudent(student);

            if (atividades.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhuma atividade cadastrada.");
            } else {
                for (Activity a : atividades) {
                    model.addElement(a);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar atividades.");
        }

        // Abrir detalhes com clique duplo
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    Activity selected = list.getSelectedValue();
                    if (selected != null) {
                        new StudentActivityDetailsView(selected);
                    }
                }
            }
        });

        setVisible(true);
    }
}
