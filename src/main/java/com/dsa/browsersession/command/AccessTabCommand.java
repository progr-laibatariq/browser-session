package com.dsa.browsersession.command;

import com.dsa.browsersession.dao.AccessLogDao;
import com.dsa.browsersession.dao.ActivityLogDao;
import com.dsa.browsersession.dao.SessionDao;
import com.dsa.browsersession.dao.TabDao;
import com.dsa.browsersession.domain.OperationType;
import com.dsa.browsersession.service.SessionRuntime;

public final class AccessTabCommand implements Command {

    private final int sessionId;
    private final int tabId;

    private final SessionDao sessionDao;
    private final TabDao tabDao;
    private final AccessLogDao accessLogDao;
    private final ActivityLogDao activityLogDao;
    private final SessionRuntime rt;

    private int prevActiveTabId;
    private int[] prevLruOrder;

    private long recordAccessTimeMillis;

    private final long tabTtlMillis;
    private final long sessionTtlMillis;
    private final long analyticsWindowMillis;

    public AccessTabCommand(int sessionId, int tabId,
                            SessionDao sessionDao, TabDao tabDao, AccessLogDao accessLogDao,
                            ActivityLogDao activityLogDao,
                            SessionRuntime rt,
                            long tabTtlMillis, long sessionTtlMillis, long analyticsWindowMillis) {
        this.sessionId = sessionId;
        this.tabId = tabId;

        this.sessionDao = sessionDao;
        this.tabDao = tabDao;
        this.accessLogDao = accessLogDao;
        this.activityLogDao = activityLogDao;
        this.rt = rt;

        this.tabTtlMillis = tabTtlMillis;
        this.sessionTtlMillis = sessionTtlMillis;
        this.analyticsWindowMillis = analyticsWindowMillis;
    }

    @Override
    public void execute() {
        long now = System.currentTimeMillis();

        prevActiveTabId = tabDao.getActiveTabId(sessionId);
        prevLruOrder = rt.lru.snapshotOrder();

        tabDao.setActiveTab(sessionId, tabId);
        rt.lru.touch(tabId);

        long tabExpires = now + tabTtlMillis;
        tabDao.updateAccessAndExpiry(tabId, now, tabExpires);

        recordAccessTimeMillis = now;
        rt.analytics.recordAccess(tabId, now, analyticsWindowMillis);
        accessLogDao.insertAccess(tabId, now);

        rt.expiration.scheduleTabExpiry(tabId, tabExpires);
        sessionDao.updateLastActiveAndExpiry(sessionId, now, now + sessionTtlMillis);

        activityLogDao.insert(sessionId, OperationType.SESSION_UPDATED, "ACCESS tabId=" + tabId);
    }

    @Override
    public void undo() {
        long now = System.currentTimeMillis();

        rt.lru.restoreOrder(prevLruOrder);
        if (prevActiveTabId != -1) tabDao.setActiveTab(sessionId, prevActiveTabId);

        rt.analytics.undoLastAccess(tabId, recordAccessTimeMillis, now, analyticsWindowMillis);

        activityLogDao.insert(sessionId, OperationType.SESSION_UPDATED, "UNDO Access tabId=" + tabId);
    }

    @Override
    public String name() { return "AccessTab"; }
}
