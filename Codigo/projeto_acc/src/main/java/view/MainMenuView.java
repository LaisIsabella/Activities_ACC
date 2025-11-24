package view;

import catalog.ActivityCatalog;
import javax.swing.*;
import java.awt.*;
import controller.*;
import repository.ActivityTypeRepository;

public class MainMenuView extends JFrame {

    private CoordinatorController coordinatorController;
    private SupervisorController supervisorController;
    private StudentController studentController;
    private ActivityController activityController;

   public MainMenuView(CoordinatorController cc, SupervisorController sc, StudentController stc) {
    this.coordinatorController = cc;
    this.supervisorController = sc;
    this.studentController = stc;
        ActivityCatalog activityCatalog = new ActivityCatalog(
            studentController.getStudentCatalog(),
            new ActivityTypeRepository()
        );
        this.activityController = new ActivityController(activityCatalog);


        setTitle("Sistema ACC - Menu Principal");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));

        JButton btnCoordinator = new JButton("Coordenador");
        JButton btnSupervisor = new JButton("Supervisor");
        JButton btnStudent = new JButton("Aluno");
        JButton btnExit = new JButton("Sair");

        btnCoordinator.addActionListener(e ->
            new LoginView("coordinator", coordinatorController, supervisorController, studentController, activityController));

        btnSupervisor.addActionListener(e ->
            new LoginView("supervisor", coordinatorController, supervisorController, studentController, activityController));

        btnStudent.addActionListener(e ->
            new LoginView("student", coordinatorController, supervisorController, studentController, activityController));
        

        btnExit.addActionListener(e -> System.exit(0));

        add(btnCoordinator);
        add(btnSupervisor);
        add(btnStudent);
        add(btnExit);

        setVisible(true);
    }
}

