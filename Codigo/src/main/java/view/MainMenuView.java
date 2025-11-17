package view;

import javax.swing.*;
import java.awt.*;
import controller.*;

public class MainMenuView extends JFrame {

    private CoordinatorController coordinatorController;
    private SupervisorController supervisorController;
    private StudentController studentController;

    public MainMenuView(CoordinatorController cc, SupervisorController sc, StudentController stc) {
        this.coordinatorController = cc;
        this.supervisorController = sc;
        this.studentController = stc;

        setTitle("Sistema ACC - Menu Principal");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));

        JButton btnCoordinator = new JButton("Coordenador");
        JButton btnSupervisor = new JButton("Supervisor");
        JButton btnStudent = new JButton("Aluno");
        JButton btnExit = new JButton("Sair");

        btnCoordinator.addActionListener(e -> new LoginView("coordinator", coordinatorController, null, null));
        btnSupervisor.addActionListener(e -> new LoginView("supervisor", null, supervisorController, null));
        btnStudent.addActionListener(e -> new LoginView("student", null, null, studentController));
        btnExit.addActionListener(e -> System.exit(0));

        add(btnCoordinator);
        add(btnSupervisor);
        add(btnStudent);
        add(btnExit);

        setVisible(true);
    }
}
