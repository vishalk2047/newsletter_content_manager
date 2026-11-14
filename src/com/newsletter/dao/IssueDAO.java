package com.newsletter.dao;

import com.newsletter.db.DBConnection;
import com.newsletter.model.Issue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IssueDAO {

    public List<Issue> getAll() throws SQLException {
        List<Issue> list = new ArrayList<>();
        String sql = "SELECT * FROM issues ORDER BY issue_date DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Issue(rs.getInt("id"), rs.getString("title"),
                        rs.getDate("issue_date"), rs.getString("status")));
            }
        }
        return list;
    }

    public void add(Issue i) throws SQLException {
        String sql = "INSERT INTO issues (title, issue_date, status) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, i.getTitle());
            ps.setDate(2, i.getIssueDate());
            ps.setString(3, i.getStatus());
            ps.executeUpdate();
        }
    }

    public void update(Issue i) throws SQLException {
        String sql = "UPDATE issues SET title = ?, issue_date = ?, status = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, i.getTitle());
            ps.setDate(2, i.getIssueDate());
            ps.setString(3, i.getStatus());
            ps.setInt(4, i.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM issues WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
