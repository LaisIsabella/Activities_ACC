package view;

import controller.ActivityTypeController;
import model.ActivityType;

import javax.swing.*;
import java.awt.*;

public class ConfigurarLimiteHorasView extends JFrame {

    private JComboBox<ActivityType> combo;
    private JTextField txtLimit;
    private final ActivityTypeController controller;

    public ConfigurarLimiteHorasView(ActivityTypeController controller) {
        this.controller = controller;

        setTitle("Configurar Limites de Horas");
        setSize(550, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setPreferredSize(new Dimension(550, 90));

        JLabel lblTitle = new JLabel("Configurar Limites de Horas", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);

        // ====== FORMULÁRIO ======
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(30, 40, 30, 40),
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1)
        ));
        formPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 10, 12, 10);

        // Tipo de Atividade
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Tipo de Atividade:"), gbc);

        combo = new JComboBox<>(controller.listTypes().toArray(new ActivityType[0]));
        styleComboBox(combo);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(combo, gbc);

        // Limite de Horas
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Novo Limite (horas):"), gbc);

        txtLimit = createTextField();
        txtLimit.setToolTipText("Digite o limite de horas (máximo 200)");
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(txtLimit, gbc);

        // Info box
        JPanel infoPanel = new JPanel(new BorderLayout(10, 0));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 0, 0, 0),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(52, 152, 219), 1),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        ));
        infoPanel.setBackground(new Color(235, 245, 255));

        JLabel lblInfo = new JLabel("<html><b>Importante:</b> O limite deve estar entre 0 e 200 horas.</html>");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(52, 152, 219));
        infoPanel.add(lblInfo, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 12, 10);
        formPanel.add(infoPanel, gbc);

        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton btnCancel = new JButton("Cancelar");
        styleSecondaryButton(btnCancel);
        btnCancel.addActionListener(e -> dispose());

        JButton btnSave = new JButton("Salvar Limite");
        stylePrimaryButton(btnSave);
        btnSave.addActionListener(e -> salvar());

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);

        gbc.gridy = 3;
        gbc.insets = new Insets(20, 10, 12, 10);
        formPanel.add(buttonPanel, gbc);

        // Container principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Adiciona à janela
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void salvar() {
        try {
            ActivityType tipoSelecionado = (ActivityType) combo.getSelectedItem();
            String novoLimite = txtLimit.getText().trim();

            if (tipoSelecionado == null) {
                showError("Selecione um tipo de atividade.");
                return;
            }

            if (novoLimite.isEmpty()) {
                showError("Informe o limite de horas.");
                return;
            }

            int limite = Integer.parseInt(novoLimite);

            if (limite <= 0) {
                showError("O limite deve ser maior que 0.");
                return;
            }

            controller.setHourLimit(tipoSelecionado, novoLimite);

            showSuccess("Limite atualizado com sucesso!\n\n"
                    + "Tipo: " + tipoSelecionado.getName() + "\n"
                    + "Novo limite: " + novoLimite + " horas");
            dispose();

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(70, 70, 70));
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(250, 35));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setPreferredSize(new Dimension(250, 35));
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
    }

    private void stylePrimaryButton(JButton button) {
        button.setPreferredSize(new Dimension(140, 40));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 152, 219));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 152, 219));
            }
        });
    }

    private void styleSecondaryButton(JButton button) {
        button.setPreferredSize(new Dimension(110, 40));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setForeground(new Color(70, 70, 70));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(245, 245, 245));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
}
