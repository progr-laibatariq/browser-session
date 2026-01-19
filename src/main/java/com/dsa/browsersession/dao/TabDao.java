package com.dsa.browsersession.dao;
import com.dsa.browsersession.domain.TabRecord;
import com.dsa.browsersession.dsa.array.DynamicArray;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class TabDao {

    private final DataSource ds;

    public TabDao(DataSource ds) {
        this.ds = ds;
    }

    public int insertTab(int sessionId, String url, boolean isActive, long nowMillis, long expiresAtMillis) {
        String sql = "INSERT INTO tabs(session_id, url, is_active, last_access_at, expires_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, sessionId);
            ps.setString(2, url);
            ps.setBoolean(3, isActive);
            ps.setTimestamp(4, new Timestamp(nowMillis));
            ps.setTimestamp(5, new Timestamp(expiresAtMillis));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            throw new RuntimeException("Failed to get generated tab_id");

        } catch (Exception e) {
            throw new RuntimeException("DB error insertTab", e);
        }
    }

    public void deleteTab(int tabId) {
        String sql = "DELETE FROM tabs WHERE tab_id = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, tabId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("DB error deleteTab", e);
        }
    }

    public void updateAccessAndExpiry(int tabId, long lastAccessMillis, long expiresAtMillis) {
        String sql = "UPDATE tabs SET last_access_at = ?, expires_at = ? WHERE tab_id = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(lastAccessMillis));
            ps.setTimestamp(2, new Timestamp(expiresAtMillis));
            ps.setInt(3, tabId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("DB error updateAccessAndExpiry", e);
        }
    }

    public void setActiveTab(int sessionId, int tabId) {
        // make all inactive
        String sql1 = "UPDATE tabs SET is_active = 0 WHERE session_id = ?";
        // make selected active
        String sql2 = "UPDATE tabs SET is_active = 1 WHERE tab_id = ? AND session_id = ?";

        try (Connection c = ds.getConnection()) {
            try (PreparedStatement ps1 = c.prepareStatement(sql1)) {
                ps1.setInt(1, sessionId);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = c.prepareStatement(sql2)) {
                ps2.setInt(1, tabId);
                ps2.setInt(2, sessionId);
                ps2.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("DB error setActiveTab", e);
        }
    }

    public int countTabsInSession(int sessionId) {
        String sql = "SELECT COUNT(*) FROM tabs WHERE session_id = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
            return 0;
        } catch (Exception e) {
            throw new RuntimeException("DB error countTabsInSession", e);
        }
    }
    public TabRecord getTabRecord(int tabId) {
        String sql = "SELECT tab_id, session_id, created_at, url, is_active, last_access_at, expires_at " +
                "FROM tabs WHERE tab_id = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, tabId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                int tid = rs.getInt("tab_id");
                int sid = rs.getInt("session_id");
                long created = toMillis(rs.getTimestamp("created_at"));
                String url = rs.getString("url");
                boolean active = rs.getBoolean("is_active");
                long lastAccess = toMillis(rs.getTimestamp("last_access_at"));

                Timestamp expTs = rs.getTimestamp("expires_at");
                long expires = (expTs == null) ? -1L : toMillis(expTs);

                return new TabRecord(tid, sid, created, url, active, lastAccess, expires);
            }
        } catch (Exception e) {
            throw new RuntimeException("DB error getTabRecord", e);
        }
    }

    public void insertTabWithId(TabRecord r) {
        String sql = "INSERT INTO tabs(tab_id, session_id, created_at, url, is_active, last_access_at, expires_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, r.tabId);
            ps.setInt(2, r.sessionId);
            ps.setTimestamp(3, new Timestamp(r.createdAtMillis));
            ps.setString(4, r.url);
            ps.setBoolean(5, r.isActive);
            ps.setTimestamp(6, new Timestamp(r.lastAccessAtMillis));
            if (r.expiresAtMillis < 0) ps.setNull(7, Types.TIMESTAMP);
            else ps.setTimestamp(7, new Timestamp(r.expiresAtMillis));
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("DB error insertTabWithId", e);
        }
    }

    public int getActiveTabId(int sessionId) {
        String sql = "SELECT tab_id FROM tabs WHERE session_id = ? AND is_active = 1 LIMIT 1";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
                return -1;
            }
        } catch (Exception e) {
            throw new RuntimeException("DB error getActiveTabId", e);
        }
    }

    public int getSessionIdForTab(int tabId) {
        String sql = "SELECT session_id FROM tabs WHERE tab_id = ? LIMIT 1";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, tabId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
                return -1;
            }
        } catch (Exception e) {
            throw new RuntimeException("DB error getSessionIdForTab", e);
        }
    }

    public DynamicArray<TabRecord> listTabsBySession(int sessionId) {
        String sql = "SELECT tab_id, session_id, created_at, url, is_active, last_access_at, expires_at " +
                "FROM tabs WHERE session_id = ?";
        DynamicArray<TabRecord> out = new DynamicArray<>(16);
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int tid = rs.getInt("tab_id");
                    int sid = rs.getInt("session_id");
                    long created = toMillis(rs.getTimestamp("created_at"));
                    String url = rs.getString("url");
                    boolean active = rs.getBoolean("is_active");
                    long lastAccess = toMillis(rs.getTimestamp("last_access_at"));
                    Timestamp expTs = rs.getTimestamp("expires_at");
                    long expires = (expTs == null) ? -1L : toMillis(expTs);

                    out.add(new TabRecord(tid, sid, created, url, active, lastAccess, expires));
                }
            }
            return out;
        } catch (Exception e) {
            throw new RuntimeException("DB error listTabsBySession", e);
        }
    }

    private long toMillis(Timestamp ts) {
        return (ts == null) ? 0L : ts.getTime();
    }
}
