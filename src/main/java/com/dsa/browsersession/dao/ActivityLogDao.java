package com.dsa.browsersession.dao;

import org.springframework.stereotype.Repository;
import com.dsa.browsersession.domain.ActivityEntry;
import com.dsa.browsersession.dsa.array.DynamicArray;

import java.sql.ResultSet;
import java.sql.Timestamp;

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
    public DynamicArray<ActivityEntry> listBySession(int sessionId) {
        String sql = "SELECT log_id, session_id, operation, details, created_at " +
                "FROM activity_log WHERE session_id = ? ORDER BY log_id DESC";

        DynamicArray<ActivityEntry> out = new DynamicArray<>(32);

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, sessionId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("log_id");
                    int sid = rs.getInt("session_id");
                    String op = rs.getString("operation");
                    String details = rs.getString("details");
                    Timestamp t = rs.getTimestamp("created_at");
                    long millis = (t == null) ? 0L : t.getTime();

                    out.add(new ActivityEntry(id, sid, op, details, millis));
                }
            }
            return out;

        } catch (Exception e) {
            throw new RuntimeException("DB error listBySession activity_log", e);
        }
    }

}
