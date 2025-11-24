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

    public CoordinatorSearchActivityView(ActivityController controller,
                                         StudentCatalog studentCatalog) {

        setTitle("Buscar Atividades");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ==================================
        // PAINEL DE FILTROS
        // ==================================
        JPanel panelFilters = new JPanel(new GridLayout(4, 2, 10, 10));

        JTextField txtStudent = new JTextField();
        JComboBox<Status> comboStatus = new JComboBox<>();
            comboStatus.addItem(null); // <- "Todos"
            for (Status s : Status.values()) {
                comboStatus.addItem(s);
            }
            
        JComboBox<ActivityType> comboType = new JComboBox<>();
            comboType.addItem(null); // <- "Todos"
            for (ActivityType t : new repository.ActivityTypeRepository().loadAll()) {
                comboType.addItem(t);
            }


        // Carregar tipos
        for (ActivityType t : new repository.ActivityTypeRepository().loadAll()) {
            comboType.addItem(t);
        }

        panelFilters.add(new JLabel("Aluno (nome/RA/email):"));
        panelFilters.add(txtStudent);

        panelFilters.add(new JLabel("Status:"));
        panelFilters.add(comboStatus);

        panelFilters.add(new JLabel("Tipo de atividade:"));
        panelFilters.add(comboType);

        JButton btnSearch = new JButton("Buscar");
        panelFilters.add(new JLabel());
        panelFilters.add(btnSearch);

        add(panelFilters, BorderLayout.NORTH);
        
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


        // ==================================
        // LISTA
        // ==================================
        DefaultListModel<Activity> model = new DefaultListModel<>();
        JList<Activity> list = new JList<>(model);

        add(new JScrollPane(list), BorderLayout.CENTER);

        // Duplo clique
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    Activity selected = list.getSelectedValue();
                    new CoordinatorActivityDetailsView(selected, controller);
                }
            }
        });

        // ==================================
        // PAGINAÇÃO
        // ==================================
        JButton btnPrev = new JButton("Página Anterior");
        JButton btnNext = new JButton("Próxima Página");

        JPanel paginationPanel = new JPanel(new FlowLayout());
        paginationPanel.add(btnPrev);
        paginationPanel.add(btnNext);

        add(paginationPanel, BorderLayout.SOUTH);

        // ==================================
        // BOTÃO SEARCH
        // ==================================
        btnSearch.addActionListener(e -> {
            String query = txtStudent.getText().trim();
            Object statusObj = comboStatus.getSelectedItem();
            Object typeObj = comboType.getSelectedItem();

            // Aluno: buscar apenas se query não vazia
            Student st = null;
            if (!query.isBlank()) {
                st = studentCatalog.findByQuery(query);
            }

            // Status: aplicar apenas se não for null
            Status status = (statusObj instanceof Status) ? (Status) statusObj : null;

            // Tipo: aplicar apenas se não for null
            ActivityType type = (typeObj instanceof ActivityType) ? (ActivityType) typeObj : null;

            // Chama filtro com os critérios fornecidos
            List<Activity> results = controller.searchFilteredActivities(
                st, status, type, true
            );

            currentResults = results;
            page = 0;

            if (results.isEmpty()) {
                model.clear();
                JOptionPane.showMessageDialog(this, "Nenhuma atividade encontrada.");
            } else {
                updateList(model);
            }
        });
        // ==================================
        // PAGINAÇÃO
        // ==================================
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

        setVisible(true);
    }

    // ==================================
    // ATUALIZA LISTA
    // ==================================
    private void updateList(DefaultListModel<Activity> model) {

        model.clear();

        int start = page * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, currentResults.size());

        if (start >= currentResults.size()) return;

        for (int i = start; i < end; i++) {
            model.addElement(currentResults.get(i));
        }
    }
}
