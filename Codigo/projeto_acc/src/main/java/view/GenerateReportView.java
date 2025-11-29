package view;

import controller.ActivityController;
import model.Student;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GenerateReportView extends JFrame {

    public GenerateReportView(Student student, ActivityController controller) {
        setTitle("Gerar Relatório");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setPreferredSize(new Dimension(450, 80));

        JLabel lblTitle = new JLabel("Gerar Relatório em PDF", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);

        // ====== INFORMAÇÕES DO ALUNO ======
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel lblStudent = new JLabel("Aluno: " + student.getName());
        lblStudent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblStudent.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblRa = new JLabel("RA: " + student.getRa());
        lblRa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblRa.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(lblStudent);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(lblRa);

        // ====== BOTÕES ======
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(new Color(245, 245, 250));

        JButton btnGenerate = new JButton("Gerar PDF");
        stylePrimaryButton(btnGenerate);

        JButton btnCancel = new JButton("Cancelar");
        styleSecondaryButton(btnCancel);

        buttonPanel.add(btnGenerate);
        buttonPanel.add(btnCancel);

        // ====== AÇÃO DO BOTÃO GERAR ======
        btnGenerate.addActionListener(e -> {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar Relatório");
            fileChooser.setSelectedFile(new File("relatorio_" + student.getRa() + ".pdf"));

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();

                if (!filePath.toLowerCase().endsWith(".pdf")) {
                    filePath += ".pdf";
                }

                boolean success = controller.generateUserReport(student, filePath);

                if (success) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Relatório gerado com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    dispose();

                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Erro ao gerar o relatório.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        btnCancel.addActionListener(e -> dispose());

        add(headerPanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void stylePrimaryButton(JButton button) {
        button.setPreferredSize(new Dimension(150, 45));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(155, 89, 182));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleSecondaryButton(JButton button) {
        button.setPreferredSize(new Dimension(120, 45));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(new Color(70, 70, 70));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
