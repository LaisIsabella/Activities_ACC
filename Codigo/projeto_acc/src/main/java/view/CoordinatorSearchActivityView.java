package view;

import controller.ActivityController;
import catalog.StudentCatalog;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorSearchActivityView extends JFrame {

    private int page = 0;
    private final int PAGE_SIZE = 10;
    private List<Activity> currentResults = new ArrayList<>();

    public CoordinatorSearchActivityView(ActivityController controller, StudentCatalog studentCatalog) {

        setTitle("Buscar e Avaliar Atividades");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setPreferredSize(new Dimension(900, 100));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(52, 152, 219));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("Buscar Atividades");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Filtre e avalie as atividades submetidas pelos alunos");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(230, 240, 255));
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(lblTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(lblSubtitle);

        headerPanel.add(titlePanel);

        // ====== PAINEL DE FILTROS ======
        JPanel filtersPanel = new JPanel();
        filtersPanel.setBackground(Color.WHITE);
        filtersPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20),
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1)
        ));
        filtersPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        JTextField txtStudent = createTextField();
        txtStudent.setToolTipText("Digite nome, RA ou email do aluno");

        JComboBox<Status> comboStatus = new JComboBox<>();
        comboStatus.addItem(null);
        for (Status s : Status.values()) {
            comboStatus.addItem(s);
        }
        styleComboBox(comboStatus);

        JComboBox<ActivityType> comboType = new JComboBox<>();
        comboType.addItem(null);
        for (ActivityType t : new repository.ActivityTypeRepository().loadAll()) {
            comboType.addItem(t);
        }
        styleComboBox(comboType);

        comboStatus.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                if (value == null) value = "Todos";
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        comboType.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                if (value == null) value = "Todos";
                else value = ((ActivityType) value).getName();
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        // Linha 1
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        filtersPanel.add(createFilterLabel("Aluno:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        filtersPanel.add(txtStudent, gbc);

        // Linha 2
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2;
        filtersPanel.add(createFilterLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4;
        filtersPanel.add(comboStatus, gbc);

        gbc.gridx = 2; gbc.weightx = 0.2;
        filtersPanel.add(createFilterLabel("Tipo:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4;
        filtersPanel.add(comboType, gbc);

        // Botão buscar
        JButton btnSearch = new JButton("Buscar");
        stylePrimaryButton(btnSearch);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        gbc.insets = new Insets(15, 10, 8, 10);
        filtersPanel.add(btnSearch, gbc);

        // ====== LISTA DE ATIVIDADES ======
        DefaultListModel<Activity> model = new DefaultListModel<>();
        JList<Activity> list = new JList<>(model);
        list.setCellRenderer(new ActivityCellRenderer());
        list.setBackground(new Color(245, 245, 250));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Duplo clique para abrir detalhes
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    Activity selected = list.getSelectedValue();
                    if (selected != null) {
                        new CoordinatorActivityDetailsView(selected, controller);
                    }
                }
            }
        });

        // ====== PAGINAÇÃO ======
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        paginationPanel.setBackground(new Color(245, 245, 250));

        JButton btnPrev = new JButton("Anterior");
        styleSecondaryButton(btnPrev);
        
        JButton btnNext = new JButton("Próxima");
        styleSecondaryButton(btnNext);

        paginationPanel.add(btnPrev);
        paginationPanel.add(btnNext);

        // ====== AÇÕES ======
        btnSearch.addActionListener(e -> {
            String query = txtStudent.getText().trim();
            Object statusObj = comboStatus.getSelectedItem();
            Object typeObj = comboType.getSelectedItem();

            Student st = null;
            if (!query.isBlank()) {
                st = studentCatalog.findByQuery(query);
            }

            Status status = (statusObj instanceof Status) ? (Status) statusObj : null;
            ActivityType type = (typeObj instanceof ActivityType) ? (ActivityType) typeObj : null;

            List<Activity> results = controller.searchFilteredActivities(st, status, type, true);

            currentResults = results;
            page = 0;

            if (results.isEmpty()) {
                model.clear();
                JOptionPane.showMessageDialog(this, "Nenhuma atividade encontrada.", "Resultado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                updateList(model);
            }
        });

        btnNext.addActionListener(e -> {
            if ((page + 1) * PAGE_SIZE < currentResults.size()) {
                page++;
                updateList(model);
            }
        });

        btnPrev.addActionListener(e -> {
            if (page > 0) {
                page--;
                updateList(model);
            }
        });

        // ====== INSTRUÇÃO ======
        JPanel instructionPanel = new JPanel();
        instructionPanel.setBackground(new Color(245, 245, 250));
        JLabel lblInstruction = new JLabel("Clique duas vezes em uma atividade para avaliar");
        lblInstruction.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInstruction.setForeground(new Color(120, 120, 120));
        instructionPanel.add(lblInstruction);

        // ====== CONTAINER PRINCIPAL ======
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        mainPanel.add(filtersPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(245, 245, 250));
        bottomPanel.add(instructionPanel, BorderLayout.NORTH);
        bottomPanel.add(paginationPanel, BorderLayout.CENTER);

        // Adiciona à janela
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateList(DefaultListModel<Activity> model) {
        model.clear();
        int start = page * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, currentResults.size());

        if (start >= currentResults.size()) return;

        for (int i = start; i < end; i++) {
            model.addElement(currentResults.get(i));
        }
    }

    private JLabel createFilterLabel(String text) {
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
        comboBox.setPreferredSize(new Dimension(200, 35));
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
    }

    private void stylePrimaryButton(JButton button) {
        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
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
        button.setPreferredSize(new Dimension(120, 35));
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

    // Renderer customizado
    class ActivityCellRenderer extends JPanel implements ListCellRenderer<Activity> {
        private JLabel lblStudent, lblActivity, lblType, lblHours, lblStatus;

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
            lblStudent.setText("Aluno: " + (activity.getStudent() != null ? activity.getStudent().getName() : "N/A"));
            lblActivity.setText(activity.getName());
            lblType.setText("Tipo: " + activity.getActivityType().getName());
            lblHours.setText(activity.getHours() + " horas");

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
                setBackground(new Color(235, 245, 255));
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 10, 5, 10),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
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