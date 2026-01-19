package com.dsa.browsersession.domain;

public final class TabRecord {
    public int tabId;
    public int sessionId;
    public long createdAtMillis;
    public String url;
    public boolean isActive;
    public long lastAccessAtMillis;
    public long expiresAtMillis; // -1 means NULL

    public TabRecord() {}

    public TabRecord(int tabId, int sessionId, long createdAtMillis, String url,
                     boolean isActive, long lastAccessAtMillis, long expiresAtMillis) {
        this.tabId = tabId;
        this.sessionId = sessionId;
        this.createdAtMillis = createdAtMillis;
        this.url = url;
        this.isActive = isActive;
        this.lastAccessAtMillis = lastAccessAtMillis;
        this.expiresAtMillis = expiresAtMillis;
    }
}
