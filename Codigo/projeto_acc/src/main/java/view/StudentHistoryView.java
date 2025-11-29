package view;

import controller.ActivityController;
import model.Activity;
import model.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import model.Status;

public class StudentHistoryView extends JFrame {

    private DefaultListModel<Activity> model;
    private ActivityController controller;
    private Student student;

    public StudentHistoryView(Student student, ActivityController controller) {
        this.student = student;
        this.controller = controller;
        
        setTitle("Histórico de Atividades");
        setSize(800, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setPreferredSize(new Dimension(800, 100));
        headerPanel.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(155, 89, 182));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("Histórico de Atividades");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Visualize e gerencie suas atividades submetidas");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(230, 230, 250));
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(lblTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(lblSubtitle);

        headerPanel.add(titlePanel, BorderLayout.CENTER);

        // ====== LISTA DE ATIVIDADES ======
        model = new DefaultListModel<>();
        JList<Activity> list = new JList<>(model);
        list.setCellRenderer(new ActivityListRenderer());
        list.setBackground(new Color(245, 245, 250));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Carregar atividades
        loadActivities();

        if (model.isEmpty()) {
            JPanel emptyPanel = createEmptyStatePanel();
            add(headerPanel, BorderLayout.NORTH);
            add(emptyPanel, BorderLayout.CENTER);
            setVisible(true);
            return;
        }

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Duplo clique para ver detalhes
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    Activity selected = list.getSelectedValue();
                    if (selected != null) {
                        new StudentActivityDetailsView(selected);
                    }
                }
            }
        });

        // ====== PAINEL DE BOTÕES ======
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(245, 245, 250));

        JButton btnDelete = new JButton("Excluir Atividade");
        styleDeleteButton(btnDelete);

        JButton btnDetails = new JButton("Ver Detalhes");
        stylePrimaryButton(btnDetails);

        JButton btnClose = new JButton("Fechar");
        styleSecondaryButton(btnClose);

        // ====== AÇÕES DOS BOTÕES ======
        
        // Excluir
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

            // ✅ Verificar se pode excluir
            if (!controller.canDeleteActivity(selected)) {
                String message = "Esta atividade não pode ser excluída.\n\n";
                
                if (selected.getStatus() != Status.PENDING) {
                    message += "Motivo: A atividade já foi avaliada.\n" +
                              "Status: " + getStatusText(selected.getStatus());
                } else if (selected.getIsVerified()) {
                    message += "Motivo: A atividade já foi validada pelo supervisor\n" +
                              "e está em processo de avaliação.";
                }
                
                JOptionPane.showMessageDialog(
                    this,
                    message,
                    "Não Permitido",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // ✅ Confirmação
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir esta atividade?\n\n" +
                "Atividade: " + selected.getName() + "\n" +
                "Tipo: " + selected.getActivityType().getName() + "\n\n" +
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
                        
                        // Se ficou vazio, fechar a janela
                        if (model.isEmpty()) {
                            dispose();
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                            this,
                            "Não foi possível excluir a atividade.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                    );
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Erro inesperado ao excluir atividade.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                    );
                    ex.printStackTrace();
                }
            }
        });

        // Ver detalhes
        btnDetails.addActionListener(e -> {
            Activity selected = list.getSelectedValue();
            if (selected != null) {
                new StudentActivityDetailsView(selected);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma atividade para ver os detalhes.",
                    "Nenhuma Seleção",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });

        // Fechar
        btnClose.addActionListener(e -> dispose());

        buttonPanel.add(btnDetails);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClose);

        // ====== PAINEL DE INSTRUÇÃO ======
        JPanel instructionPanel = new JPanel();
        instructionPanel.setBackground(new Color(245, 245, 250));
        instructionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));

        JLabel lblInstruction = new JLabel("Clique duas vezes em uma atividade para ver os detalhes");
        lblInstruction.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInstruction.setForeground(new Color(120, 120, 120));
        instructionPanel.add(lblInstruction);

        // Adiciona à janela
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(245, 245, 250));
        bottomPanel.add(instructionPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadActivities() {
        try {
            List<Activity> atividades = controller.getByStudent(student);

            if (atividades != null && !atividades.isEmpty()) {
                for (Activity a : atividades) {
                    model.addElement(a);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar atividades.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getStatusText(model.Status status) {
        return switch (status) {
            case PENDING -> "PENDENTE";
            case APPROVED -> "APROVADA";
            case DENIED -> "NEGADA";
            case PARTIALLY_DENIED -> "AJUSTES NECESSÁRIOS";
        };
    }

    private JPanel createEmptyStatePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 250));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel lblEmpty = new JLabel("Nenhuma atividade encontrada");
        lblEmpty.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblEmpty.setForeground(new Color(120, 120, 120));
        lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubEmpty = new JLabel("Você ainda não submeteu nenhuma atividade complementar");
        lblSubEmpty.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubEmpty.setForeground(new Color(150, 150, 150));
        lblSubEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(lblEmpty);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(lblSubEmpty);

        panel.add(contentPanel);
        return panel;
    }

    private void stylePrimaryButton(JButton button) {
        button.setPreferredSize(new Dimension(140, 45));
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

    private void styleDeleteButton(JButton button) {
        button.setPreferredSize(new Dimension(160, 45));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
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
        button.setPreferredSize(new Dimension(100, 45));
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

    // Renderer customizado para as atividades
    class ActivityListRenderer extends JPanel implements ListCellRenderer<Activity> {

        private JLabel lblName;
        private JLabel lblType;
        private JLabel lblStatus;
        private JLabel lblHours;
        private JLabel lblCanDelete;

        public ActivityListRenderer() {
            setLayout(new BorderLayout(10, 5));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 10, 5, 10),
                BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                )
            ));
            setBackground(Color.WHITE);

            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            leftPanel.setOpaque(false);

            lblName = new JLabel();
            lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblName.setAlignmentX(Component.LEFT_ALIGNMENT);

            lblType = new JLabel();
            lblType.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblType.setForeground(new Color(100, 100, 100));
            lblType.setAlignmentX(Component.LEFT_ALIGNMENT);

            lblCanDelete = new JLabel();
            lblCanDelete.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            lblCanDelete.setAlignmentX(Component.LEFT_ALIGNMENT);

            leftPanel.add(lblName);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            leftPanel.add(lblType);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 3)));
            leftPanel.add(lblCanDelete);

            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setOpaque(false);

            lblStatus = new JLabel();
            lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblStatus.setAlignmentX(Component.RIGHT_ALIGNMENT);

            lblHours = new JLabel();
            lblHours.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblHours.setForeground(new Color(100, 100, 100));
            lblHours.setAlignmentX(Component.RIGHT_ALIGNMENT);

            rightPanel.add(lblStatus);
            rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            rightPanel.add(lblHours);

            add(leftPanel, BorderLayout.CENTER);
            add(rightPanel, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Activity> list, Activity activity,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            lblName.setText(activity.getName());
            lblType.setText("Tipo: " + activity.getActivityType().getName());
            lblHours.setText(activity.getHours() + " horas");

            // Indicador se pode excluir
            if (controller.canDeleteActivity(activity)) {
                lblCanDelete.setText("✓ Pode ser excluída");
                lblCanDelete.setForeground(new Color(46, 204, 113));
            } else {
                lblCanDelete.setText("✗ Não pode ser excluída");
                lblCanDelete.setForeground(new Color(231, 76, 60));
            }

            String statusText = "";
            Color statusColor = Color.BLACK;

            switch (activity.getStatus()) {
                case PENDING:
                    statusText = "PENDENTE";
                    statusColor = new Color(243, 156, 18);
                    break;
                case APPROVED:
                    statusText = "APROVADA";
                    statusColor = new Color(46, 204, 113);
                    break;
                case DENIED:
                    statusText = "NEGADA";
                    statusColor = new Color(231, 76, 60);
                    break;
                case PARTIALLY_DENIED:
                    statusText = "AJUSTES NECESSÁRIOS";
                    statusColor = new Color(230, 126, 34);
                    break;
            }

            lblStatus.setText(statusText);
            lblStatus.setForeground(statusColor);

            if (isSelected) {
                setBackground(new Color(240, 240, 255));
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 10, 5, 10),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                    )
                ));
            } else {
                setBackground(Color.WHITE);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 10, 5, 10),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                    )
                ));
            }

            return this;
        }
    }
}