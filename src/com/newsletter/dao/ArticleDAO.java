package com.newsletter.dao;

import com.newsletter.db.DBConnection;
import com.newsletter.model.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleDAO {

    private static final String BASE_SELECT =
            "SELECT a.id, a.title, a.content, a.status, a.created_date, " +
            "       a.category_id, c.name AS category_name, " +
            "       a.author_id, au.name AS author_name, " +
            "       a.issue_id, i.title AS issue_title " +
            "FROM articles a " +
            "LEFT JOIN categories c ON a.category_id = c.id " +
            "LEFT JOIN authors au ON a.author_id = au.id " +
            "LEFT JOIN issues i ON a.issue_id = i.id ";

    public List<Article> getAll() throws SQLException {
        List<Article> list = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY a.id ASC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    /** Search/filter by title keyword, category id, and status. Pass null/0/"" to skip a filter. */
    public List<Article> search(String keyword, Integer categoryId, String status) throws SQLException {
        StringBuilder sql = new StringBuilder(BASE_SELECT + "WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND a.title LIKE ? ");
            params.add("%" + keyword.trim() + "%");
        }
        if (categoryId != null && categoryId > 0) {
            sql.append("AND a.category_id = ? ");
            params.add(categoryId);
        }
        if (status != null && !status.equals("All")) {
            sql.append("AND a.status = ? ");
            params.add(status);
        }
        sql.append("ORDER BY a.created_date DESC, a.id DESC");

        List<Article> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public void add(Article a) throws SQLException {
        String sql = "INSERT INTO articles (title, content, category_id, author_id, issue_id, status, created_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getTitle());
            ps.setString(2, a.getContent());
            setNullableInt(ps, 3, a.getCategoryId());
            setNullableInt(ps, 4, a.getAuthorId());
            setNullableInt(ps, 5, a.getIssueId());
            ps.setString(6, a.getStatus());
            ps.setDate(7, a.getCreatedDate());
            ps.executeUpdate();
        }
    }

    public void update(Article a) throws SQLException {
        String sql = "UPDATE articles SET title=?, content=?, category_id=?, author_id=?, issue_id=?, status=?, created_date=? " +
                     "WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getTitle());
            ps.setString(2, a.getContent());
            setNullableInt(ps, 3, a.getCategoryId());
            setNullableInt(ps, 4, a.getAuthorId());
            setNullableInt(ps, 5, a.getIssueId());
            ps.setString(6, a.getStatus());
            ps.setDate(7, a.getCreatedDate());
            ps.setInt(8, a.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM articles WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private void setNullableInt(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null || value == 0) {
            ps.setNull(index, Types.INTEGER);
        } else {
            ps.setInt(index, value);
        }
    }

    private Article mapRow(ResultSet rs) throws SQLException {
        Article a = new Article();
        a.setId(rs.getInt("id"));
        a.setTitle(rs.getString("title"));
        a.setContent(rs.getString("content"));
        a.setStatus(rs.getString("status"));
        a.setCreatedDate(rs.getDate("created_date"));

        int catId = rs.getInt("category_id");
        a.setCategoryId(rs.wasNull() ? null : catId);
        a.setCategoryName(rs.getString("category_name"));

        int authId = rs.getInt("author_id");
        a.setAuthorId(rs.wasNull() ? null : authId);
        a.setAuthorName(rs.getString("author_name"));

        int issueId = rs.getInt("issue_id");
        a.setIssueId(rs.wasNull() ? null : issueId);
        a.setIssueTitle(rs.getString("issue_title"));

        return a;
    }
}
