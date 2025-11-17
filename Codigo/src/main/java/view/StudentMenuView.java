package view;

import catalog.ActivityCatalog;
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

    public StudentMenuView(Student student) {

        this.student = student;

        ActivityCatalog activityCatalog = new ActivityCatalog(); // carrega do TXT
        this.activityController = new ActivityController(activityCatalog);

        ActivityTypeRepository typeRepo = new ActivityTypeRepository();
        this.activityTypes = typeRepo.loadAll();

        setTitle("Menu do Aluno - " + student.getName());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10));

        JButton btnAdd = new JButton("Adicionar Atividade");
        JButton btnHistory = new JButton("Histórico de Atividades");
        JButton btnExit = new JButton("Sair");

        btnAdd.addActionListener(e ->
                new AddActivityView(student, activityController, activityTypes));

        btnHistory.addActionListener(e -> {
            List<Activity> mine = activityController.getByStudent(student);
            new DeleteActivityView(student, activityController, mine);
        });

        btnExit.addActionListener(e -> dispose());

        add(btnAdd);
        add(btnHistory);
        add(btnExit);

        setVisible(true);
    }
}

