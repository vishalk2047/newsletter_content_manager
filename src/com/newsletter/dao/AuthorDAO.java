package com.newsletter.dao;

import com.newsletter.db.DBConnection;
import com.newsletter.model.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {

    public List<Author> getAll() throws SQLException {
        List<Author> list = new ArrayList<>();
        String sql = "SELECT * FROM authors ORDER BY name";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Author(rs.getInt("id"), rs.getString("name"),
                        rs.getString("department"), rs.getString("email")));
            }
        }
        return list;
    }

    public void add(Author a) throws SQLException {
        String sql = "INSERT INTO authors (name, department, email) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getName());
            ps.setString(2, a.getDepartment());
            ps.setString(3, a.getEmail());
            ps.executeUpdate();
        }
    }

    public void update(Author a) throws SQLException {
        String sql = "UPDATE authors SET name = ?, department = ?, email = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getName());
            ps.setString(2, a.getDepartment());
            ps.setString(3, a.getEmail());
            ps.setInt(4, a.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM authors WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
