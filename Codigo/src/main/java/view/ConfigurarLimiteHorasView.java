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
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));

        combo = new JComboBox<>(controller.listarTipos().toArray(new ActivityType[0]));
        txtLimit = new JTextField();

        JButton btnSave = new JButton("Salvar");

        add(new JLabel("Tipo de Atividade:"));
        add(combo);
        add(new JLabel("Limite (horas):"));
        add(txtLimit);
        add(new JLabel());
        add(btnSave);

        btnSave.addActionListener(e -> salvar());

        setVisible(true);
    }

    private void salvar() {
        try {
            ActivityType tipo = (ActivityType) combo.getSelectedItem();
            String novoLimite = txtLimit.getText();

            controller.definirLimite(tipo, novoLimite);

            JOptionPane.showMessageDialog(this, "Limite atualizado com sucesso!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
