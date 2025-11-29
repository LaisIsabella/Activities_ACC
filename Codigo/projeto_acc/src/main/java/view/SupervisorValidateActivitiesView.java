package view;

import controller.ActivityController;
import model.Activity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import model.Status;

public class SupervisorValidateActivitiesView extends JFrame {

    public SupervisorValidateActivitiesView(ActivityController controller) {
        setTitle("Validar Atividades");
        setSize(800, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setPreferredSize(new Dimension(800, 100));
        headerPanel.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(46, 204, 113));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("Validação de Atividades");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Verifique os documentos e valide ou negue as atividades");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(230, 250, 240));
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(lblTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(lblSubtitle);

        headerPanel.add(titlePanel, BorderLayout.CENTER);

        // ====== LISTA DE ATIVIDADES ======
        DefaultListModel<Activity> model = new DefaultListModel<>();
        JList<Activity> list = new JList<>(model);
        list.setCellRenderer(new ActivityCellRenderer());
        list.setBackground(new Color(245, 245, 250));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Carrega apenas atividades pendentes e não verificadas
        List<Activity> activities = controller.searchFilteredActivities(null, Status.PENDING, null, false);
        
        if (activities.isEmpty()) {
            JPanel emptyPanel = createEmptyStatePanel();
            add(headerPanel, BorderLayout.NORTH);
            add(emptyPanel, BorderLayout.CENTER);
            setVisible(true);
            return;
        }

        for (Activity a : activities) {
            model.addElement(a);
        }

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Duplo clique para abrir detalhes
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    Activity selected = list.getSelectedValue();
                    if (selected != null) {
                        new SupervisorActivityDetailsView(selected, controller, model);
                    }
                }
            }
        });

        // ====== PAINEL DE INSTRUÇÃO ======
        JPanel instructionPanel = new JPanel();
        instructionPanel.setBackground(new Color(245, 245, 250));
        instructionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        JLabel lblInstruction = new JLabel("Clique duas vezes em uma atividade para validar ou negar");
        lblInstruction.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInstruction.setForeground(new Color(120, 120, 120));
        instructionPanel.add(lblInstruction);

        // Adiciona à janela
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(instructionPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createEmptyStatePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 250));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel lblEmpty = new JLabel("Nenhuma atividade pendente");
        lblEmpty.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblEmpty.setForeground(new Color(120, 120, 120));
        lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubEmpty = new JLabel("Todas as atividades foram validadas");
        lblSubEmpty.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubEmpty.setForeground(new Color(150, 150, 150));
        lblSubEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(lblEmpty);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(lblSubEmpty);

        panel.add(contentPanel);
        return panel;
    }

    // Renderer customizado
    class ActivityCellRenderer extends JPanel implements ListCellRenderer<Activity> {
        private JLabel lblStudent, lblActivity, lblType, lblHours, lblDate;

        public ActivityCellRenderer() {
            setLayout(new BorderLayout(15, 5));
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

            lblStudent = new JLabel();
            lblStudent.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblStudent.setAlignmentX(Component.LEFT_ALIGNMENT);

            lblActivity = new JLabel();
            lblActivity.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblActivity.setForeground(new Color(80, 80, 80));
            lblActivity.setAlignmentX(Component.LEFT_ALIGNMENT);

            lblType = new JLabel();
            lblType.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblType.setForeground(new Color(120, 120, 120));
            lblType.setAlignmentX(Component.LEFT_ALIGNMENT);

            leftPanel.add(lblStudent);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 3)));
            leftPanel.add(lblActivity);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 3)));
            leftPanel.add(lblType);

            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setOpaque(false);

            lblHours = new JLabel();
            lblHours.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblHours.setForeground(new Color(46, 204, 113));
            lblHours.setAlignmentX(Component.RIGHT_ALIGNMENT);

            lblDate = new JLabel();
            lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lblDate.setForeground(new Color(120, 120, 120));
            lblDate.setAlignmentX(Component.RIGHT_ALIGNMENT);

            rightPanel.add(lblHours);
            rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            rightPanel.add(lblDate);

            add(leftPanel, BorderLayout.CENTER);
            add(rightPanel, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Activity> list, Activity activity,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            lblStudent.setText("Aluno: " + (activity.getStudent() != null ? activity.getStudent().getName() : "N/A"));
            lblActivity.setText(activity.getName());
            lblType.setText("Tipo: " + activity.getActivityType().getName());
            lblHours.setText(activity.getHours() + " horas");
            lblDate.setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format(activity.getDate()));

            if (isSelected) {
                setBackground(new Color(240, 255, 245));
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 10, 5, 10),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
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