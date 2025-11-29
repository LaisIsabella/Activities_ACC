package view;

import catalog.ActivityCatalog;
import catalog.StudentCatalog;
import controller.ActivityController;
import controller.ActivityTypeController;

import javax.swing.*;
import java.awt.*;
import repository.ActivityTypeRepository;

public class CoordinatorMenuView extends JFrame {

    public CoordinatorMenuView() {

        StudentCatalog studentCatalog = new StudentCatalog();
        ActivityTypeRepository typeRepo = new ActivityTypeRepository();
        ActivityCatalog aCatalog = new ActivityCatalog(studentCatalog, typeRepo);
        ActivityController controller = new ActivityController(aCatalog);

        setTitle("Sistema ACC - Coordenador");
        setSize(550, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setPreferredSize(new Dimension(550, 120));
        headerPanel.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(52, 152, 219));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel lblTitle = new JLabel("Painel do Coordenador");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Gerencie atividades e configure o sistema");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(230, 240, 255));
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(lblTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(lblSubtitle);

        headerPanel.add(titlePanel, BorderLayout.CENTER);

        // ====== MENU ======
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBackground(new Color(245, 245, 250));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Botões do menu
        JButton btnConfigLimits = createMenuButton(
            "Configurar Limites de Horas", 
            "Defina os limites de horas por tipo de atividade"
        );
        
        JButton btnManageActivities = createMenuButton(
            "Buscar e Avaliar Atividades", 
            "Visualize e avalie as atividades dos alunos"
        );
        
        JButton btnLogout = createExitButton(
            "Sair", 
            "Retornar ao menu principal"
        );

        // Ações
        btnConfigLimits.addActionListener(e ->
                new ConfigurarLimiteHorasView(new ActivityTypeController()));

        btnManageActivities.addActionListener(e -> 
                new CoordinatorSearchActivityView(controller, studentCatalog));

        btnLogout.addActionListener(e -> dispose());

        // Adiciona botões
        gbc.gridy = 0;
        menuPanel.add(btnConfigLimits, gbc);
        gbc.gridy = 1;
        menuPanel.add(btnManageActivities, gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(25, 0, 10, 0);
        menuPanel.add(btnLogout, gbc);

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
                button.setBackground(new Color(248, 250, 255));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
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