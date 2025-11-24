package view;

import catalog.ActivityCatalog;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import controller.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.ActivityType;
import model.Student;
import model.Supervisor;




public class LoginView extends JFrame {
    
    private final ActivityController activityController;

    public LoginView(String userType,
                 CoordinatorController cc,
                 SupervisorController sc,
                 StudentController stc,
                 ActivityController ac) {
        this.activityController = ac;
        setTitle("Login - " + userType.toUpperCase());
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel lblEmail = new JLabel("E-mail:");
        JTextField txtEmail = new JTextField();

        JLabel lblPassword = new JLabel("Senha:");
        JPasswordField txtPassword = new JPasswordField();

        JButton btnLogin = new JButton("Entrar");
        JButton btnRegister = new JButton("Cadastrar");

       btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText();
                String password = new String(txtPassword.getPassword());
                boolean success = false;
                if (userType.equals("coordinator")) {
                    success = cc.loginCoordinator(email, password);
                    if (success) {
                        new CoordinatorMenuView();
                        dispose();
                        return;
                    }
                }
                
                else if (userType.equals("supervisor")) {
                    success = sc.loginSupervisor(email, password);
                    if (success) {
                        Supervisor logged = sc.getSupervisorCatalog().findSupervisorByEmail(email);
                        new SupervisorMenuView(activityController, logged);
                        dispose();
                        return;
                    }
                }
                
                else if (userType.equals("student")) {
                    success = stc.loginStudent(email, password);
                    
                    if (success) {
                        Student logged = stc.getStudentCatalog().findStudentByEmail(email);
                        List<ActivityType> activityTypes = new repository.ActivityTypeRepository().loadAll();
                        ActivityCatalog activityCatalog = new ActivityCatalog(stc.getStudentCatalog(), new repository.ActivityTypeRepository());
                        ActivityController ac = new ActivityController(activityCatalog);
                        new StudentMenuView(logged, ac, activityTypes); 
                        dispose();
                        return;
                    }
                }           JOptionPane.showMessageDialog(LoginView.this, "Falha no login!");
            }
        });

        btnRegister.addActionListener(e -> new RegisterView(userType, cc, sc, stc));

        add(lblEmail);
        add(txtEmail);
        add(lblPassword);
        add(txtPassword);
        add(new JLabel());
        add(btnLogin);
        add(new JLabel());
        add(btnRegister);

        setVisible(true);
    }
}
