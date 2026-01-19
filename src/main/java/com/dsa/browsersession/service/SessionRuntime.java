package com.dsa.browsersession.service;

import com.dsa.browsersession.command.CommandManager;

public final class SessionRuntime {
    public final int sessionId;

    public final CommandManager commandManager;
    public final AnalyticsService analytics;
    public final ExpirationManager expiration;
    public final LruTabManager lru;

    public SessionRuntime(int sessionId,
                          ExpirationManager expiration,
                          AnalyticsService analytics,
                          LruTabManager lru) {
        this.sessionId = sessionId;
        this.commandManager = new CommandManager();
        this.analytics = analytics;
        this.expiration = expiration;
        this.lru = lru;
    }
}
