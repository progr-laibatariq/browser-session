package com.dsa.browsersession.dao;

import org.springframework.stereotype.Repository;
import com.dsa.browsersession.domain.EvictionEntry;
import com.dsa.browsersession.dsa.array.DynamicArray;

import java.sql.ResultSet;
import java.sql.Timestamp;

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
    public DynamicArray<EvictionEntry> listBySession(int sessionId) {
        String sql = "SELECT eviction_id, session_id, tab_id, reason, evicted_at " +
                "FROM eviction_log WHERE session_id = ? ORDER BY eviction_id DESC";

        DynamicArray<EvictionEntry> out = new DynamicArray<>(16);

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, sessionId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("eviction_id");
                    int sid = rs.getInt("session_id");
                    int tid = rs.getInt("tab_id");
                    String reason = rs.getString("reason");
                    Timestamp t = rs.getTimestamp("evicted_at");
                    long millis = (t == null) ? 0L : t.getTime();

                    out.add(new EvictionEntry(id, sid, tid, reason, millis));
                }
            }
            return out;

        } catch (Exception e) {
            throw new RuntimeException("DB error listBySession eviction_log", e);
        }
    }
}
