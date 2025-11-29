package view;

import javax.swing.*;
import java.awt.*;
import controller.*;

public class RegisterView extends JFrame {

    public RegisterView(String userType, CoordinatorController cc, SupervisorController sc, StudentController stc) {

        String title = switch (userType) {
            case "coordinator" ->
                "Cadastro - Coordenador";
            case "supervisor" ->
                "Cadastro - Supervisor";
            case "student" ->
                "Cadastro - Aluno";
            default ->
                "Cadastro";
        };

        setTitle(title);
        setSize(500, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        // ====== PAINEL DO TOPO ======
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setPreferredSize(new Dimension(500, 80));

        String icon = switch (userType) {
            case "coordinator" ->
                "";
            case "supervisor" ->
                "";
            case "student" ->
                "";
            default ->
                "";
        };

        JLabel lblTitle = new JLabel(icon + "  " + title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);

        // ====== PAINEL CENTRAL (FORMULÁRIO) ======
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 250));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.gridx = 0;

        // Nome
        gbc.gridy = 0;
        formPanel.add(createLabel("Nome completo"), gbc);
        JTextField txtName = createTextField();
        gbc.gridy = 1;
        formPanel.add(txtName, gbc);

        // Email
        gbc.gridy = 2;
        formPanel.add(createLabel("E-mail institucional (@unesp.br)"), gbc);
        JTextField txtEmail = createTextField();
        gbc.gridy = 3;
        formPanel.add(txtEmail, gbc);

        // Senha
        gbc.gridy = 4;
        formPanel.add(createLabel("Senha (mínimo 8 caracteres)"), gbc);
        JPasswordField txtPassword = new JPasswordField();
        styleTextField(txtPassword);
        gbc.gridy = 5;
        formPanel.add(txtPassword, gbc);

        // Campos dinâmicos
        JLabel lblExtra1 = createLabel("");
        JTextField txtExtra1 = createTextField();
        JLabel lblExtra2 = createLabel("");
        JTextField txtExtra2 = createTextField();

        switch (userType) {
            case "student" -> {
                lblExtra1.setText("CPF (somente números)");
                lblExtra2.setText("RA (Registro Acadêmico)");
                gbc.gridy = 6;
                formPanel.add(lblExtra1, gbc);
                gbc.gridy = 7;
                formPanel.add(txtExtra1, gbc);
                gbc.gridy = 8;
                formPanel.add(lblExtra2, gbc);
                gbc.gridy = 9;
                formPanel.add(txtExtra2, gbc);
            }
            case "supervisor" -> {
                lblExtra1.setText("CPF (somente números)");
                gbc.gridy = 6;
                formPanel.add(lblExtra1, gbc);
                gbc.gridy = 7;
                formPanel.add(txtExtra1, gbc);
            }
            case "coordinator" -> {
                lblExtra1.setText("RC (Registro do Coordenador)");
                gbc.gridy = 6;
                formPanel.add(lblExtra1, gbc);
                gbc.gridy = 7;
                formPanel.add(txtExtra1, gbc);
            }
        }

        // Botões
        JButton btnRegister = createStyledButton("CADASTRAR", new Color(46, 204, 113));
        JButton btnCancel = createStyledButton("CANCELAR", new Color(231, 76, 60));

        gbc.gridy = 10;
        gbc.insets = new Insets(25, 0, 8, 0);
        formPanel.add(btnRegister, gbc);
        gbc.gridy = 11;
        gbc.insets = new Insets(8, 0, 8, 0);
        formPanel.add(btnCancel, gbc);

        // ====== AÇÕES ======
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
                        String rc = extra1;
                        success = cc.createCoordinator(name, email, password, rc);
                    }
                    case "supervisor" -> {
                        if (sc.getSupervisorCatalog().findSupervisorByEmail(email) != null) {
                            JOptionPane.showMessageDialog(this, "Já existe um supervisor com esse e-mail.", "Erro", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        success = sc.createSupervisor(name, email, password, extra1);
                    }
                    case "student" -> {
                        String cpf = extra1;
                        String ra = extra2;

                        if (stc.getStudentCatalog().findStudentByEmail(email) != null) {
                            JOptionPane.showMessageDialog(this, "Já existe um aluno com esse e-mail.", "Erro", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (stc.getStudentCatalog().getStudents().stream().anyMatch(s -> s.getCpf().equals(cpf))) {
                            JOptionPane.showMessageDialog(this, "Já existe um aluno com esse CPF.", "Erro", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (stc.getStudentCatalog().getStudents().stream().anyMatch(s -> s.getRa().equals(ra))) {
                            JOptionPane.showMessageDialog(this, "Já existe um aluno com esse RA.", "Erro", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (!stc.validateStudent(name, email, password, cpf, ra)) {
                            JOptionPane.showMessageDialog(this, "Dados inválidos. Verifique os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        success = stc.createStudent(name, email, password, cpf, ra);
                    }

                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Campos numéricos inválidos (RA/RC).", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                new LoginView(userType, cc, sc, stc, null);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Falha no cadastro. Verifique os dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> {
            new LoginView(userType, cc, sc, stc, null);
            dispose();
        });

        // Adiciona à janela
        add(headerPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(70, 70, 70));
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        styleTextField(textField);
        return textField;
    }

    private void styleTextField(JTextField textField) {
        textField.setPreferredSize(new Dimension(350, 38));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(350, 45));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }
}
