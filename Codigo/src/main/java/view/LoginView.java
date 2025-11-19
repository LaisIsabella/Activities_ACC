package view;

import javax.swing.*;
import java.awt.*;
import controller.*;
import model.Student;

public class LoginView extends JFrame {

    public LoginView(String userType, CoordinatorController cc, SupervisorController sc, StudentController stc) {
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

       btnLogin.addActionListener(e -> {
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
                JOptionPane.showMessageDialog(this, "Menu de Supervisor ainda não implementado.");
                dispose();
                return;
            }
        }

        else if (userType.equals("student")) {
            success = stc.loginStudent(email, password);

            if (success) {
                Student logged = stc.findStudentByEmail(email);
                new StudentMenuView(logged);
                dispose();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Falha no login!");
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
