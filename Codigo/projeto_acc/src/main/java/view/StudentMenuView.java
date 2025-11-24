package view;

import catalog.ActivityCatalog;
import catalog.StudentCatalog;
import controller.ActivityController;
import model.Activity;
import model.ActivityType;
import model.Student;
import repository.ActivityTypeRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentMenuView extends JFrame {

    private final Student student;
    private final ActivityController activityController;
    private final List<ActivityType> activityTypes;

    public StudentMenuView(Student student, ActivityController activityController, List<ActivityType> activityTypes) {
        this.student = student;
        this.activityController = activityController;
        this.activityTypes = activityTypes;

        setTitle("Menu do Aluno - " + student.getName());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        JButton btnAdd = new JButton("Adicionar Atividade");
        JButton btnHistory = new JButton("Histórico de Atividades");
        JButton btnMessages = new JButton("Caixa de Mensagens");
        JButton btnExit = new JButton("Sair");

        btnAdd.addActionListener(e
                -> new AddActivityView(student, activityController, activityTypes));

        btnHistory.addActionListener(e -> {
            List<Activity> mine = activityController.getByStudent(student);
            new DeleteActivityView(student, activityController, mine);
        });

        btnMessages.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (String m : student.getMessages()) {
                sb.append("• ").append(m).append("\n\n");
            }
            if (sb.length() == 0) {
                sb.append("Sem mensagens.");
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Mensagens", JOptionPane.INFORMATION_MESSAGE);
        });

        btnExit.addActionListener(e -> dispose());

        add(btnAdd);
        add(btnHistory);
        add(btnMessages);
        add(btnExit);

        setVisible(true);
    }
}
