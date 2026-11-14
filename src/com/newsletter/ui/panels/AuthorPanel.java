package com.newsletter.ui.panels;

import com.newsletter.dao.AuthorDAO;
import com.newsletter.model.Author;
import com.newsletter.ui.dialogs.AuthorDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AuthorPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private AuthorDAO dao = new AuthorDAO();

    public AuthorPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel("Manage Authors / Contributors");
        heading.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(heading, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Department", "Email"}, 0) {
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
            AuthorDialog dialog = new AuthorDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
            dialog.setVisible(true);
            loadData();
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an author to edit.");
                return;
            }
            Author a = new Author((int) tableModel.getValueAt(row, 0),
                    (String) tableModel.getValueAt(row, 1),
                    (String) tableModel.getValueAt(row, 2),
                    (String) tableModel.getValueAt(row, 3));
            AuthorDialog dialog = new AuthorDialog((Frame) SwingUtilities.getWindowAncestor(this), a);
            dialog.setVisible(true);
            loadData();
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an author to delete.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this author?",
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
            List<Author> authors = dao.getAll();
            for (Author a : authors) {
                tableModel.addRow(new Object[]{a.getId(), a.getName(), a.getDepartment(), a.getEmail()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading authors: " + ex.getMessage());
        }
    }
}
