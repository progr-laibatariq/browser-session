package com.dsa.browsersession.domain;

public final class CountEntry implements Comparable<CountEntry> {
    public int tabId;
    public int count;

    public CountEntry(int tabId, int count) {
        this.tabId = tabId;
        this.count = count;
    }

    @Override
    public int compareTo(CountEntry other) {
        if (this.count != other.count) return this.count - other.count;
        return this.tabId - other.tabId;
    }
}
