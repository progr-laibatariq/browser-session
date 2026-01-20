package com.dsa.browsersession.domain;

public final class EvictionEntry {
    public int evictionId;
    public int sessionId;
    public int tabId;
    public String reason;
    public long evictedAtMillis;

    public EvictionEntry() {}

    public EvictionEntry(int evictionId, int sessionId, int tabId, String reason, long evictedAtMillis) {
        this.evictionId = evictionId;
        this.sessionId = sessionId;
        this.tabId = tabId;
        this.reason = reason;
        this.evictedAtMillis = evictedAtMillis;
    }
}
