package com.newsletter.ui;

import com.newsletter.model.User;
import com.newsletter.ui.panels.ArticlePanel;
import com.newsletter.ui.panels.AuthorPanel;
import com.newsletter.ui.panels.CategoryPanel;
import com.newsletter.ui.panels.IssuePanel;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardForm extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);
    private final Map<String, ModernButton> navButtons = new LinkedHashMap<>();

    public DashboardForm(User loggedInUser) {
        setTitle("Departmental Newsletter Content Manager - Dashboard");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // ---- Top bar ----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        topBar.setBackground(new Color(45, 62, 80));
        JLabel title = new JLabel("Departmental Newsletter Content Manager");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JLabel userLabel = new JLabel(loggedInUser.getFullName() + " (" + loggedInUser.getRole() + ")");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        topBar.add(title, BorderLayout.WEST);
        topBar.add(userLabel, BorderLayout.EAST);

        // ---- Sidebar ----
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(52, 73, 94));
        sidebar.setPreferredSize(new Dimension(190, 0));

        for (String name : new String[]{"Articles", "Categories", "Authors", "Issues"}) {
            navButtons.put(name, createSidebarButton(name));
        }
        ModernButton logoutBtn = createSidebarButton("Logout");

        sidebar.add(Box.createVerticalStrut(20));
        for (ModernButton b : navButtons.values()) {
            sidebar.add(b);
            sidebar.add(Box.createVerticalStrut(4));
        }
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(logoutBtn);
        sidebar.add(Box.createVerticalStrut(20));

        // ---- Content panels (CRUD screens) ----
        contentPanel.add(new ArticlePanel(), "Articles");
        contentPanel.add(new CategoryPanel(), "Categories");
        contentPanel.add(new AuthorPanel(), "Authors");
        contentPanel.add(new IssuePanel(), "Issues");

        navButtons.forEach((name, btn) -> btn.addActionListener(e -> showPanel(name)));

        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?",
                    "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginForm().setVisible(true);
            }
        });

        setLayout(new BorderLayout());
        add(topBar, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        showPanel("Articles");
    }

    private void showPanel(String name) {
        cardLayout.show(contentPanel, name);
        navButtons.forEach((n, btn) -> btn.setActive(n.equals(name)));
    }

    private ModernButton createSidebarButton(String text) {
        ModernButton btn = new ModernButton(text, ModernButton.Type.SIDEBAR);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(170, 40));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return btn;
    }
}