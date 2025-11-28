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

        btnCoordinator.addActionListener(e
                -> new LoginView("coordinator", coordinatorController, supervisorController, studentController, activityController));

        btnSupervisor.addActionListener(e
                -> new LoginView("supervisor", coordinatorController, supervisorController, studentController, activityController));

        btnStudent.addActionListener(e
                -> new LoginView("student", coordinatorController, supervisorController, studentController, activityController));

        btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Tem certeza que deseja encerrar a sessão?",
                    "Confirmar saída",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Aqui encerramos a "sessão" e finalizamos processos
                    dispose(); // Fecha a janela
                    System.exit(0); // Encerra aplicação
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Erro ao encerrar o sistema. Tente novamente.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        add(btnCoordinator);
        add(btnSupervisor);
        add(btnStudent);
        add(btnExit);

        setVisible(true);
    }
}
