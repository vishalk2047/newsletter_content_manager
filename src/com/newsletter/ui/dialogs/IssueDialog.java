package com.newsletter.ui.dialogs;

import com.newsletter.dao.IssueDAO;
import com.newsletter.model.Issue;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;

public class IssueDialog extends JDialog {

    private JTextField titleField;
    private JTextField dateField; // format yyyy-MM-dd
    private JComboBox<String> statusCombo;
    private Issue existing;
    private IssueDAO dao = new IssueDAO();

    public IssueDialog(Frame owner, Issue existing) {
        super(owner, existing == null ? "Add Issue" : "Edit Issue", true);
        this.existing = existing;
        setSize(380, 260);
        setLocationRelativeTo(owner);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Title:"), gbc);
        titleField = new JTextField(18);
        gbc.gridx = 1;
        panel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Issue Date (yyyy-MM-dd):"), gbc);
        dateField = new JTextField(18);
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Status:"), gbc);
        statusCombo = new JComboBox<>(new String[]{"DRAFT", "PUBLISHED"});
        gbc.gridx = 1;
        panel.add(statusCombo, gbc);

        if (existing != null) {
            titleField.setText(existing.getTitle());
            dateField.setText(existing.getIssueDate() != null ? existing.getIssueDate().toString() : "");
            statusCombo.setSelectedItem(existing.getStatus());
        } else {
            dateField.setText(java.time.LocalDate.now().toString());
        }

        JButton saveBtn = new JButton("Save");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(saveBtn, gbc);

        add(panel);

        saveBtn.addActionListener(e -> save());
    }

    private void save() {
        String title = titleField.getText().trim();
        String dateStr = dateField.getText().trim();
        String status = (String) statusCombo.getSelectedItem();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Issue title is required.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date issueDate;
        try {
            issueDate = Date.valueOf(dateStr);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Please enter the date in yyyy-MM-dd format.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (existing == null) {
                dao.add(new Issue(0, title, issueDate, status));
            } else {
                existing.setTitle(title);
                existing.setIssueDate(issueDate);
                existing.setStatus(status);
                dao.update(existing);
            }
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
