package com.newsletter.model;

import java.sql.Date;

public class Article {
    private int idNumber;
    private String title;
    private String content;
    private Integer categoryId;
    private String categoryName;  // convenience field for display in JTable
    private Integer authorId;
    private String authorName;    // convenience field for display in JTable
    private Integer issueId;
    private String issueTitle;    // convenience field for display in JTable
    private String status;        // DRAFT / SUBMITTED / PUBLISHED
    private Date createdDate;

    public Article() {}

    public int getId() { return idNumber; }
    public void setId(int id) { this.idNumber = idNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Integer getAuthorId() { return authorId; }
    public void setAuthorId(Integer authorId) { this.authorId = authorId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public Integer getIssueId() { return issueId; }
    public void setIssueId(Integer issueId) { this.issueId = issueId; }

    public String getIssueTitle() { return issueTitle; }
    public void setIssueTitle(String issueTitle) { this.issueTitle = issueTitle; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
}
