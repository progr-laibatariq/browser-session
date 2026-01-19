package com.dsa.browsersession.service;

import com.dsa.browsersession.domain.ExpiryEvent;
import com.dsa.browsersession.domain.ExpiryType;
import com.dsa.browsersession.dsa.heap.MinHeap;
import com.dsa.browsersession.dsa.map.CustomHashMap;

public final class ExpirationManager {
    private final MinHeap<ExpiryEvent> heap = new MinHeap<>();
    private final CustomHashMap<Integer, Integer> versionMap = new CustomHashMap<>(64);

    // callbacks to services that actually expire items
    public interface ExpireCallback {
        void expireTab(int tabId);
        void expireSession(int sessionId);
    }

    private final ExpireCallback callback;

    public ExpirationManager(ExpireCallback callback) {
        this.callback = callback;
    }

    private int nextVersion(int idKey) {
        Integer v = versionMap.get(idKey);
        int nv = (v == null) ? 1 : (v + 1);
        versionMap.put(idKey, nv);
        return nv;
    }

    // unique key space so tabId/sessionId versions don't collide:
    // key = type*1_000_000_000 + id (safe)
    private int key(int type, int id) {
        return type * 1_000_000_000 + id;
    }

    public void scheduleTabExpiry(int tabId, long expiresAtMillis) {
        int k = key(ExpiryType.TAB, tabId);
        int version = nextVersion(k);
        heap.push(new ExpiryEvent(expiresAtMillis, ExpiryType.TAB, tabId, version));
    }

    public void scheduleSessionExpiry(int sessionId, long expiresAtMillis) {
        int k = key(ExpiryType.SESSION, sessionId);
        int version = nextVersion(k);
        heap.push(new ExpiryEvent(expiresAtMillis, ExpiryType.SESSION, sessionId, version));
    }

    public int expireDue(long nowMillis) {
        int expiredCount = 0;

        while (true) {
            ExpiryEvent top = heap.peek();
            if (top == null) break;
            if (top.expiresAtMillis > nowMillis) break;

            top = heap.pop();
            int k = key(top.type, top.id);
            Integer curV = versionMap.get(k);

            // stale event -> skip
            if (curV == null || curV.intValue() != top.version) {
                continue;
            }

            // valid event -> expire
            if (top.type == ExpiryType.TAB) {
                callback.expireTab(top.id);
            } else {
                callback.expireSession(top.id);
            }
            expiredCount++;
        }

        return expiredCount;
    }
    public void cancelTabExpiry(int tabId) {
        int k = key(ExpiryType.TAB, tabId);
        nextVersion(k); // invalidate old heap events (lazy deletion)
    }

    public void cancelSessionExpiry(int sessionId) {
        int k = key(ExpiryType.SESSION, sessionId);
        nextVersion(k);
    }

}
