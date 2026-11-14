package com.newsletter.ui.panels;

import com.newsletter.dao.IssueDAO;
import com.newsletter.model.Issue;
import com.newsletter.ui.dialogs.IssueDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class IssuePanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private IssueDAO dao = new IssueDAO();

    public IssuePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel("Manage Newsletter Issues");
        heading.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(heading, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Title", "Issue Date", "Status"}, 0) {
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
            IssueDialog dialog = new IssueDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
            dialog.setVisible(true);
            loadData();
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an issue to edit.");
                return;
            }
            Issue i = new Issue((int) tableModel.getValueAt(row, 0),
                    (String) tableModel.getValueAt(row, 1),
                    Date.valueOf(tableModel.getValueAt(row, 2).toString()),
                    (String) tableModel.getValueAt(row, 3));
            IssueDialog dialog = new IssueDialog((Frame) SwingUtilities.getWindowAncestor(this), i);
            dialog.setVisible(true);
            loadData();
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an issue to delete.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this issue?",
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
            List<Issue> issues = dao.getAll();
            for (Issue i : issues) {
                tableModel.addRow(new Object[]{i.getId(), i.getTitle(), i.getIssueDate(), i.getStatus()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading issues: " + ex.getMessage());
        }
    }
}
