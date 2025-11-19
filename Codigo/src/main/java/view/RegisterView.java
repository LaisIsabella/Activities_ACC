package view;

import javax.swing.*;
import java.awt.*;
import controller.*;

public class RegisterView extends JFrame {

    public RegisterView(String userType, CoordinatorController cc, SupervisorController sc, StudentController stc) {
        setTitle("Cadastro - " + userType.toUpperCase());
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(8, 2, 10, 10));

        JLabel lblName = new JLabel("Nome:");
        JTextField txtName = new JTextField();

        JLabel lblEmail = new JLabel("E-mail:");
        JTextField txtEmail = new JTextField();

        JLabel lblPassword = new JLabel("Senha:");
        JPasswordField txtPassword = new JPasswordField();

        // Campos dinâmicos
        JLabel lblExtra1 = new JLabel();
        JTextField txtExtra1 = new JTextField();

        JLabel lblExtra2 = new JLabel();
        JTextField txtExtra2 = new JTextField();

        // Configuração por tipo
        switch (userType) {
            case "student" -> {
                lblExtra1.setText("CPF:");
                lblExtra2.setText("RA:");
                lblExtra2.setVisible(true);
                txtExtra2.setVisible(true);
            }
            case "supervisor" -> {
                lblExtra1.setText("CPF:");
                lblExtra2.setVisible(false);
                txtExtra2.setVisible(false);
            }
            case "coordinator" -> {
                lblExtra1.setText("RC:");
                lblExtra2.setVisible(false);
                txtExtra2.setVisible(false);
            }
        }

        JButton btnRegister = new JButton("Registrar");
        JButton btnCancel = new JButton("Cancelar");

        btnRegister.addActionListener(e -> {
            String name = txtName.getText();
            String email = txtEmail.getText();
            String password = new String(txtPassword.getPassword());
            String extra1 = txtExtra1.getText();
            String extra2 = txtExtra2.getText();
            boolean success = false;

            try {
                switch (userType) {

                    case "coordinator" -> {
                        int rc = Integer.parseInt(extra1);
                        success = cc.createCoordinator(name, email, password, rc);
                    }

                    case "supervisor" -> {
                        success = sc.createSupervisor(name, email, password, extra1);
                    }

                    case "student" -> {
                        String cpf = extra1;
                        int ra = Integer.parseInt(extra2);
                        success = stc.createStudent(name, email, password, cpf, ra);
                    }
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Campos numéricos inválidos (RA/RC).",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this,
                    success ? "Cadastro realizado com sucesso!" : "Falha no cadastro!");
        });

        btnCancel.addActionListener(e -> dispose());

        // Construção da interface
        add(lblName); add(txtName);
        add(lblEmail); add(txtEmail);
        add(lblPassword); add(txtPassword);
        add(lblExtra1); add(txtExtra1);
        add(lblExtra2); add(txtExtra2);
        add(btnRegister); add(btnCancel);

        setVisible(true);
    }
}
