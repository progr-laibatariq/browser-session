package com.dsa.browsersession.dao;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Repository
public class ActivityLogDao {
    private final DataSource ds;

    public ActivityLogDao(DataSource ds) {
        this.ds = ds;
    }

    public void insert(int sessionId, String operation, String details) {
        String sql = "INSERT INTO activity_log(session_id, operation, details) VALUES (?, ?, ?)";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            ps.setString(2, operation);
            ps.setString(3, details);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("DB error insert activity_log", e);
        }
    }
}
