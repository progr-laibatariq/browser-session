package com.dsa.browsersession.domain;

public final class ActivityEntry {
    public int logId;
    public int sessionId;
    public String operation;
    public String details;
    public long createdAtMillis;

    public ActivityEntry() {}

    public ActivityEntry(int logId, int sessionId, String operation, String details, long createdAtMillis) {
        this.logId = logId;
        this.sessionId = sessionId;
        this.operation = operation;
        this.details = details;
        this.createdAtMillis = createdAtMillis;
    }
}
