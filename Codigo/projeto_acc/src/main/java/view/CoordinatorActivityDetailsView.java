package view;

import controller.ActivityController;
import model.Activity;
import model.Status;

import javax.swing.*;
import java.awt.*;

public class CoordinatorActivityDetailsView extends JFrame {

    public CoordinatorActivityDetailsView(Activity activity, ActivityController controller) {

        setTitle("Avaliar Atividade");
        setSize(650, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setPreferredSize(new Dimension(650, 90));

        JLabel lblTitle = new JLabel("Avaliação de Atividade", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);

        // ====== DETALHES DA ATIVIDADE ======
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(20, 30, 20, 30),
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1)
        ));

        // Título da seção
        JLabel lblSectionTitle = new JLabel("Informações da Atividade");
        lblSectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSectionTitle.setForeground(new Color(50, 50, 50));
        lblSectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblSectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        detailsPanel.add(lblSectionTitle);

        // Grid de informações
        JPanel infoGrid = new JPanel(new GridLayout(6, 2, 15, 12));
        infoGrid.setOpaque(false);
        infoGrid.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        infoGrid.add(createInfoLabel("Aluno:"));
        infoGrid.add(createValueLabel(activity.getStudent().getName()));

        infoGrid.add(createInfoLabel("RA:"));
        infoGrid.add(createValueLabel(String.valueOf(activity.getStudent().getRa())));

        infoGrid.add(createInfoLabel("Atividade:"));
        infoGrid.add(createValueLabel(activity.getName()));

        infoGrid.add(createInfoLabel("Tipo:"));
        infoGrid.add(createValueLabel(activity.getActivityType().getName()));

        infoGrid.add(createInfoLabel("Horas Solicitadas:"));
        JLabel lblHours = createValueLabel(activity.getHours() + "h");
        lblHours.setForeground(new Color(52, 152, 219));
        lblHours.setFont(new Font("Segoe UI", Font.BOLD, 15));
        infoGrid.add(lblHours);

        infoGrid.add(createInfoLabel("Status Atual:"));
        JLabel lblStatus = createValueLabel(getStatusText(activity.getStatus()));
        lblStatus.setForeground(getStatusColor(activity.getStatus()));
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoGrid.add(lblStatus);

        detailsPanel.add(infoGrid);

        // Descrição
        JPanel descPanel = new JPanel(new BorderLayout(0, 8));
        descPanel.setOpaque(false);
        descPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

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

        // ====== PAINEL DE BOTÕES DE AÇÃO ======
        JPanel actionPanel = new JPanel(new GridLayout(3, 1, 0, 12));
        actionPanel.setBackground(new Color(245, 245, 250));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JButton btnApprove = createActionButton("Aprovar Atividade", new Color(46, 204, 113));
        JButton btnDeny = createActionButton("Negar Atividade", new Color(231, 76, 60));
        JButton btnPartial = createActionButton("Solicitar Ajustes", new Color(230, 126, 34));

        actionPanel.add(btnApprove);
        actionPanel.add(btnDeny);
        actionPanel.add(btnPartial);

        // ====== AÇÕES DOS BOTÕES ======

        // APROVAR
        btnApprove.addActionListener(e -> {
            String value = JOptionPane.showInputDialog(
                this,
                "Informe a quantidade de horas a serem aprovadas:",
                "Aprovar Atividade",
                JOptionPane.PLAIN_MESSAGE
            );

            if (value == null) return;

            value = value.trim();

            if (value.isEmpty()) {
                showError("Campo vazio. Informe um valor válido.");
                return;
            }

            int hours;
            try {
                hours = Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                showError("Valor inválido. Digite apenas números.");
                return;
            }

            if (hours <= 0) {
                showError("O valor deve ser maior que zero.");
                return;
            }

            int limit = activity.getActivityType().getLimit();

            if (hours > limit) {
                int option = JOptionPane.showConfirmDialog(
                    this,
                    String.format(
                        "As horas informadas (%d) excedem o limite permitido (%d).\n\n" +
                        "Deseja aplicar automaticamente o limite máximo?",
                        hours, limit
                    ),
                    "Ajustar ao Limite",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );

                if (option == JOptionPane.NO_OPTION) return;
                hours = limit;
            }

            try {
                controller.approveActivity(activity, hours);
                showSuccess("Atividade aprovada com " + hours + " horas!\n\nO aluno será notificado por e-mail.");
                dispose();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        // NEGAR
        btnDeny.addActionListener(e -> {
            JTextArea txtJustification = new JTextArea(5, 30);
            txtJustification.setLineWrap(true);
            txtJustification.setWrapStyleWord(true);
            txtJustification.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            txtJustification.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));

            JPanel panel = new JPanel(new BorderLayout(0, 10));
            panel.add(new JLabel("Justificativa da negação:"), BorderLayout.NORTH);
            panel.add(new JScrollPane(txtJustification), BorderLayout.CENTER);

            int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Negar Atividade",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                String response = txtJustification.getText().trim();

                if (response.isEmpty()) {
                    showError("A justificativa é obrigatória.");
                    return;
                }

                try {
                    controller.denyActivity(activity, response);
                    showSuccess("Atividade negada!\n\nO aluno será notificado por e-mail.");
                    dispose();
                } catch (Exception ex) {
                    showError(ex.getMessage());
                }
            }
        });

        // SOLICITAR AJUSTES
        btnPartial.addActionListener(e -> {
            JTextArea txtAdjustments = new JTextArea(5, 30);
            txtAdjustments.setLineWrap(true);
            txtAdjustments.setWrapStyleWord(true);
            txtAdjustments.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            txtAdjustments.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));

            JPanel panel = new JPanel(new BorderLayout(0, 10));
            panel.add(new JLabel("Descreva os ajustes necessários:"), BorderLayout.NORTH);
            panel.add(new JScrollPane(txtAdjustments), BorderLayout.CENTER);

            int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Solicitar Ajustes",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                String response = txtAdjustments.getText().trim();

                if (response.isEmpty()) {
                    showError("Descreva os ajustes necessários.");
                    return;
                }

                try {
                    controller.partiallyDenyActivity(activity, response);
                    showSuccess("Solicitação de ajustes enviada!\n\nO aluno será notificado por e-mail.");
                    dispose();
                } catch (Exception ex) {
                    showError(ex.getMessage());
                }
            }
        });

        // ====== CONTAINER PRINCIPAL ======
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Adiciona à janela
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

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

    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(550, 50));
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
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

    private String getStatusText(Status status) {
        return switch (status) {
            case PENDING -> "PENDENTE";
            case APPROVED -> "APROVADA";
            case DENIED -> "NEGADA";
            case PARTIALLY_DENIED -> "AJUSTES NECESSÁRIOS";
        };
    }

    private Color getStatusColor(Status status) {
        return switch (status) {
            case PENDING -> new Color(243, 156, 18);
            case APPROVED -> new Color(46, 204, 113);
            case DENIED -> new Color(231, 76, 60);
            case PARTIALLY_DENIED -> new Color(230, 126, 34);
        };
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Erro",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Sucesso",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}