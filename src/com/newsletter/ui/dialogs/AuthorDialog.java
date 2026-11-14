package com.newsletter.ui.dialogs;

import com.newsletter.dao.AuthorDAO;
import com.newsletter.model.Author;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class AuthorDialog extends JDialog {

    private JTextField nameField;
    private JTextField deptField;
    private JTextField emailField;
    private Author existing;
    private AuthorDAO dao = new AuthorDAO();

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    public AuthorDialog(Frame owner, Author existing) {
        super(owner, existing == null ? "Add Author" : "Edit Author", true);
        this.existing = existing;
        setSize(380, 260);
        setLocationRelativeTo(owner);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        nameField = new JTextField(18);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Department:"), gbc);
        deptField = new JTextField(18);
        gbc.gridx = 1;
        panel.add(deptField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(18);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        if (existing != null) {
            nameField.setText(existing.getName());
            deptField.setText(existing.getDepartment());
            emailField.setText(existing.getEmail());
        }

        JButton saveBtn = new JButton("Save");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(saveBtn, gbc);

        add(panel);

        saveBtn.addActionListener(e -> save());
    }

    private void save() {
        String name = nameField.getText().trim();
        String dept = deptField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Author name is required.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!email.isEmpty() && !EMAIL_PATTERN.matcher(email).matches()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (existing == null) {
                dao.add(new Author(0, name, dept, email));
            } else {
                existing.setName(name);
                existing.setDepartment(dept);
                existing.setEmail(email);
                dao.update(existing);
            }
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
