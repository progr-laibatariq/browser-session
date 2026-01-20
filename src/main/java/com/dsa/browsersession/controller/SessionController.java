package com.dsa.browsersession.controller;

import com.dsa.browsersession.service.BrowserEngineService;
import org.springframework.web.bind.annotation.*;
import com.dsa.browsersession.domain.SessionSummary;
import com.dsa.browsersession.domain.EvictionEntry;
import com.dsa.browsersession.domain.ActivityEntry;
import com.dsa.browsersession.dsa.array.DynamicArray;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final BrowserEngineService engine;

    public SessionController(BrowserEngineService engine) {
        this.engine = engine;
    }

    public static final class CreateSessionReq { public int maxCapacity; }
    public static final class CreateSessionRes { public int sessionId; }

    @PostMapping
    public CreateSessionRes create(@RequestBody CreateSessionReq req) {
        CreateSessionRes res = new CreateSessionRes();
        res.sessionId = engine.createSession(req.maxCapacity);
        return res;
    }

    @PostMapping("/{sid}/undo")
    public boolean undo(@PathVariable("sid") int sid) { return engine.undo(sid); }

    @PostMapping("/{sid}/redo")
    public boolean redo(@PathVariable("sid") int sid) { return engine.redo(sid); }
    


    @GetMapping
    public SessionSummary[] listSessions() {
        DynamicArray<SessionSummary> arr = engine.listSessions();
        int n = arr.size();
        SessionSummary[] out = new SessionSummary[n];
        for (int i = 0; i < n; i++) out[i] = arr.get(i);
        return out;
    }

    @GetMapping("/{sid}/evictions")
    public EvictionEntry[] evictions(@PathVariable("sid") int sid) {
        DynamicArray<EvictionEntry> arr = engine.evictions(sid);
        int n = arr.size();
        EvictionEntry[] out = new EvictionEntry[n];
        for (int i = 0; i < n; i++) out[i] = arr.get(i);
        return out;
    }

    @GetMapping("/{sid}/activity")
    public ActivityEntry[] activity(@PathVariable("sid") int sid) {
        DynamicArray<ActivityEntry> arr = engine.activity(sid);
        int n = arr.size();
        ActivityEntry[] out = new ActivityEntry[n];
        for (int i = 0; i < n; i++) out[i] = arr.get(i);
        return out;
    }

}
