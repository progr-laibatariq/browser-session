package com.dsa.browsersession.dao;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

@Repository
public class AccessLogDao {

    private final DataSource ds;

    public AccessLogDao(DataSource ds) {
        this.ds = ds;
    }

    public void insertAccess(int tabId, long accessTimeMillis) {
        String sql = "INSERT INTO tab_access_log(tab_id, access_time) VALUES (?, ?)";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, tabId);
            ps.setTimestamp(2, new Timestamp(accessTimeMillis));
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("DB error insertAccess", e);
        }
    }
}
