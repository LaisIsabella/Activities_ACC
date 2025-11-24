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
        // validações
        String name = txtName.getText();
        String desc = txtDesc.getText();
        String dateStr = txtDate.getText();
        String hoursStr = txtHours.getText();

        if (name.isBlank()) throw new IllegalArgumentException("Nome obrigatório.");
        if (desc.isBlank()) throw new IllegalArgumentException("Descrição obrigatória.");

        if (!dateStr.matches("\\d{2}/\\d{2}/\\d{4}"))
            throw new IllegalArgumentException("Data inválida. Use DD/MM/AAAA.");

        int hours = Integer.parseInt(hoursStr);

        if (hours < 0) throw new IllegalArgumentException("Horas inválidas.");

        ActivityType type = (ActivityType) comboType.getSelectedItem();
        if (type == null) throw new IllegalArgumentException("Tipo obrigatório.");

        // regra do limite
        if (hours > type.getLimit())
            throw new IllegalArgumentException(
                    "O tipo \"" + type.getName() + "\" permite no máximo " +
                     type.getLimit() + " horas."
            );

        if (selectedFile == null)
            throw new IllegalArgumentException("Selecione um arquivo PDF.");

        if (!selectedFile.getName().toLowerCase().endsWith(".pdf"))
            throw new IllegalArgumentException("O arquivo deve ser PDF.");

        // validar data
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();

        if (date.before(today))
            throw new IllegalArgumentException("A data não pode ser passada.");

        // salvar anexo
        File destFolder = new File("attachments");
        destFolder.mkdirs();

        File destFile = new File(destFolder, System.currentTimeMillis() + "_" + selectedFile.getName());
        selectedFile.renameTo(destFile);

        Document doc = new Document(selectedFile.getName(), destFile.getAbsolutePath());

        boolean ok = controller.createActivity(
                name,
                desc,
                date,
                hours,
                Status.PENDING,
                type,
                student,
                doc
        );

        if (!ok) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar a atividade.");
            return;
        }

        JOptionPane.showMessageDialog(this, "Atividade criada com sucesso!");
        dispose();

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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
