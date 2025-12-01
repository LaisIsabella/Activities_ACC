package view;

import controller.ActivityController;
import model.Activity;

import javax.swing.*;
import java.awt.*;
import model.Status;
import model.Student;

public class DeleteActivityView extends JFrame {

    public DeleteActivityView(Student student, ActivityController controller, java.util.List<Activity> activities) {

        setTitle("Excluir Atividades");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(231, 76, 60));
        headerPanel.setPreferredSize(new Dimension(700, 90));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(231, 76, 60));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("Excluir Atividades");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Apenas atividades PENDENTES podem ser excluídas");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(255, 240, 240));
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(lblTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(lblSubtitle);

        headerPanel.add(titlePanel);

        // ====== LISTA ======
        DefaultListModel<Activity> model = new DefaultListModel<>();
        
        // Filtra apenas atividades pendentes
        java.util.List<Activity> pendingActivities = activities.stream()
            .filter(a -> a.getStatus() == Status.PENDING)
            .collect(java.util.stream.Collectors.toList());

        if (pendingActivities.isEmpty()) {
            JPanel emptyPanel = createEmptyPanel();
            add(headerPanel, BorderLayout.NORTH);
            add(emptyPanel, BorderLayout.CENTER);
            setVisible(true);
            return;
        }

        pendingActivities.forEach(model::addElement);

        JList<Activity> list = new JList<>(model);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        list.setBackground(new Color(245, 245, 250));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // ====== BOTÕES ======
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(new Color(245, 245, 250));

        JButton btnDelete = new JButton("Excluir Selecionada");
        styleDeleteButton(btnDelete);

        JButton btnCancel = new JButton("Cancelar");
        styleSecondaryButton(btnCancel);

        buttonPanel.add(btnDelete);
        buttonPanel.add(btnCancel);

        // ====== AÇÕES ======
        btnDelete.addActionListener(e -> {
            Activity selected = list.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma atividade para excluir.",
                    "Nenhuma Seleção",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir esta atividade?\n\n" +
                "Atividade: " + selected.getName() + "\n" +
                "Esta ação não pode ser desfeita.",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean success = controller.deleteActivity(selected);
                    if (success) {
                        model.removeElement(selected);
                        JOptionPane.showMessageDialog(
                            this,
                            "Atividade excluída com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        
                        if (model.isEmpty()) {
                            dispose();
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                            this,
                            "Não foi possível excluir a atividade.\nApenas atividades PENDENTES podem ser excluídas.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        btnCancel.addActionListener(e -> dispose());

        // Adiciona à janela
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createEmptyPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 250));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel lblEmpty = new JLabel("Nenhuma atividade pode ser excluída");
        lblEmpty.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblEmpty.setForeground(new Color(120, 120, 120));
        lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubEmpty = new JLabel("Você não possui atividades PENDENTES para excluir");
        lblSubEmpty.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubEmpty.setForeground(new Color(150, 150, 150));
        lblSubEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(lblEmpty);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(lblSubEmpty);

        panel.add(contentPanel);
        return panel;
    }

    private void styleDeleteButton(JButton button) {
        button.setPreferredSize(new Dimension(180, 45));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(231, 76, 60));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(231, 76, 60));
            }
        });
    }

    private void styleSecondaryButton(JButton button) {
        button.setPreferredSize(new Dimension(120, 45));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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