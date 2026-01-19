package com.dsa.browsersession.dao;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Repository
public class EvictionLogDao {
    private final DataSource ds;

    public EvictionLogDao(DataSource ds) {
        this.ds = ds;
    }

    public void insert(int sessionId, int tabId, String reason) {
        String sql = "INSERT INTO eviction_log(session_id, tab_id, reason) VALUES (?, ?, ?)";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            ps.setInt(2, tabId);
            ps.setString(3, reason);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("DB error insert eviction_log", e);
        }
    }
    public com.dsa.browsersession.dsa.array.DynamicArray<String> listBySession(int sessionId) {
        String sql = "SELECT eviction_id, tab_id, reason, evicted_at FROM eviction_log WHERE session_id = ? ORDER BY eviction_id DESC";
        com.dsa.browsersession.dsa.array.DynamicArray<String> out = new com.dsa.browsersession.dsa.array.DynamicArray<>(16);

        try (java.sql.Connection c = ds.getConnection();
             java.sql.PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, sessionId);

            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int eid = rs.getInt("eviction_id");
                    int tabId = rs.getInt("tab_id");
                    String reason = rs.getString("reason");
                    java.sql.Timestamp t = rs.getTimestamp("evicted_at");

                    out.add("evictionId=" + eid + ", tabId=" + tabId + ", reason=" + reason + ", time=" + t);
                }
            }
            return out;

        } catch (Exception e) {
            throw new RuntimeException("DB error listBySession eviction_log", e);
        }
    }

}
