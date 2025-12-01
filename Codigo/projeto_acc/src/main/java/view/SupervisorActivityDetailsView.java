package view;

import controller.ActivityController;
import model.Activity;

import javax.swing.*;
import java.awt.*;

public class SupervisorActivityDetailsView extends JFrame {

    public SupervisorActivityDetailsView(Activity activity,
                                         ActivityController controller,
                                         DefaultListModel<Activity> model) {
        setTitle("Validar Atividade");
        setSize(650, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setPreferredSize(new Dimension(650, 90));

        JLabel lblTitle = new JLabel("Validação de Atividade", SwingConstants.CENTER);
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
        JPanel infoGrid = new JPanel(new GridLayout(5, 2, 15, 12));
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
        lblHours.setForeground(new Color(46, 204, 113));
        lblHours.setFont(new Font("Segoe UI", Font.BOLD, 15));
        infoGrid.add(lblHours);

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

        // Alerta sobre documento
        JPanel alertPanel = new JPanel(new BorderLayout(10, 0));
        alertPanel.setOpaque(false);
        alertPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(15, 0, 0, 0),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            )
        ));
        alertPanel.setBackground(new Color(235, 245, 255));

        JLabel lblAlert = new JLabel("<html><b>Importante:</b> Verifique o documento anexado antes de validar a atividade.</html>");
        lblAlert.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAlert.setForeground(new Color(52, 152, 219));
        alertPanel.add(lblAlert, BorderLayout.CENTER);

        detailsPanel.add(alertPanel);

        // ====== PAINEL DE BOTÕES DE AÇÃO ======
        JPanel actionPanel = new JPanel(new GridLayout(3, 1, 0, 12));
        actionPanel.setBackground(new Color(245, 245, 250));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JButton btnVerArquivo = createActionButton("Abrir Documento Anexado", new Color(52, 152, 219));
        JButton btnValidar = createActionButton("Validar e Encaminhar ao Coordenador", new Color(46, 204, 113));
        JButton btnNegar = createActionButton("Negar Atividade", new Color(231, 76, 60));

        actionPanel.add(btnVerArquivo);
        actionPanel.add(btnValidar);
        actionPanel.add(btnNegar);

        // ====== AÇÕES DOS BOTÕES ======

        // Abrir documento
        btnVerArquivo.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(activity.getDocument().getFile());
            } catch (Exception ex) {
                showError("Erro ao abrir o documento.\n\nVerifique se o arquivo existe no caminho:\n" + 
                         activity.getDocument().getFilePath());
            }
        });

        // Validar e encaminhar
        btnValidar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Confirma que o documento foi verificado e a atividade está correta?\n\n" +
                "A atividade será encaminhada ao coordenador para aprovação final.",
                "Confirmar Validação",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = controller.verifyActivity(activity);
                if (success) {
                    showSuccess("Atividade validada com sucesso!\n\nEncaminhada ao coordenador para aprovação final.");
                    model.removeElement(activity);
                    dispose();
                } else {
                    showError("Erro ao validar atividade. Tente novamente.");
                }
            }
        });

        // Negar com justificativa
        btnNegar.addActionListener(e -> {
            JTextArea txtJustification = new JTextArea(5, 35);
            txtJustification.setLineWrap(true);
            txtJustification.setWrapStyleWord(true);
            txtJustification.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            txtJustification.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
            ));

            JPanel panel = new JPanel(new BorderLayout(0, 10));
            JLabel lblInfo = new JLabel("<html><b>Justificativa da negação:</b><br>" +
                                       "Explique por que a atividade está sendo negada.</html>");
            lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            
            panel.add(lblInfo, BorderLayout.NORTH);
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

                boolean success = controller.denyActivitySupervisor(activity, response);
                if (success) {
                    showSuccess("Atividade negada!\n\nO aluno " + activity.getStudent().getName() + 
                               " será notificado por e-mail.");
                    model.removeElement(activity);
                    dispose();
                } else {
                    showError("Erro ao negar atividade. Tente novamente.");
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