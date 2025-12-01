package view;

import controller.ActivityTypeController;
import model.ActivityType;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentHourLimitsView extends JFrame {

    public StudentHourLimitsView() {
        setTitle("Limites de Horas por Tipo");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setPreferredSize(new Dimension(600, 90));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(155, 89, 182));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("Limites de Horas");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Consulte os limites de horas por tipo de atividade");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(230, 230, 250));
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(lblTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(lblSubtitle);

        headerPanel.add(titlePanel);

        // ====== TABELA ======
        try {
            ActivityTypeController controller = new ActivityTypeController();
            List<ActivityType> tipos = controller.listTypes();

            if (tipos.isEmpty()) {
                JPanel emptyPanel = createEmptyPanel();
                add(headerPanel, BorderLayout.NORTH);
                add(emptyPanel, BorderLayout.CENTER);
                setVisible(true);
                return;
            }

            String[] columns = {"Tipo de Atividade", "Limite de Horas"};
            DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (ActivityType tipo : tipos) {
                tableModel.addRow(new Object[]{tipo.getName(), tipo.getLimit() + "h"});
            }

            JTable table = new JTable(tableModel);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            table.setRowHeight(40);
            table.setGridColor(new Color(230, 230, 230));
            table.setSelectionBackground(new Color(230, 230, 250));
            table.setSelectionForeground(new Color(50, 50, 50));
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
            table.getTableHeader().setBackground(new Color(155, 89, 182));
            table.getTableHeader().setForeground(Color.WHITE);
            table.getTableHeader().setPreferredSize(new Dimension(0, 45));

            // Centraliza a coluna de horas
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

            add(scrollPane, BorderLayout.CENTER);

        } catch (Exception e) {
            JPanel errorPanel = createErrorPanel();
            add(headerPanel, BorderLayout.NORTH);
            add(errorPanel, BorderLayout.CENTER);
            setVisible(true);
            return;
        }

        // ====== BOTÃO FECHAR ======
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        buttonPanel.setBackground(new Color(245, 245, 250));

        JButton btnFechar = new JButton("Fechar");
        stylePrimaryButton(btnFechar);
        btnFechar.addActionListener(e -> dispose());

        buttonPanel.add(btnFechar);

        // Adiciona à janela
        add(headerPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createEmptyPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 250));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel lblEmpty = new JLabel("Nenhum limite configurado");
        lblEmpty.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblEmpty.setForeground(new Color(120, 120, 120));
        lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubEmpty = new JLabel("Os limites de horas ainda não foram definidos");
        lblSubEmpty.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubEmpty.setForeground(new Color(150, 150, 150));
        lblSubEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(lblEmpty);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(lblSubEmpty);

        panel.add(contentPanel);
        return panel;
    }

    private JPanel createErrorPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 250));

        JLabel lblError = new JLabel("Erro ao carregar os limites de horas");
        lblError.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblError.setForeground(new Color(231, 76, 60));

        panel.add(lblError);
        return panel;
    }

    private void stylePrimaryButton(JButton button) {
        button.setPreferredSize(new Dimension(120, 40));
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
}