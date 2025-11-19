package view;

import javax.swing.*;
import java.awt.*;
import controller.*;

public class RegisterView extends JFrame {

    public RegisterView(String userType, CoordinatorController cc, SupervisorController sc, StudentController stc) {
        setTitle("Cadastro - " + userType.toUpperCase());
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 2, 10, 10));

        JLabel lblName = new JLabel("Nome:");
        JTextField txtName = new JTextField();

        JLabel lblEmail = new JLabel("E-mail:");
        JTextField txtEmail = new JTextField();

        JLabel lblPassword = new JLabel("Senha:");
        JPasswordField txtPassword = new JPasswordField();

        JLabel lblExtra = new JLabel(userType.equals("student") ? "RA:" : userType.equals("supervisor") ? "CPF:" : "RC:");
        JTextField txtExtra = new JTextField();

        JButton btnRegister = new JButton("Registrar");
        JButton btnCancel = new JButton("Cancelar");

        btnRegister.addActionListener(e -> {
            String name = txtName.getText();
            String email = txtEmail.getText();
            String password = new String(txtPassword.getPassword());
            String extra = txtExtra.getText();
            boolean success = false;

            switch (userType) {
                case "coordinator" -> success = cc.createCoordinator(name, email, password, Integer.parseInt(extra));
                case "supervisor" -> success = sc.createSupervisor(name, email, password, extra);
                case "student" -> success = stc.createStudent(name, email, password, extra, Integer.parseInt(extra));
            }

            JOptionPane.showMessageDialog(this, success ? "Cadastro realizado com sucesso!" : "Falha no cadastro!");
        });

        btnCancel.addActionListener(e -> dispose());

        add(lblName); add(txtName);
        add(lblEmail); add(txtEmail);
        add(lblPassword); add(txtPassword);
        add(lblExtra); add(txtExtra);
        add(new JLabel()); add(btnRegister);
        add(new JLabel()); add(btnCancel);

        setVisible(true);
    }
}
