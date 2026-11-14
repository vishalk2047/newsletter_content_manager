package com.newsletter.model;

import java.sql.Date;

public class Issue {
    private int id;
    private String title;
    private Date issueDate;
    private String status; // DRAFT / PUBLISHED

    public Issue() {}

    public Issue(int id, String title, Date issueDate, String status) {
        this.id = id;
        this.title = title;
        this.issueDate = issueDate;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() { return title; }
}
