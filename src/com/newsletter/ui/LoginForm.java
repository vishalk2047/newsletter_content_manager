package com.newsletter.ui;

import com.newsletter.dao.UserDAO;
import com.newsletter.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginForm() {
        setTitle("Departmental Newsletter Content Manager - Login");
        setSize(420, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("Newsletter Content Manager", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(heading, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Username:"), gbc);
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        ModernButton loginBtn = new ModernButton("Login", ModernButton.Type.PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(loginBtn, gbc);

        ModernButton registerBtn = new ModernButton("New user? Register here", ModernButton.Type.LINK);
        registerBtn.setForeground(new Color(0x2C3E50));
        gbc.gridy = 4;
        panel.add(registerBtn, gbc);

        // GridBagLayout centers the form when the window is maximized
        setLayout(new GridBagLayout());
        add(panel);

        loginBtn.addActionListener(this::handleLogin);
        passwordField.addActionListener(this::handleLogin);
        registerBtn.addActionListener(e -> new RegisterForm().setVisible(true));
        loginBtn.setBackground(Color.blue);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            User user = new UserDAO().login(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Welcome, " + user.getFullName() + "!");
                new DashboardForm(user).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}