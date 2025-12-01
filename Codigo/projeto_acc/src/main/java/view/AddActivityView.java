package view;

import controller.ActivityController;
import model.ActivityType;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import model.Document;
import model.Status;
import model.Student;

public class AddActivityView extends JFrame {

    private File selectedFile;
    private JLabel lblFileName;

    public AddActivityView(Student student, ActivityController controller, java.util.List<ActivityType> types) {

        setTitle("Nova Atividade Complementar");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setPreferredSize(new Dimension(600, 80));

        JLabel lblTitle = new JLabel("Adicionar Atividade", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);

        // ====== FORMULÁRIO ======
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 30, 20, 30),
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.weightx = 0.3;

        // Campos do formulário
        JTextField txtName = createTextField();
        JTextField txtDesc = createTextField();
        JTextField txtDate = createTextField();
        txtDate.setToolTipText("Formato: DD/MM/AAAA");
        JTextField txtHours = createTextField();
        JComboBox<ActivityType> comboType = new JComboBox<>(types.toArray(new ActivityType[0]));
        styleComboBox(comboType);

        // Nome
        gbc.gridy = 0;
        formPanel.add(createLabel("Nome da Atividade:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(txtName, gbc);

        // Descrição
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Descrição:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(txtDesc, gbc);

        // Tipo
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Tipo de Atividade:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(comboType, gbc);

        // Data
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Data (DD/MM/AAAA):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(txtDate, gbc);

        // Horas
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Horas:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(txtHours, gbc);

        // Anexo
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        formPanel.add(createLabel("Comprovante (PDF):"), gbc);

        JPanel attachPanel = new JPanel(new BorderLayout(10, 0));
        attachPanel.setOpaque(false);

        JButton btnAttach = new JButton("Escolher Arquivo");
        styleSecondaryButton(btnAttach);

        lblFileName = new JLabel("Nenhum arquivo selecionado");
        lblFileName.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblFileName.setForeground(new Color(120, 120, 120));

        attachPanel.add(btnAttach, BorderLayout.WEST);
        attachPanel.add(lblFileName, BorderLayout.CENTER);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(attachPanel, gbc);

        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton btnSave = new JButton("Salvar Atividade");
        stylePrimaryButton(btnSave);

        JButton btnCancel = new JButton("Cancelar");
        styleSecondaryButton(btnCancel);

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(buttonPanel, gbc);

        // ====== AÇÕES ======
        btnAttach.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().toLowerCase().endsWith(".pdf");
                }

                public String getDescription() {
                    return "Arquivos PDF (*.pdf)";
                }
            });

            int r = chooser.showOpenDialog(this);
            if (r == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                lblFileName.setText(selectedFile.getName());
                lblFileName.setForeground(new Color(46, 204, 113));
            }
        });

        btnSave.addActionListener(e -> {
            try {
                String name = txtName.getText();
                String desc = txtDesc.getText();
                String dateStr = txtDate.getText();
                String hoursStr = txtHours.getText();

                if (name.isBlank()) {
                    throw new IllegalArgumentException("Nome obrigatório.");
                }
                if (desc.isBlank()) {
                    throw new IllegalArgumentException("Descrição obrigatória.");
                }
                if (!dateStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    throw new IllegalArgumentException("Data inválida. Use DD/MM/AAAA.");
                }

                int hours = Integer.parseInt(hoursStr);
                if (hours <= 0) {
                    throw new IllegalArgumentException("Horas devem ser maior que zero.");
                }

                ActivityType type = (ActivityType) comboType.getSelectedItem();

                // ✅ VALIDAÇÃO CRÍTICA ADICIONADA
                if (type == null) {
                    throw new IllegalArgumentException("Selecione um tipo de atividade.");
                }

                // ✅ DEBUG (remova depois de testar)
                System.out.println("DEBUG - Tipo selecionado: " + type.getName() + " (Limite: " + type.getLimit() + ")");

                if (hours > type.getLimit()) {
                    throw new IllegalArgumentException(
                            "O tipo \"" + type.getName() + "\" permite no máximo " + type.getLimit() + " horas."
                    );
                }

                if (selectedFile == null) {
                    throw new IllegalArgumentException("Selecione um arquivo PDF.");
                }

                if (!selectedFile.getName().toLowerCase().endsWith(".pdf")) {
                    throw new IllegalArgumentException("O arquivo deve ser PDF.");
                }

                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 999);
                Date today = cal.getTime();

                if (date.after(today)) {
                    throw new IllegalArgumentException("A data não pode ser futura.");
                }

                File destFolder = new File("attachments");
                destFolder.mkdirs();

                File destFile = new File(destFolder, System.currentTimeMillis() + "_" + selectedFile.getName());
                selectedFile.renameTo(destFile);

                Document doc = new Document(selectedFile.getName(), destFile.getAbsolutePath());

                // ✅ DEBUG FINAL (remova depois de testar)
                System.out.println("DEBUG - Antes de criar atividade:");
                System.out.println("  Nome: " + name);
                System.out.println("  Tipo: " + (type != null ? type.getName() : "NULL! "));
                System.out.println("  Student: " + (student != null ? student.getName() : "NULL!"));

                boolean ok = controller.createActivity(name, desc, date, hours, Status.PENDING, type, student, doc);

                if (!ok) {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar a atividade.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(this, "Atividade criada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();  // ✅ Para ver a stack trace completa
            }
        });

        btnCancel.addActionListener(e -> dispose());

        // Container com scroll
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.add(formPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(70, 70, 70));
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(300, 35));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setPreferredSize(new Dimension(300, 35));
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
    }

    private void stylePrimaryButton(JButton button) {
        button.setPreferredSize(new Dimension(160, 40));
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

    private void styleSecondaryButton(JButton button) {
        button.setPreferredSize(new Dimension(140, 35));
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
}
