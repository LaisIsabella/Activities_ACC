package view;

import catalog.ActivityCatalog;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import controller.*;
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

        // Título baseado no tipo de usuário
        String title = switch (userType) {
            case "coordinator" ->
                "Login - Coordenador";
            case "supervisor" ->
                "Login - Supervisor";
            case "student" ->
                "Login - Aluno";
            default ->
                "Login";
        };

        setTitle(title);
        setSize(450, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        // ====== PAINEL DO TOPO ======
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setPreferredSize(new Dimension(450, 100));
        headerPanel.setLayout(new BorderLayout());

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
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        headerPanel.add(lblTitle, BorderLayout.CENTER);

        // ====== PAINEL CENTRAL (FORMULÁRIO) ======
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 250));
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Label E-mail
        JLabel lblEmail = new JLabel("E-mail institucional");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEmail.setForeground(new Color(70, 70, 70));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblEmail, gbc);

        // Campo E-mail
        JTextField txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(300, 40));
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridy = 1;
        formPanel.add(txtEmail, gbc);

        // Label Senha
        JLabel lblPassword = new JLabel("Senha");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPassword.setForeground(new Color(70, 70, 70));
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        formPanel.add(lblPassword, gbc);

        // Campo Senha
        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(300, 40));
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 0, 10, 0);
        formPanel.add(txtPassword, gbc);

        // Botão Entrar
        JButton btnLogin = createStyledButton("ENTRAR", new Color(46, 204, 113));
        gbc.gridy = 4;
        gbc.insets = new Insets(30, 0, 10, 0);
        formPanel.add(btnLogin, gbc);

        // Botão Cadastrar
        JButton btnRegister = createStyledButton("CRIAR CONTA", new Color(52, 152, 219));
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 0, 10, 0);
        formPanel.add(btnRegister, gbc);

        // ====== AÇÕES DOS BOTÕES ======
        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText();
            String password = new String(txtPassword.getPassword());
            boolean success = false;

            if (userType.equals("coordinator")) {
                success = cc.authenticateCoordinator(email, password);
                if (success) {
                    new CoordinatorMenuView();
                    dispose();
                    return;
                }
            } else if (userType.equals("supervisor")) {
                success = sc.authenticateSupervisor(email, password);
                if (success) {
                    Supervisor logged = sc.getSupervisorCatalog().findSupervisorByEmail(email);
                    new SupervisorMenuView(activityController, logged);
                    dispose();
                    return;
                }
            } else if (userType.equals("student")) {
                success = stc.authenticateStudent(email, password);
                if (success) {
                    Student logged = stc.getStudentCatalog().findStudentByEmail(email);
                    if (logged == null) {
                        JOptionPane.showMessageDialog(
                                this,
                                "ERRO: Estudante não encontrado no sistema!\nEmail: " + email,
                                "Debug",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    List<ActivityType> activityTypes = new repository.ActivityTypeRepository().loadAll();
                    new StudentMenuView(logged, activityController, activityTypes);  // ✅ USA O activityController DO CONSTRUTOR
                    dispose();
                    return;
                }
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Email ou senha incorretos!",
                    "Erro de Login",
                    JOptionPane.ERROR_MESSAGE
            );
        });

        btnRegister.addActionListener(e -> {
            new RegisterView(userType, cc, sc, stc);
            dispose();
        });

        // Enter para fazer login
        txtPassword.addActionListener(e -> btnLogin.doClick());

        // Adiciona tudo à janela
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(300, 45));
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
