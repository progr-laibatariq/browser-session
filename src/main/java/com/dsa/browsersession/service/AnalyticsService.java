package com.dsa.browsersession.service;

import com.dsa.browsersession.domain.AccessEvent;
import com.dsa.browsersession.domain.CountEntry;
import com.dsa.browsersession.dsa.deque.CustomDeque;
import com.dsa.browsersession.dsa.heap.MaxHeap;
import com.dsa.browsersession.dsa.map.CustomHashMap;

public final class AnalyticsService {

    private final CustomDeque<AccessEvent> windowQ = new CustomDeque<>();
    private final CustomHashMap<Integer, Integer> counts = new CustomHashMap<>(64);

    public void recordAccess(int tabId, long nowMillis, long windowMillis) {
        windowQ.pushBack(new AccessEvent(nowMillis, tabId));

        Integer c = counts.get(tabId);
        counts.put(tabId, (c == null) ? 1 : (c + 1));

        evictOld(nowMillis, windowMillis);
    }

    private void evictOld(long nowMillis, long windowMillis) {
        long cutoff = nowMillis - windowMillis;

        while (!windowQ.isEmpty()) {
            AccessEvent e = windowQ.peekFront();
            if (e == null) break;
            if (e.timeMillis >= cutoff) break;

            e = windowQ.popFront();
            Integer c = counts.get(e.tabId);
            if (c == null) continue;

            int nc = c.intValue() - 1;
            if (nc <= 0) counts.remove(e.tabId);
            else counts.put(e.tabId, nc);
        }
    }

    public CountEntry[] topK(int k, long nowMillis, long windowMillis) {
        if (k <= 0) return new CountEntry[0];

        evictOld(nowMillis, windowMillis);

        MaxHeap<CountEntry> heap = new MaxHeap<>(counts.size() + 1);
        counts.forEach(new CustomHashMap.EntryVisitor<Integer, Integer>() {
            @Override
            public void visit(Integer key, Integer value) {
                heap.push(new CountEntry(key.intValue(), value.intValue()));
            }
        });

        int n = heap.size();
        int outSize = (k < n) ? k : n;

        CountEntry[] out = new CountEntry[outSize];
        for (int i = 0; i < outSize; i++) out[i] = heap.pop();
        return out;
    }
    public boolean undoLastAccess(int expectedTabId, long expectedTimeMillis, long nowMillis, long windowMillis) {
        evictOld(nowMillis, windowMillis);

        if (windowQ.isEmpty()) return false;
        AccessEvent last = windowQ.peekBack();
        if (last == null) return false;

        if (last.tabId != expectedTabId) return false;
        if (last.timeMillis != expectedTimeMillis) return false;

        windowQ.popBack();

        Integer c = counts.get(expectedTabId);
        if (c == null) return false;

        int nc = c.intValue() - 1;
        if (nc <= 0) counts.remove(expectedTabId);
        else counts.put(expectedTabId, nc);

        return true;
    }

}
