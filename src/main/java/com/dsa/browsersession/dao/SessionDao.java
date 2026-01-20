package com.dsa.browsersession.dao;

import org.springframework.stereotype.Repository;
import com.dsa.browsersession.domain.SessionSummary;
import com.dsa.browsersession.dsa.array.DynamicArray;
import java.sql.Timestamp;
import java.sql.Types;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class SessionDao {

    private final DataSource ds;

    public SessionDao(DataSource ds) {
        this.ds = ds;
    }

    public int createSession(int maxCapacity, long nowMillis, long expiresAtMillis) {
        String sql = "INSERT INTO sessions(max_capacity, last_active_at, status, expires_at) VALUES (?, ?, 'ACTIVE', ?)";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, maxCapacity);
            ps.setTimestamp(2, new Timestamp(nowMillis));
            ps.setTimestamp(3, new Timestamp(expiresAtMillis));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            throw new RuntimeException("Failed to get generated session_id");

        } catch (Exception e) {
            throw new RuntimeException("DB error createSession", e);
        }
    }

    public int getMaxCapacity(int sessionId) {
        String sql = "SELECT max_capacity FROM sessions WHERE session_id = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
            throw new RuntimeException("Session not found: " + sessionId);
        } catch (Exception e) {
            throw new RuntimeException("DB error getMaxCapacity", e);
        }
    }

    public void updateLastActiveAndExpiry(int sessionId, long lastActiveMillis, long expiresAtMillis) {
        String sql = "UPDATE sessions SET last_active_at = ?, expires_at = ? WHERE session_id = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(lastActiveMillis));
            ps.setTimestamp(2, new Timestamp(expiresAtMillis));
            ps.setInt(3, sessionId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("DB error updateLastActiveAndExpiry", e);
        }
    }

    public void setStatus(int sessionId, String status) {
        String sql = "UPDATE sessions SET status = ? WHERE session_id = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, sessionId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("DB error setStatus", e);
        }
    }
    public DynamicArray<SessionSummary> listSessions() {
        // assumes you have expires_at column (you do, because your code already uses it)
        String sql = "SELECT session_id, status, max_capacity, last_active_at, expires_at " +
                "FROM sessions ORDER BY session_id DESC";

        DynamicArray<SessionSummary> out = new DynamicArray<>(16);

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int sid = rs.getInt("session_id");
                String status = rs.getString("status");
                int cap = rs.getInt("max_capacity");

                Timestamp last = rs.getTimestamp("last_active_at");
                long lastMillis = (last == null) ? 0L : last.getTime();

                Timestamp exp = rs.getTimestamp("expires_at");
                long expMillis = (exp == null) ? -1L : exp.getTime();

                out.add(new SessionSummary(sid, status, cap, lastMillis, expMillis));
            }
            return out;

        } catch (Exception e) {
            throw new RuntimeException("DB error listSessions", e);
        }
    }

}
