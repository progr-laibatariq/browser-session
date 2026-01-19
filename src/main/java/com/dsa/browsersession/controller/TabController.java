package com.dsa.browsersession.controller;
import com.dsa.browsersession.domain.TabRecord;
import com.dsa.browsersession.dsa.array.DynamicArray;
import com.dsa.browsersession.service.BrowserEngineService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions/{sid}/tabs")
public class TabController {

    private final BrowserEngineService engine;

    public TabController(BrowserEngineService engine) {
        this.engine = engine;
    }

    public static final class OpenTabReq { public String url; }
    public static final class OpenTabRes { public int tabId; }

    @PostMapping
    public OpenTabRes open(@PathVariable("sid") int sid, @RequestBody OpenTabReq req) {
        OpenTabRes res = new OpenTabRes();
        res.tabId = engine.openTab(sid, req.url);
        return res;
    }

    @PostMapping("/{tid}/access")
    public void access(@PathVariable("sid") int sid, @PathVariable("tid") int tid) {
        engine.accessTab(sid, tid);
    }

    @DeleteMapping("/{tid}")
    public void close(@PathVariable("sid") int sid, @PathVariable("tid") int tid) {
        engine.closeTab(sid, tid);
    }
    @GetMapping
    public TabRecord[] list(@PathVariable("sid") int sid) {
        DynamicArray<TabRecord> arr = engine.listTabs(sid);
        int n = arr.size();
        TabRecord[] out = new TabRecord[n];
        for (int i = 0; i < n; i++) out[i] = arr.get(i);
        return out;
    }
    @GetMapping("/lru-order")
    public int[] lru(@PathVariable("sid") int sid) {
        return engine.lruOrder(sid);
    }

}
