package com.newsletter;

import com.newsletter.ui.LoginForm;

import javax.swing.*;

/**
 * Departmental Newsletter Content Manager
 * Entry point of the application.
 */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
