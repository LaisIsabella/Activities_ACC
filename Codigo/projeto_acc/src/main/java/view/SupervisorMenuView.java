package view;

import controller.ActivityController;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import model.Supervisor;
import java.awt.*;
import javax.swing.*;

public class SupervisorMenuView extends JFrame {

    public SupervisorMenuView(ActivityController activityController, Supervisor supervisor) {
        setTitle("Sistema ACC - Supervisor");
        setSize(550, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setPreferredSize(new Dimension(550, 120));
        headerPanel.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(46, 204, 113));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel lblWelcome = new JLabel("Bem-vindo(a),");
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblWelcome.setForeground(new Color(230, 250, 240));
        lblWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblName = new JLabel(supervisor.getName());
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblName.setForeground(Color.WHITE);
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblRole = new JLabel("Supervisor");
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblRole.setForeground(new Color(230, 250, 240));
        lblRole.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(lblWelcome);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(lblName);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(lblRole);

        headerPanel.add(titlePanel, BorderLayout.CENTER);

        // ====== MENU ======
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBackground(new Color(245, 245, 250));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        JButton btnVerificar = createMenuButton(
            "Validar Atividades", 
            "Verifique e encaminhe atividades para o coordenador"
        );
        
        JButton btnSair = createExitButton(
            "Sair", 
            "Retornar ao menu principal"
        );

        btnVerificar.addActionListener(e -> new SupervisorValidateActivitiesView(activityController));
        btnSair.addActionListener(e -> dispose());

        gbc.gridy = 0;
        menuPanel.add(btnVerificar, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(30, 0, 10, 0);
        menuPanel.add(btnSair, gbc);

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
                button.setBackground(new Color(245, 255, 250));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
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