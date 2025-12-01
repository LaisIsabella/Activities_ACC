package view;

import model.Activity;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class StudentActivityDetailsView extends JFrame {

    public StudentActivityDetailsView(Activity activity) {
        setTitle("Detalhes da Atividade");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setPreferredSize(new Dimension(600, 80));

        JLabel lblTitle = new JLabel("Detalhes da Atividade", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);

        // ====== DETALHES ======
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(25, 30, 25, 30),
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1)
        ));

        // Grid de informações
        JPanel infoGrid = new JPanel(new GridLayout(5, 2, 15, 15));
        infoGrid.setOpaque(false);

        infoGrid.add(createInfoLabel("Nome:"));
        infoGrid.add(createValueLabel(activity.getName()));

        infoGrid.add(createInfoLabel("Tipo:"));
        infoGrid.add(createValueLabel(activity.getActivityType().getName()));

        infoGrid.add(createInfoLabel("Data:"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        infoGrid.add(createValueLabel(sdf.format(activity.getDate())));

        infoGrid.add(createInfoLabel("Horas:"));
        JLabel lblHours = createValueLabel(activity.getHours() + "h");
        lblHours.setForeground(new Color(155, 89, 182));
        lblHours.setFont(new Font("Segoe UI", Font.BOLD, 15));
        infoGrid.add(lblHours);

        infoGrid.add(createInfoLabel("Status:"));
        JLabel lblStatus = createValueLabel(getStatusText(activity.getStatus()));
        lblStatus.setForeground(getStatusColor(activity.getStatus()));
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoGrid.add(lblStatus);

        detailsPanel.add(infoGrid);

        // Descrição
        JPanel descPanel = new JPanel(new BorderLayout(0, 10));
        descPanel.setOpaque(false);
        descPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel lblDescTitle = new JLabel("Descrição:");
        lblDescTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDescTitle.setForeground(new Color(70, 70, 70));

        JTextArea txtDesc = new JTextArea(activity.getDescription());
        txtDesc.setEditable(false);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDesc.setBackground(new Color(250, 250, 250));
        txtDesc.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        descPanel.add(lblDescTitle, BorderLayout.NORTH);
        descPanel.add(txtDesc, BorderLayout.CENTER);

        detailsPanel.add(descPanel);

        // Resposta/Feedback (se houver)
        String response = activity.getResponse();
        if (response != null && !response.isBlank()) {
            JPanel responsePanel = new JPanel(new BorderLayout(0, 10));
            responsePanel.setOpaque(false);
            responsePanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

            JLabel lblResponseTitle = new JLabel("Resposta do Avaliador:");
            lblResponseTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblResponseTitle.setForeground(new Color(70, 70, 70));

            JTextArea txtResponse = new JTextArea(response);
            txtResponse.setEditable(false);
            txtResponse.setLineWrap(true);
            txtResponse.setWrapStyleWord(true);
            txtResponse.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            txtResponse.setBackground(new Color(255, 250, 245));
            txtResponse.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(getStatusColor(activity.getStatus())),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            responsePanel.add(lblResponseTitle, BorderLayout.NORTH);
            responsePanel.add(txtResponse, BorderLayout.CENTER);

            detailsPanel.add(responsePanel);
        }

        // ====== BOTÃO FECHAR ======
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        buttonPanel.setBackground(new Color(245, 245, 250));

        JButton btnFechar = new JButton("Fechar");
        stylePrimaryButton(btnFechar);
        btnFechar.addActionListener(e -> dispose());

        buttonPanel.add(btnFechar);

        // Container principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 20, 30));
        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Adiciona à janela
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(100, 100, 100));
        return label;
    }

    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    private String getStatusText(model.Status status) {
        return switch (status) {
            case PENDING -> "PENDENTE";
            case APPROVED -> "APROVADA";
            case DENIED -> "NEGADA";
            case PARTIALLY_DENIED -> "AJUSTES NECESSÁRIOS";
        };
    }

    private Color getStatusColor(model.Status status) {
        return switch (status) {
            case PENDING -> new Color(243, 156, 18);
            case APPROVED -> new Color(46, 204, 113);
            case DENIED -> new Color(231, 76, 60);
            case PARTIALLY_DENIED -> new Color(230, 126, 34);
        };
    }

    private void stylePrimaryButton(JButton button) {
        button.setPreferredSize(new Dimension(120, 40));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(155, 89, 182));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(142, 68, 173));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(155, 89, 182));
            }
        });
    }
}