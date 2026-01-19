package com.dsa.browsersession.command;

import com.dsa.browsersession.dao.*;
import com.dsa.browsersession.domain.*;
import com.dsa.browsersession.service.SessionRuntime;

public final class OpenTabCommand implements Command {

    private final int sessionId;
    private final String url;

    private final SessionDao sessionDao;
    private final TabDao tabDao;
    private final AccessLogDao accessLogDao;
    private final ActivityLogDao activityLogDao;
    private final EvictionLogDao evictionLogDao;

    private final SessionRuntime rt;
    // configs
    private final long tabTtlMillis;
    private final long sessionTtlMillis;
    private final long analyticsWindowMillis;
    // state for undo/redo
    private int openedTabId;           // stable id across undo/redo
    private TabRecord openedRecord;    // used for re-insert with same id
    private com.dsa.browsersession.dsa.array.DynamicArray<TabRecord> evictedRecords;
    private int prevActiveTabId;
    private int[] prevLruOrder;
    private long recordAccessTimeMillis;

    public OpenTabCommand(int sessionId, String url,
                          SessionDao sessionDao, TabDao tabDao, AccessLogDao accessLogDao,
                          ActivityLogDao activityLogDao, EvictionLogDao evictionLogDao,
                          SessionRuntime rt,
                          long tabTtlMillis, long sessionTtlMillis, long analyticsWindowMillis) {
        this.sessionId = sessionId;
        this.url = url;

        this.sessionDao = sessionDao;
        this.tabDao = tabDao;
        this.accessLogDao = accessLogDao;
        this.activityLogDao = activityLogDao;
        this.evictionLogDao = evictionLogDao;

        this.rt = rt;
        this.tabTtlMillis = tabTtlMillis;
        this.sessionTtlMillis = sessionTtlMillis;
        this.analyticsWindowMillis = analyticsWindowMillis;
    }

    public int getOpenedTabId() {
        return openedTabId;
    }

    @Override
    public void execute() {
        long now = System.currentTimeMillis();

        // snapshot for undo
        prevActiveTabId = tabDao.getActiveTabId(sessionId);
        prevLruOrder = rt.lru.snapshotOrder();

        // capacity eviction (LRU)
        int capacity = sessionDao.getMaxCapacity(sessionId);

// Always use DB count (because runtime LRU can be empty after restart)
        int tabCount = tabDao.countTabsInSession(sessionId);

// store evictions for undo
        evictedRecords = new com.dsa.browsersession.dsa.array.DynamicArray<>(2);

// If DB already has too many tabs, evict until tabCount < capacity
        while (tabCount >= capacity) {
            int activeId = tabDao.getActiveTabId(sessionId);

            // Make sure LRU has correct tabs (if not, fallback pick from DB using last_access)
            int candidate = rt.lru.pickEvictionCandidate(activeId);
            if (candidate == -1) break;

            TabRecord rec = tabDao.getTabRecord(candidate);
            if (rec == null) {
                // If candidate doesn't exist in DB, remove from LRU and continue
                rt.lru.remove(candidate);
                continue;
            }

            tabDao.deleteTab(candidate);
            rt.lru.remove(candidate);
            rt.expiration.cancelTabExpiry(candidate);

            evictedRecords.add(rec);

            evictionLogDao.insert(sessionId, candidate, EvictionReason.LRU_LIMIT_REACHED);
            activityLogDao.insert(sessionId, OperationType.TAB_CLOSED, "LRU_EVICT tabId=" + candidate);

            tabCount--; // DB count reduced by 1
        }


        long tabExpires = now + tabTtlMillis;

        if (openedTabId == 0) {
            openedTabId = tabDao.insertTab(sessionId, url, true, now, tabExpires);
            openedRecord = tabDao.getTabRecord(openedTabId);
        } else {
            // redo after undo -> reinsert same tab_id
            if (openedRecord == null) {
                openedRecord = new TabRecord(openedTabId, sessionId, now, url, true, now, tabExpires);
            } else {
                openedRecord.isActive = true;
                openedRecord.lastAccessAtMillis = now;
                openedRecord.expiresAtMillis = tabExpires;
            }
            tabDao.insertTabWithId(openedRecord);
        }

        tabDao.setActiveTab(sessionId, openedTabId);
        rt.lru.addToFront(openedTabId);

        recordAccessTimeMillis = now;
        rt.analytics.recordAccess(openedTabId, now, analyticsWindowMillis);
        accessLogDao.insertAccess(openedTabId, now);

        rt.expiration.scheduleTabExpiry(openedTabId, tabExpires);
        sessionDao.updateLastActiveAndExpiry(sessionId, now, now + sessionTtlMillis);

        activityLogDao.insert(sessionId, OperationType.TAB_OPENED, "url=" + url + " tabId=" + openedTabId);
    }

    @Override
    public void undo() {
        long now = System.currentTimeMillis();

        // remove opened tab
        TabRecord rec = tabDao.getTabRecord(openedTabId);
        if (rec != null) {
            tabDao.deleteTab(openedTabId);
        }
        rt.lru.remove(openedTabId);
        rt.expiration.cancelTabExpiry(openedTabId);
        rt.analytics.undoLastAccess(openedTabId, recordAccessTimeMillis, now, analyticsWindowMillis);

        // restore evicted (if any)
        // restore evicted tabs (if any)
        if (evictedRecords != null) {
            for (int i = 0; i < evictedRecords.size(); i++) {
                TabRecord r = evictedRecords.get(i);
                tabDao.insertTabWithId(r);

                long exp = (r.expiresAtMillis < 0) ? (now + tabTtlMillis) : r.expiresAtMillis;
                rt.expiration.scheduleTabExpiry(r.tabId, exp);
            }
        }

// restore original LRU order
        rt.lru.restoreOrder(prevLruOrder);

        // restore active tab
        if (prevActiveTabId != -1) tabDao.setActiveTab(sessionId, prevActiveTabId);

        activityLogDao.insert(sessionId, OperationType.SESSION_UPDATED, "UNDO OpenTab tabId=" + openedTabId);
    }

    @Override
    public String name() {
        return "OpenTab";
    }
}
