package com.newsletter.ui;

import com.newsletter.dao.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RegisterForm extends JFrame {

    private JTextField fullNameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> roleCombo;

    public RegisterForm() {
        setTitle("Register New User");
        setSize(430, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JLabel heading = new JLabel("Create Account", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(heading, gbc);
        gbc.gridwidth = 1;

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Full Name:"), gbc);
        fullNameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(fullNameField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Username:"), gbc);
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Confirm Password:"), gbc);
        confirmPasswordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Role:"), gbc);
        roleCombo = new JComboBox<>(new String[]{"EDITOR", "ADMIN"});
        gbc.gridx = 1;
        panel.add(roleCombo, gbc);

        row++;
        JButton registerBtn = new JButton("Register");
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(registerBtn, gbc);

        add(panel);

        registerBtn.addActionListener(e -> handleRegister());
    }

    private void handleRegister() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            UserDAO dao = new UserDAO();
            if (dao.usernameExists(username)) {
                JOptionPane.showMessageDialog(this, "Username already taken. Choose another.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            dao.register(username, password, fullName, role);
            JOptionPane.showMessageDialog(this, "Registration successful! You can now log in.");
            this.dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
