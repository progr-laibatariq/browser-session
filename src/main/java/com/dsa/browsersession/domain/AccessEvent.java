package com.dsa.browsersession.domain;

public final class AccessEvent {
    public long timeMillis;
    public int tabId;

    public AccessEvent(long timeMillis, int tabId) {
        this.timeMillis = timeMillis;
        this.tabId = tabId;
    }
}
