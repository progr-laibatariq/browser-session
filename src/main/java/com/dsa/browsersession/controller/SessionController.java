package com.dsa.browsersession.controller;

import com.dsa.browsersession.service.BrowserEngineService;
import org.springframework.web.bind.annotation.*;

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
    
    @GetMapping("/{sid}/evictions")
    public String[] evictions(@PathVariable("sid") int sid) {
        return engine.evictionLog(sid);
    }

}
