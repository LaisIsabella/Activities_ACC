package view;

import controller.ActivityController;
import model.ActivityType;
import model.Student;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentMenuView extends JFrame {

    private final Student student;
    private final ActivityController activityController;
    private final List<ActivityType> activityTypes;

    public StudentMenuView(Student student, ActivityController activityController, List<ActivityType> activityTypes) {
        this.student = student;
        this.activityController = activityController;
        this.activityTypes = activityTypes;

        setTitle("Sistema ACC - Menu do Aluno");
        setSize(550, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        // ====== PAINEL DO TOPO (HEADER) ======
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setPreferredSize(new Dimension(550, 120));
        headerPanel.setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(155, 89, 182));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblWelcome = new JLabel("Bem-vindo(a),");
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblWelcome.setForeground(new Color(230, 230, 250));
        lblWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblName = new JLabel(student.getName());
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblName.setForeground(Color.WHITE);
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblRA = new JLabel("RA: " + student.getRa());
        lblRA.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblRA.setForeground(new Color(230, 230, 250));
        lblRA.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(lblWelcome);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(lblName);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(lblRA);

        headerPanel.add(infoPanel, BorderLayout.CENTER);

        // ====== PAINEL CENTRAL (MENU) ======
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBackground(new Color(245, 245, 250));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        // ✅ Botões do menu (SEM "Caixa de Mensagens")
        JButton btnAdd = createMenuButton("Adicionar Atividade", "Submeta suas atividades complementares");
        JButton btnViewLimits = createMenuButton("Limites de Horas", "Consulte os limites por tipo de atividade");
        JButton btnHistory = createMenuButton("Histórico de Atividades", "Visualize suas atividades submetidas");
        JButton btnGenerateReport = createMenuButton("Gerar Relatório", "Baixe o relatório das suas atividades");
        JButton btnExit = createExitButton("Sair", "Voltar ao menu principal");

        // Ações dos botões
        btnAdd.addActionListener(e
                -> new AddActivityView(student, activityController, activityTypes));

        btnViewLimits.addActionListener(e
                -> new StudentHourLimitsView());

        btnHistory.addActionListener(e
                -> new StudentHistoryView(student, activityController));

        btnGenerateReport.addActionListener(e
                -> new GenerateReportView(student, activityController));

        btnExit.addActionListener(e -> dispose());

        // Adiciona botões ao painel
        gbc.gridy = 0;
        menuPanel.add(btnAdd, gbc);
        gbc.gridy = 1;
        menuPanel.add(btnViewLimits, gbc);
        gbc.gridy = 2;
        menuPanel.add(btnHistory, gbc);
        gbc.gridy = 3;
        menuPanel.add(btnGenerateReport, gbc);
        gbc.gridy = 4;
        gbc.insets = new Insets(25, 0, 10, 0);
        menuPanel.add(btnExit, gbc);

        // Adiciona à janela
        add(headerPanel, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createMenuButton(String title, String subtitle) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout(10, 0));
        button.setPreferredSize(new Dimension(420, 70));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(new Color(50, 50, 50));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitle = new JLabel(subtitle);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(new Color(120, 120, 120));
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(lblTitle);
        textPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        textPanel.add(lblSubtitle);

        button.add(textPanel, BorderLayout.CENTER);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(248, 248, 255));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });

        return button;
    }

    private JButton createExitButton(String title, String subtitle) {
        JButton button = createMenuButton(title, subtitle);
        button.setBackground(new Color(255, 250, 250));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 240, 240));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(231, 76, 60), 2),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 250, 250));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });
        
        return button;
    }
}