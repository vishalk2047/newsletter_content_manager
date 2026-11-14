package com.newsletter.ui.panels;

import com.newsletter.dao.ArticleDAO;
import com.newsletter.dao.CategoryDAO;
import com.newsletter.model.Article;
import com.newsletter.model.Category;
import com.newsletter.ui.dialogs.ArticleDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ArticlePanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private ArticleDAO articleDAO = new ArticleDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

    private JTextField searchField;
    private JComboBox<Category> categoryFilterCombo;
    private JComboBox<String> statusFilterCombo;

    public ArticlePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel("Manage Newsletter Articles");
        heading.setFont(new Font("SansSerif", Font.BOLD, 18));

        // ---- Search / filter bar ----
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(15);
        categoryFilterCombo = new JComboBox<>();
        categoryFilterCombo.addItem(new Category(0, "All Categories", ""));
        statusFilterCombo = new JComboBox<>(new String[]{"All", "DRAFT", "SUBMITTED", "PUBLISHED"});
        JButton searchBtn = new JButton("Search");
        JButton clearBtn = new JButton("Clear");

        filterPanel.add(new JLabel("Title:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(categoryFilterCombo);
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusFilterCombo);
        filterPanel.add(searchBtn);
        filterPanel.add(clearBtn);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(heading, BorderLayout.NORTH);
        northPanel.add(filterPanel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        // ---- Table ----
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Title", "Category", "Author", "Issue", "Status", "Created Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ---- Buttons ----
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add Article");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            ArticleDialog dialog = new ArticleDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
            dialog.setVisible(true);
            loadData();
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an article to edit.");
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);
            try {
                Article article = findById(id);
                if (article != null) {
                    ArticleDialog dialog = new ArticleDialog((Frame) SwingUtilities.getWindowAncestor(this), article);
                    dialog.setVisible(true);
                    loadData();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an article to delete.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this article?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    articleDAO.delete((int) tableModel.getValueAt(row, 0));
                    loadData();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        refreshBtn.addActionListener(e -> loadData());
        searchBtn.addActionListener(e -> runSearch());
        clearBtn.addActionListener(e -> {
            searchField.setText("");
            categoryFilterCombo.setSelectedIndex(0);
            statusFilterCombo.setSelectedIndex(0);
            loadData();
        });

        loadCategoryFilterOptions();
        loadData();
    }

    private void loadCategoryFilterOptions() {
        try {
            List<Category> categories = categoryDAO.getAll();
            for (Category c : categories) {
                categoryFilterCombo.addItem(c);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + ex.getMessage());
        }
    }

    private void runSearch() {
        try {
            String keyword = searchField.getText();
            Category selectedCategory = (Category) categoryFilterCombo.getSelectedItem();
            Integer categoryId = (selectedCategory != null && selectedCategory.getId() != 0)
                    ? selectedCategory.getId() : null;
            String status = (String) statusFilterCombo.getSelectedItem();

            List<Article> results = articleDAO.search(keyword, categoryId, status);
            populateTable(results);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error searching articles: " + ex.getMessage());
        }
    }

    public void loadData() {
        try {
            populateTable(articleDAO.getAll());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading articles: " + ex.getMessage());
        }
    }

    private void populateTable(List<Article> articles) {
        tableModel.setRowCount(0);
        for (Article a : articles) {
            tableModel.addRow(new Object[]{
                    a.getId(), a.getTitle(),
                    a.getCategoryName() == null ? "-" : a.getCategoryName(),
                    a.getAuthorName() == null ? "-" : a.getAuthorName(),
                    a.getIssueTitle() == null ? "-" : a.getIssueTitle(),
                    a.getStatus(),
                    a.getCreatedDate()
            });
        }
    }

    private Article findById(int id) throws SQLException {
        for (Article a : articleDAO.getAll()) {
            if (a.getId() == id) return a;
        }
        return null;
    }
}
