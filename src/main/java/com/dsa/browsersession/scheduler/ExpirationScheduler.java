package com.dsa.browsersession.scheduler;

import com.dsa.browsersession.service.BrowserEngineService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExpirationScheduler {

    private final BrowserEngineService engine;

    public ExpirationScheduler(BrowserEngineService engine) {
        this.engine = engine;
    }

    @Scheduled(fixedDelay = 1000)
    public void tick() {
        engine.expireAllDue();
    }
}
