package com.newsletter.ui.dialogs;

import com.newsletter.dao.CategoryDAO;
import com.newsletter.model.Category;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class CategoryDialog extends JDialog {

    private JTextField nameField;
    private JTextField descField;
    private Category existing;
    private CategoryDAO dao = new CategoryDAO();

    public CategoryDialog(Frame owner, Category existing) {
        super(owner, existing == null ? "Add Category" : "Edit Category", true);
        this.existing = existing;
        setSize(380, 220);
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
        panel.add(new JLabel("Description:"), gbc);
        descField = new JTextField(18);
        gbc.gridx = 1;
        panel.add(descField, gbc);

        if (existing != null) {
            nameField.setText(existing.getName());
            descField.setText(existing.getDescription());
        }

        JButton saveBtn = new JButton("Save");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(saveBtn, gbc);

        add(panel);

        saveBtn.addActionListener(e -> save());
    }

    private void save() {
        String name = nameField.getText().trim();
        String desc = descField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category name is required.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (existing == null) {
                dao.add(new Category(0, name, desc));
            } else {
                existing.setName(name);
                existing.setDescription(desc);
                dao.update(existing);
            }
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
