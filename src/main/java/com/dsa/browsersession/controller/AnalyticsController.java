package com.dsa.browsersession.controller;

import com.dsa.browsersession.domain.CountEntry;
import com.dsa.browsersession.service.BrowserEngineService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions/{sid}/analytics")
public class AnalyticsController {

    private final BrowserEngineService engine;

    public AnalyticsController(BrowserEngineService engine) {
        this.engine = engine;
    }

    @GetMapping("/top-tabs")
    public CountEntry[] topTabs(@PathVariable("sid") int sid,
                                @RequestParam(name = "k", defaultValue = "5") int k,
                                @RequestParam(name = "windowSeconds", defaultValue = "300") int windowSeconds) {
        return engine.topK(sid, k, windowSeconds);
    }
}
