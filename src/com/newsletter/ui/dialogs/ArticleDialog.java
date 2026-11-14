package com.newsletter.ui.dialogs;

import com.newsletter.dao.ArticleDAO;
import com.newsletter.dao.AuthorDAO;
import com.newsletter.dao.CategoryDAO;
import com.newsletter.dao.IssueDAO;
import com.newsletter.model.Article;
import com.newsletter.model.Author;
import com.newsletter.model.Category;
import com.newsletter.model.Issue;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class ArticleDialog extends JDialog {

    private JTextField titleField;
    private JTextArea contentArea;
    private JComboBox<Category> categoryCombo;
    private JComboBox<Author> authorCombo;
    private JComboBox<Issue> issueCombo;
    private JComboBox<String> statusCombo;
    private JTextField dateField;

    private Article existing;
    private ArticleDAO articleDAO = new ArticleDAO();

    public ArticleDialog(Frame owner, Article existing) {
        super(owner, existing == null ? "Add Article" : "Edit Article", true);
        this.existing = existing;
        setSize(560, 560);
        setLocationRelativeTo(owner);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Title:"), gbc);
        titleField = new JTextField(25);
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Category:"), gbc);
        categoryCombo = new JComboBox<>();
        gbc.gridx = 1;
        formPanel.add(categoryCombo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Author:"), gbc);
        authorCombo = new JComboBox<>();
        gbc.gridx = 1;
        formPanel.add(authorCombo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Newsletter Issue:"), gbc);
        issueCombo = new JComboBox<>();
        gbc.gridx = 1;
        formPanel.add(issueCombo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Status:"), gbc);
        statusCombo = new JComboBox<>(new String[]{"DRAFT", "SUBMITTED", "PUBLISHED"});
        gbc.gridx = 1;
        formPanel.add(statusCombo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Created Date (yyyy-MM-dd):"), gbc);
        dateField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(dateField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Content:"), gbc);
        contentArea = new JTextArea(10, 25);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane contentScroll = new JScrollPane(contentArea);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        formPanel.add(contentScroll, gbc);

        JButton saveBtn = new JButton("Save Article");
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        formPanel.add(saveBtn, gbc);

        add(formPanel);

        loadComboData();

        if (existing != null) {
            populateFields();
        } else {
            dateField.setText(java.time.LocalDate.now().toString());
        }

        saveBtn.addActionListener(e -> save());
    }

    private void loadComboData() {
        try {
            categoryCombo.addItem(new Category(0, "-- None --", ""));
            for (Category c : new CategoryDAO().getAll()) {
                categoryCombo.addItem(c);
            }
            authorCombo.addItem(new Author(0, "-- None --", "", ""));
            for (Author a : new AuthorDAO().getAll()) {
                authorCombo.addItem(a);
            }
            issueCombo.addItem(new Issue(0, "-- None --", null, ""));
            for (Issue i : new IssueDAO().getAll()) {
                issueCombo.addItem(i);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading dropdown data: " + ex.getMessage());
        }
    }

    private void populateFields() {
        titleField.setText(existing.getTitle());
        contentArea.setText(existing.getContent());
        statusCombo.setSelectedItem(existing.getStatus());
        dateField.setText(existing.getCreatedDate() != null ? existing.getCreatedDate().toString() : "");
        selectComboById(categoryCombo, existing.getCategoryId());
        selectComboById(authorCombo, existing.getAuthorId());
        selectComboById(issueCombo, existing.getIssueId());
    }

    private void selectComboById(JComboBox<?> combo, Integer id) {
        if (id == null) return;
        for (int i = 0; i < combo.getItemCount(); i++) {
            Object item = combo.getItemAt(i);
            int itemId = -1;
            if (item instanceof Category) itemId = ((Category) item).getId();
            else if (item instanceof Author) itemId = ((Author) item).getId();
            else if (item instanceof Issue) itemId = ((Issue) item).getId();
            if (itemId == id) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void save() {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();
        String status = (String) statusCombo.getSelectedItem();
        String dateStr = dateField.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Article title is required.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Article content cannot be empty.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date createdDate;
        try {
            createdDate = Date.valueOf(dateStr);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Please enter the date in yyyy-MM-dd format.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Category selCat = (Category) categoryCombo.getSelectedItem();
        Author selAuth = (Author) authorCombo.getSelectedItem();
        Issue selIssue = (Issue) issueCombo.getSelectedItem();

        Article article = existing != null ? existing : new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setStatus(status);
        article.setCreatedDate(createdDate);
        article.setCategoryId(selCat != null && selCat.getId() != 0 ? selCat.getId() : null);
        article.setAuthorId(selAuth != null && selAuth.getId() != 0 ? selAuth.getId() : null);
        article.setIssueId(selIssue != null && selIssue.getId() != 0 ? selIssue.getId() : null);

        try {
            if (existing == null) {
                articleDAO.add(article);
            } else {
                articleDAO.update(article);
            }
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
