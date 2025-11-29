package view;

import controller.ActivityController;
import model.Activity;
import model.Student;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class GenerateReportView extends JFrame {

    public GenerateReportView(Student student, ActivityController controller) {
        setTitle("Gerar Relatório de Atividades");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Painel de informações
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Busca atividades aprovadas
        List<Activity> approvedActivities = controller.getAllStudentApprovedActivities(student);

        // Calcula total de horas
        int totalHours = 0;
        for (Activity a : approvedActivities) {
            totalHours += a.getHours();
        }

        JLabel lblInfo = new JLabel("Relatório de Atividades Complementares");
        lblInfo.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel lblStudent = new JLabel("Aluno: " + student.getName() + " (RA: " + student.getRa() + ")");
        JLabel lblActivities = new JLabel("Total de atividades aprovadas: " + approvedActivities.size());
        JLabel lblHours = new JLabel("Total de horas aprovadas: " + totalHours + "h");

        infoPanel.add(lblInfo);
        infoPanel.add(lblStudent);
        infoPanel.add(lblActivities);
        infoPanel.add(lblHours);

        add(infoPanel, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnGenerate = new JButton("Gerar PDF");
        JButton btnCancel = new JButton("Cancelar");

        buttonPanel.add(btnGenerate);
        buttonPanel.add(btnCancel);

        add(buttonPanel, BorderLayout.SOUTH);

        // Ação do botão Gerar
        btnGenerate.addActionListener(e -> {
            if (approvedActivities.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Você não possui atividades aprovadas para gerar o relatório.",
                    "Sem Atividades",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Abre diálogo para escolher local de salvamento
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar Relatório");
            fileChooser.setSelectedFile(new File("relatorio_" + student.getRa() + ".pdf"));

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();

                // Garante extensão .pdf
                if (!filePath.toLowerCase().endsWith(".pdf")) {
                    filePath += ".pdf";
                }

                // Gera o relatório
                boolean success = controller.generateUserReport(filePath, approvedActivities);

                if (success) {
                    int option = JOptionPane.showConfirmDialog(
                        this,
                        "Relatório gerado com sucesso em:\n" + filePath + "\n\nDeseja abrir o arquivo?",
                        "Sucesso",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE
                    );

                    if (option == JOptionPane.YES_OPTION) {
                        try {
                            Desktop.getDesktop().open(new File(filePath));
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(
                                this,
                                "Relatório gerado, mas não foi possível abri-lo automaticamente.",
                                "Aviso",
                                JOptionPane.WARNING_MESSAGE
                            );
                        }
                    }

                    dispose();
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Erro ao gerar o relatório. Verifique as permissões do diretório.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        // Ação do botão Cancelar
        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }
}