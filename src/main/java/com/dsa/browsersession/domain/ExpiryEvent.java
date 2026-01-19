package com.dsa.browsersession.domain;

public final class ExpiryEvent implements Comparable<ExpiryEvent> {
    public long expiresAtMillis;
    public int type;      // ExpiryType.TAB or ExpiryType.SESSION
    public int id;        // tabId or sessionId
    public int version;   // for lazy deletion

    public ExpiryEvent(long expiresAtMillis, int type, int id, int version) {
        this.expiresAtMillis = expiresAtMillis;
        this.type = type;
        this.id = id;
        this.version = version;
    }

    @Override
    public int compareTo(ExpiryEvent other) {
        if (this.expiresAtMillis < other.expiresAtMillis) return -1;
        if (this.expiresAtMillis > other.expiresAtMillis) return 1;
        // Tie-breaker to keep heap stable
        if (this.type != other.type) return this.type - other.type;
        return this.id - other.id;
    }
}
