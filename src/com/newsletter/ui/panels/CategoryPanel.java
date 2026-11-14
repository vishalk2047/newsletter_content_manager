package com.newsletter.ui.panels;

import com.newsletter.dao.CategoryDAO;
import com.newsletter.model.Category;
import com.newsletter.ui.dialogs.CategoryDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CategoryPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private CategoryDAO dao = new CategoryDAO();

    public CategoryPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel("Manage Categories");
        heading.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(heading, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Description"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            CategoryDialog dialog = new CategoryDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
            dialog.setVisible(true);
            loadData();
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a category to edit.");
                return;
            }
            Category c = new Category((int) tableModel.getValueAt(row, 0),
                    (String) tableModel.getValueAt(row, 1),
                    (String) tableModel.getValueAt(row, 2));
            CategoryDialog dialog = new CategoryDialog((Frame) SwingUtilities.getWindowAncestor(this), c);
            dialog.setVisible(true);
            loadData();
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a category to delete.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this category?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    dao.delete((int) tableModel.getValueAt(row, 0));
                    loadData();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        refreshBtn.addActionListener(e -> loadData());

        loadData();
    }

    public void loadData() {
        tableModel.setRowCount(0);
        try {
            List<Category> categories = dao.getAll();
            for (Category c : categories) {
                tableModel.addRow(new Object[]{c.getId(), c.getName(), c.getDescription()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + ex.getMessage());
        }
    }
}
