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
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1, 10, 10));

        JButton btnAdd = new JButton("Adicionar Atividade");
        JButton btnViewLimits = new JButton("Limites de Horas");
        JButton btnHistory = new JButton("Histórico de Atividades");
        JButton btnMessages = new JButton("Caixa de Mensagens");
        JButton btnGenerateReport = new JButton("Gerar Relatório");
        JButton btnExit = new JButton("Sair");

        btnAdd.addActionListener(e
                -> new AddActivityView(student, activityController, activityTypes));

        btnViewLimits.addActionListener(e
                -> new StudentHourLimitsView()
        );

        btnHistory.addActionListener(e
                -> new StudentHistoryView(student, activityController)
        );
        
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

        btnGenerateReport.addActionListener(e
                -> new GenerateReportView(student, activityController)
        );

        btnExit.addActionListener(e -> dispose());

        add(btnAdd);
        add(btnViewLimits);
        add(btnHistory);
        add(btnMessages);
        add(btnGenerateReport);
        add(btnExit);

        setVisible(true);
    }
}