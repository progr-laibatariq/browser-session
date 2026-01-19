package com.dsa.browsersession.command;

import com.dsa.browsersession.dao.ActivityLogDao;
import com.dsa.browsersession.dao.EvictionLogDao;
import com.dsa.browsersession.dao.TabDao;
import com.dsa.browsersession.domain.EvictionReason;
import com.dsa.browsersession.domain.OperationType;
import com.dsa.browsersession.domain.TabRecord;
import com.dsa.browsersession.service.SessionRuntime;

public final class CloseTabCommand implements Command {

    private final int sessionId;
    private int tabId;

    private final TabDao tabDao;
    private final ActivityLogDao activityLogDao;
    private final EvictionLogDao evictionLogDao;
    private final SessionRuntime rt;

    private TabRecord closedRecord;
    private int prevActiveTabId;
    private int[] prevLruOrder;

    public CloseTabCommand(int sessionId, int tabId,
                           TabDao tabDao, ActivityLogDao activityLogDao, EvictionLogDao evictionLogDao,
                           SessionRuntime rt) {
        this.sessionId = sessionId;
        this.tabId = tabId;
        this.tabDao = tabDao;
        this.activityLogDao = activityLogDao;
        this.evictionLogDao = evictionLogDao;
        this.rt = rt;
    }

    @Override
    public void execute() {
        prevActiveTabId = tabDao.getActiveTabId(sessionId);
        prevLruOrder = rt.lru.snapshotOrder();

        closedRecord = tabDao.getTabRecord(tabId);
        if (closedRecord == null) return;

        tabDao.deleteTab(tabId);
        rt.lru.remove(tabId);
        rt.expiration.cancelTabExpiry(tabId);

        evictionLogDao.insert(sessionId, tabId, EvictionReason.MANUAL_CLOSE);
        activityLogDao.insert(sessionId, OperationType.TAB_CLOSED, "MANUAL_CLOSE tabId=" + tabId);
    }

    @Override
    public void undo() {
        if (closedRecord == null) return;

        tabDao.insertTabWithId(closedRecord);
        rt.lru.restoreOrder(prevLruOrder);

        // restore active
        if (prevActiveTabId != -1) tabDao.setActiveTab(sessionId, prevActiveTabId);

        activityLogDao.insert(sessionId, OperationType.SESSION_UPDATED, "UNDO CloseTab tabId=" + tabId);
    }

    @Override
    public String name() { return "CloseTab"; }
}
