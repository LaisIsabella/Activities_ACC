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
        
        // ✅ IMPORTANTE: Injetar o StudentController no ActivityController
        // Conforme diagrama de colaboração - necessário para chamar addStudentHours()
        this.activityController.setStudentController(studentController);

        // Configurações da janela
        setTitle("Sistema de Atividades Complementares - UNESP");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Define cor de fundo
        getContentPane().setBackground(new Color(245, 245, 250));

        // ====== PAINEL DO TOPO (HEADER) ======
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setPreferredSize(new Dimension(500, 120));
        headerPanel.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Sistema ACC", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);
        
        JLabel lblSubtitle = new JLabel("Atividades Complementares - UNESP", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(230, 240, 255));

        JPanel titleContainer = new JPanel(new GridLayout(2, 1, 0, 5));
        titleContainer.setBackground(new Color(0, 102, 204));
        titleContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        titleContainer.add(lblTitle);
        titleContainer.add(lblSubtitle);

        headerPanel.add(titleContainer, BorderLayout.CENTER);

        // ====== PAINEL CENTRAL (BOTÕES) ======
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 245, 250));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Botões modernos
        JButton btnCoordinator = createStyledButton("COORDENADOR", new Color(52, 152, 219));
        JButton btnSupervisor = createStyledButton("SUPERVISOR", new Color(46, 204, 113));
        JButton btnStudent = createStyledButton("ALUNO", new Color(155, 89, 182));
        JButton btnExit = createStyledButton("SAIR", new Color(231, 76, 60));

        // Ações dos botões
        btnCoordinator.addActionListener(e
                -> new LoginView("coordinator", coordinatorController, supervisorController, studentController, activityController));

        btnSupervisor.addActionListener(e
                -> new LoginView("supervisor", coordinatorController, supervisorController, studentController, activityController));

        btnStudent.addActionListener(e
                -> new LoginView("student", coordinatorController, supervisorController, studentController, activityController));

        btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Tem certeza que deseja sair do sistema?",
                    "Confirmar Saída",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                System.exit(0);
            }
        });

        // Adiciona botões ao painel
        gbc.gridy = 0;
        centerPanel.add(btnCoordinator, gbc);
        gbc.gridy = 1;
        centerPanel.add(btnSupervisor, gbc);
        gbc.gridy = 2;
        centerPanel.add(btnStudent, gbc);
        gbc.gridy = 3;
        gbc.insets = new Insets(30, 0, 10, 0);
        centerPanel.add(btnExit, gbc);

        // ====== PAINEL INFERIOR (FOOTER) ======
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(245, 245, 250));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        JLabel lblFooter = new JLabel("© 2025 UNESP - Todos os direitos reservados", SwingConstants.CENTER);
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(120, 120, 120));
        footerPanel.add(lblFooter);

        // Adiciona tudo à janela
        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Método para criar botões estilizados
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(350, 60));
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover
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