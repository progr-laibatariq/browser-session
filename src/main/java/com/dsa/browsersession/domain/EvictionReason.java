package com.dsa.browsersession.domain;

public final class EvictionReason {
    private EvictionReason() {}

    public static final String LRU_LIMIT_REACHED = "LRU_LIMIT_REACHED";
    public static final String SESSION_EXPIRED = "SESSION_EXPIRED";
    public static final String MANUAL_CLOSE = "MANUAL_CLOSE";
}
