package com.dsa.browsersession.service;
import com.dsa.browsersession.domain.SessionSummary;
import com.dsa.browsersession.domain.EvictionEntry;
import com.dsa.browsersession.domain.ActivityEntry;
import com.dsa.browsersession.dsa.array.DynamicArray;

import com.dsa.browsersession.command.AccessTabCommand;
import com.dsa.browsersession.command.CloseTabCommand;
import com.dsa.browsersession.command.OpenTabCommand;
import com.dsa.browsersession.dao.*;
import com.dsa.browsersession.domain.CountEntry;
import com.dsa.browsersession.domain.ExpiryType;
import com.dsa.browsersession.domain.OperationType;
import com.dsa.browsersession.domain.TabRecord;
import com.dsa.browsersession.dsa.map.CustomHashMap;
import org.springframework.stereotype.Service;

@Service
public class BrowserEngineService {

    // Configs (you can adjust)
    public static final long TAB_TTL_MILLIS = 10L * 60L * 1000L;        // 10 minutes
    public static final long SESSION_TTL_MILLIS = 30L * 60L * 1000L;    // 30 minutes
    public static final long ANALYTICS_WINDOW_MILLIS = 5L * 60L * 1000L;// 5 minutes

    private final SessionDao sessionDao;
    private final TabDao tabDao;
    private final AccessLogDao accessLogDao;
    private final ActivityLogDao activityLogDao;
    private final EvictionLogDao evictionLogDao;
    private final RuntimeStore runtimeStore;

    public BrowserEngineService(SessionDao sessionDao,
                                TabDao tabDao,
                                AccessLogDao accessLogDao,
                                ActivityLogDao activityLogDao,
                                EvictionLogDao evictionLogDao,
                                RuntimeStore runtimeStore) {
        this.sessionDao = sessionDao;
        this.tabDao = tabDao;
        this.accessLogDao = accessLogDao;
        this.activityLogDao = activityLogDao;
        this.evictionLogDao = evictionLogDao;
        this.runtimeStore = runtimeStore;
    }

    public int createSession(int capacity) {
        long now = System.currentTimeMillis();
        int sessionId = sessionDao.createSession(capacity, now, now + SESSION_TTL_MILLIS);
        ensureRuntime(sessionId);
        activityLogDao.insert(sessionId, OperationType.SESSION_CREATED, "capacity=" + capacity);
        return sessionId;
    }

    public int openTab(int sessionId, String url) {
        SessionRuntime rt = ensureRuntime(sessionId);
        OpenTabCommand cmd = new OpenTabCommand(
                sessionId, url,
                sessionDao, tabDao, accessLogDao, activityLogDao, evictionLogDao,
                rt, TAB_TTL_MILLIS, SESSION_TTL_MILLIS, ANALYTICS_WINDOW_MILLIS
        );
        rt.commandManager.executeCommand(cmd);
        return cmd.getOpenedTabId();
    }

    public void closeTab(int sessionId, int tabId) {
        SessionRuntime rt = ensureRuntime(sessionId);
        CloseTabCommand cmd = new CloseTabCommand(sessionId, tabId, tabDao, activityLogDao, evictionLogDao, rt);
        rt.commandManager.executeCommand(cmd);
    }

    public void accessTab(int sessionId, int tabId) {
        SessionRuntime rt = ensureRuntime(sessionId);
        AccessTabCommand cmd = new AccessTabCommand(
                sessionId, tabId,
                sessionDao, tabDao, accessLogDao, activityLogDao,
                rt, TAB_TTL_MILLIS, SESSION_TTL_MILLIS, ANALYTICS_WINDOW_MILLIS
        );
        rt.commandManager.executeCommand(cmd);
    }

    public boolean undo(int sessionId) {
        SessionRuntime rt = ensureRuntime(sessionId);
        return rt.commandManager.undo();
    }

    public boolean redo(int sessionId) {
        SessionRuntime rt = ensureRuntime(sessionId);
        return rt.commandManager.redo();
    }

    public CountEntry[] topK(int sessionId, int k, int windowSeconds) {
        SessionRuntime rt = ensureRuntime(sessionId);
        long now = System.currentTimeMillis();
        long win = (windowSeconds <= 0) ? ANALYTICS_WINDOW_MILLIS : (windowSeconds * 1000L);
        return rt.analytics.topK(k, now, win);
    }

    // called by ExpirationManager callback
    public void expireTabById(int tabId) {
        int sid = tabDao.getSessionIdForTab(tabId);
        if (sid == -1) return;
        SessionRuntime rt = ensureRuntime(sid);

        TabRecord r = tabDao.getTabRecord(tabId);
        if (r == null) return;

        tabDao.deleteTab(tabId);
        rt.lru.remove(tabId);
        rt.expiration.cancelTabExpiry(tabId);

        activityLogDao.insert(sid, OperationType.TAB_CLOSED, "EXPIRED tabId=" + tabId);
    }

    public void expireSessionById(int sessionId) {
        sessionDao.setStatus(sessionId, "EXPIRED");
        activityLogDao.insert(sessionId, OperationType.SESSION_CLOSED, "EXPIRED");
    }

    public int expireAllDue() {
        final long now = System.currentTimeMillis();
        final int[] total = new int[]{0};

        runtimeStore.forEach(new CustomHashMap.EntryVisitor<Integer, SessionRuntime>() {
            @Override
            public void visit(Integer key, SessionRuntime rt) {
                total[0] += rt.expiration.expireDue(now);
            }
        });

        return total[0];
    }

    private SessionRuntime ensureRuntime(final int sessionId) {
        SessionRuntime rt = runtimeStore.get(sessionId);
        if (rt != null) return rt;

        ExpirationManager exp = new ExpirationManager(new ExpirationManager.ExpireCallback() {
            @Override
            public void expireTab(int tabId) { expireTabById(tabId); }

            @Override
            public void expireSession(int sid) { expireSessionById(sid); }
        });

        AnalyticsService analytics = new AnalyticsService();
        LruTabManager lru = new LruTabManager();

        // Rebuild LRU from DB (no Collections.sort, we do O(n^2) manually)
        rebuildLruFromDb(sessionId, lru);

        rt = new SessionRuntime(sessionId, exp, analytics, lru);
        runtimeStore.put(sessionId, rt);
        return rt;
    }

    private void rebuildLruFromDb(int sessionId, LruTabManager lru) {
        // Build LRU order by last_access_at descending (n<=50 so O(n^2) selection sort is fine)
        com.dsa.browsersession.dsa.array.DynamicArray<TabRecord> tabs = tabDao.listTabsBySession(sessionId);

        int n = tabs.size();
        if (n <= 0) return;

        // selection sort indexes by lastAccessAtMillis descending
        for (int i = 0; i < n; i++) {
            int best = i;
            long bestVal = tabs.get(i).lastAccessAtMillis;
            for (int j = i + 1; j < n; j++) {
                long v = tabs.get(j).lastAccessAtMillis;
                if (v > bestVal) {
                    bestVal = v;
                    best = j;
                }
            }
            if (best != i) {
                TabRecord tmp = tabs.get(i);
                tabs.set(i, tabs.get(best));
                tabs.set(best, tmp);
            }
        }

        // after sorting DESC (most recent first), restore head->tail properly:
        int[] order = new int[n];
        for (int i = 0; i < n; i++) {
            order[i] = tabs.get(i).tabId;
        }
        lru.restoreOrder(order);

    }
    public com.dsa.browsersession.dsa.array.DynamicArray<com.dsa.browsersession.domain.TabRecord>
    listTabs(int sessionId) {
        ensureRuntime(sessionId); // ensures session runtime exists
        return tabDao.listTabsBySession(sessionId);
    }
    public int[] lruOrder(int sessionId) {
        SessionRuntime rt = ensureRuntime(sessionId);
        return rt.lru.snapshotOrder();
    }
    public String[] evictionLog(int sessionId) {
        DynamicArray<EvictionEntry> arr = evictionLogDao.listBySession(sessionId);
        int n = arr.size();
        String[] out = new String[n];
        for (int i = 0; i < n; i++) out[i] = arr.get(i).toString();
        return out;
    }
    public DynamicArray<SessionSummary> listSessions() {
        return sessionDao.listSessions();
    }

    public DynamicArray<EvictionEntry> evictions(int sessionId) {
        return evictionLogDao.listBySession(sessionId);
    }

    public DynamicArray<ActivityEntry> activity(int sessionId) {
        return activityLogDao.listBySession(sessionId);
    }

}
