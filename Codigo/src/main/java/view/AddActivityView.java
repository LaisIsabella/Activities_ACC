package view;

import controller.ActivityController;
import model.ActivityType;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import model.Student;

public class AddActivityView extends JFrame {

    private File selectedFile;

    public AddActivityView(Student student, ActivityController controller, java.util.List<ActivityType> types) {

        setTitle("Adicionar Atividade");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 2, 10, 10));

        JTextField txtName = new JTextField();
        JTextField txtDesc = new JTextField();
        JTextField txtDate = new JTextField();
        JTextField txtHours = new JTextField();

        JComboBox<ActivityType> comboType = new JComboBox<>(types.toArray(new ActivityType[0]));

        JButton btnAttach = new JButton("Anexar PDF");
        JButton btnSave = new JButton("Salvar");

        btnAttach.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int r = chooser.showOpenDialog(this);

            if (r == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                JOptionPane.showMessageDialog(this, "Arquivo selecionado");
            }
        });

        btnSave.addActionListener(e -> {  
        try {

            boolean ok = controller.createActivity(
                    student,
                    txtName.getText(),
                    txtDesc.getText(),
                    txtDate.getText(),
                    Integer.parseInt(txtHours.getText()),
                    (ActivityType) comboType.getSelectedItem(),
                    selectedFile
            );

            if (!ok) {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível salvar a atividade.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            JOptionPane.showMessageDialog(this, "Atividade adicionada!");
            dispose();

    } catch (IllegalArgumentException ex) {
        // Aqui aparecem os erros de limite, horas, falta de anexo etc.
        JOptionPane.showMessageDialog(
                this, 
                ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
        );

    } catch (Exception ex) {
        // Erros inesperados
        JOptionPane.showMessageDialog(
                this,
                "Falha interna: " + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
        );
        ex.printStackTrace();
    }
});


        add(new JLabel("Nome:")); add(txtName);
        add(new JLabel("Descrição:")); add(txtDesc);
        add(new JLabel("Data (DD/MM/AAAA):")); add(txtDate);
        add(new JLabel("Horas:")); add(txtHours);
        add(new JLabel("Tipo:")); add(comboType);
        add(btnAttach); add(btnSave);

        setVisible(true);
    }
}
