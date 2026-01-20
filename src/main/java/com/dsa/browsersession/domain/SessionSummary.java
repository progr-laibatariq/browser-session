package com.dsa.browsersession.domain;

public final class SessionSummary {
    public int sessionId;
    public String status;
    public int maxCapacity;
    public long lastActiveAtMillis;
    public long expiresAtMillis; // -1 if NULL

    public SessionSummary() {}

    public SessionSummary(int sessionId, String status, int maxCapacity, long lastActiveAtMillis, long expiresAtMillis) {
        this.sessionId = sessionId;
        this.status = status;
        this.maxCapacity = maxCapacity;
        this.lastActiveAtMillis = lastActiveAtMillis;
        this.expiresAtMillis = expiresAtMillis;
    }
}
