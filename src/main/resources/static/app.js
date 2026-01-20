const $ = (id) => document.getElementById(id);

let sessions = [];
let currentSessionId = null;
let tabs = [];
let activeTabId = null;

function toast(msg){
    const t = $("toast");
    t.textContent = msg;
    t.classList.remove("hidden");
    clearTimeout(window.__toastTimer);
    window.__toastTimer = setTimeout(() => t.classList.add("hidden"), 1800);
}

async function api(path, opts){
    const res = await fetch(path, opts || {});
    if(!res.ok){
        const txt = await res.text();
        throw new Error(txt || ("HTTP " + res.status));
    }
    const ct = res.headers.get("content-type") || "";
    if(ct.includes("application/json")) return await res.json();
    return await res.text();
}

function fmtTime(ms){
    if(ms <= 0) return "—";
    const d = new Date(ms);
    return d.toLocaleString();
}

function secondsLeft(expiresAtMillis){
    if(expiresAtMillis <= 0) return null;
    const diff = Math.floor((expiresAtMillis - Date.now()) / 1000);
    return diff;
}

function shortUrl(url){
    try{
        const u = new URL(url);
        return u.hostname.replace("www.","");
    }catch(e){
        return url;
    }
}

async function loadSessions(){
    sessions = await api("/api/sessions");
    const sel = $("sessionSelect");
    sel.innerHTML = "";

    if(sessions.length === 0){
        const opt = document.createElement("option");
        opt.value = "";
        opt.textContent = "No sessions";
        sel.appendChild(opt);
        currentSessionId = null;
        return;
    }

    for(const s of sessions){
        const opt = document.createElement("option");
        opt.value = s.sessionId;
        opt.textContent = `#${s.sessionId} (${s.status}) cap=${s.maxCapacity}`;
        sel.appendChild(opt);
    }

    // keep current selection if exists
    if(currentSessionId == null || !sessions.some(x => x.sessionId === currentSessionId)){
        currentSessionId = sessions[0].sessionId;
    }
    sel.value = String(currentSessionId);
}

async function loadTabs(){
    if(currentSessionId == null) return;
    tabs = await api(`/api/sessions/${currentSessionId}/tabs`);

    // find active
    activeTabId = null;
    for(const t of tabs){
        if(t.isActive) activeTabId = t.tabId;
    }
    renderTabsBar();
    renderTabsList();
    renderActiveTab();
}

function renderTabsBar(){
    const bar = $("tabbar");
    bar.innerHTML = "";
    if(currentSessionId == null) return;

    if(tabs.length === 0){
        const empty = document.createElement("div");
        empty.className = "small";
        empty.textContent = "No tabs. Open one using the URL box below.";
        bar.appendChild(empty);
        return;
    }

    for(const t of tabs){
        const el = document.createElement("div");
        el.className = "tab" + (t.isActive ? " active" : "");
        el.title = t.url;

        const name = document.createElement("span");
        name.textContent = shortUrl(t.url);

        const close = document.createElement("button");
        close.className = "close";
        close.textContent = "×";
        close.title = "Close tab";
        close.addEventListener("click", async (e) => {
            e.stopPropagation();
            await closeTab(t.tabId);
        });

        el.appendChild(name);
        el.appendChild(close);

        el.addEventListener("click", async () => {
            await accessTab(t.tabId);
        });

        bar.appendChild(el);
    }
}

function renderActiveTab(){
    const card = $("activeTabCard");
    const t = tabs.find(x => x.tabId === activeTabId);
    if(!t){
        card.className = "card muted";
        card.textContent = "No active tab selected.";
        return;
    }

    const left = secondsLeft(t.expiresAtMillis);
    const expText = (left == null) ? "—" : (left < 0 ? "expired" : (left + "s"));

    card.className = "card";
    card.innerHTML = `
    <div class="itemTop">
      <div>
        <div><b>tabId:</b> ${t.tabId} <span class="badge active">ACTIVE</span></div>
        <div class="small"><b>URL:</b> ${t.url}</div>
      </div>
      <div class="small" style="text-align:right">
        <div><b>Last access:</b> ${fmtTime(t.lastAccessAtMillis)}</div>
        <div><b>Expires in:</b> ${expText}</div>
      </div>
    </div>
    <div class="actions">
      <button class="btn" id="activeAccessBtn">Access</button>
      <button class="btn" id="activeCloseBtn">Close</button>
    </div>
  `;

    $("activeAccessBtn").onclick = () => accessTab(t.tabId);
    $("activeCloseBtn").onclick = () => closeTab(t.tabId);
}

function renderTabsList(){
    const list = $("tabsList");
    list.innerHTML = "";

    if(tabs.length === 0){
        const empty = document.createElement("div");
        empty.className = "card muted";
        empty.textContent = "No tabs yet.";
        list.appendChild(empty);
        return;
    }

    for(const t of tabs){
        const item = document.createElement("div");
        item.className = "item";

        const left = secondsLeft(t.expiresAtMillis);
        const expText = (left == null) ? "—" : (left < 0 ? "expired" : (left + "s"));

        item.innerHTML = `
      <div class="itemTop">
        <div>
          <div><b>${shortUrl(t.url)}</b> <span class="badge ${t.isActive ? "active" : ""}">${t.isActive ? "ACTIVE" : "INACTIVE"}</span></div>
          <div class="small">tabId=${t.tabId} • ${t.url}</div>
        </div>
        <div class="small" style="text-align:right">
          <div>Last: ${fmtTime(t.lastAccessAtMillis)}</div>
          <div>Expires: ${expText}</div>
        </div>
      </div>
      <div class="actions">
        <button class="btn accessBtn">Access</button>
        <button class="btn closeBtn">Close</button>
      </div>
    `;

        item.querySelector(".accessBtn").onclick = () => accessTab(t.tabId);
        item.querySelector(".closeBtn").onclick = () => closeTab(t.tabId);
        list.appendChild(item);
    }
}

async function loadAnalytics(){
    if(currentSessionId == null) return;
    const k = Number($("kInput").value || 5);
    const win = Number($("windowInput").value || 300);
    const data = await api(`/api/sessions/${currentSessionId}/analytics/top-tabs?k=${k}&windowSeconds=${win}`);

    const box = $("analyticsList");
    box.innerHTML = "";
    if(!data || data.length === 0){
        box.innerHTML = `<div class="card muted">No analytics data yet. Access tabs to generate events.</div>`;
        return;
    }

    for(const e of data){
        const div = document.createElement("div");
        div.className = "item";
        div.innerHTML = `<div><b>tabId:</b> ${e.tabId} <span class="badge">count=${e.count}</span></div>`;
        box.appendChild(div);
    }
}

async function loadEvictions(){
    if(currentSessionId == null) return;
    const data = await api(`/api/sessions/${currentSessionId}/evictions`);
    const box = $("evictionsList");
    box.innerHTML = "";

    if(!data || data.length === 0){
        box.innerHTML = `<div class="card muted">No evictions yet.</div>`;
        return;
    }

    for(const e of data){
        const div = document.createElement("div");
        div.className = "item";
        div.innerHTML = `
      <div><b>#${e.evictionId}</b> tabId=${e.tabId} <span class="badge">${e.reason}</span></div>
      <div class="small">${fmtTime(e.evictedAtMillis)}</div>
    `;
        box.appendChild(div);
    }
}

async function loadActivity(){
    if(currentSessionId == null) return;
    const data = await api(`/api/sessions/${currentSessionId}/activity`);
    const box = $("activityList");
    box.innerHTML = "";

    if(!data || data.length === 0){
        box.innerHTML = `<div class="card muted">No activity yet.</div>`;
        return;
    }

    for(const a of data){
        const div = document.createElement("div");
        div.className = "item";
        div.innerHTML = `
      <div><b>#${a.logId}</b> <span class="badge">${a.operation}</span></div>
      <div class="small">${a.details || ""}</div>
      <div class="small">${fmtTime(a.createdAtMillis)}</div>
    `;
        box.appendChild(div);
    }
}

async function refreshAll(){
    await loadSessions();
    await loadTabs();
    await loadEvictions();
    await loadActivity();
    await loadAnalytics();
}

async function createSession(){
    const cap = Number($("capacityInput").value || 3);
    const res = await api("/api/sessions", {
        method:"POST",
        headers:{ "Content-Type":"application/json" },
        body: JSON.stringify({ maxCapacity: cap })
    });
    currentSessionId = res.sessionId;
    toast(`Session #${currentSessionId} created`);
    await refreshAll();
}

async function openTab(){
    if(currentSessionId == null) return;
    const url = $("openUrlInput").value.trim();
    if(!url){
        toast("Enter a URL first");
        return;
    }

    const res = await api(`/api/sessions/${currentSessionId}/tabs`, {
        method:"POST",
        headers:{ "Content-Type":"application/json" },
        body: JSON.stringify({ url })
    });

    $("openUrlInput").value = "";
    toast(`Opened tabId=${res.tabId}`);
    await refreshAll();
}

async function accessTab(tabId){
    if(currentSessionId == null) return;
    await api(`/api/sessions/${currentSessionId}/tabs/${tabId}/access`, { method:"POST" });
    toast(`Accessed tabId=${tabId}`);
    await refreshAll();
}

async function closeTab(tabId){
    if(currentSessionId == null) return;
    await api(`/api/sessions/${currentSessionId}/tabs/${tabId}`, { method:"DELETE" });
    toast(`Closed tabId=${tabId}`);
    await refreshAll();
}

async function undo(){
    if(currentSessionId == null) return;
    const ok = await api(`/api/sessions/${currentSessionId}/undo`, { method:"POST" });
    toast(ok ? "Undo successful" : "Nothing to undo");
    await refreshAll();
}

async function redo(){
    if(currentSessionId == null) return;
    const ok = await api(`/api/sessions/${currentSessionId}/redo`, { method:"POST" });
    toast(ok ? "Redo successful" : "Nothing to redo");
    await refreshAll();
}

function bindUI(){
    $("createSessionBtn").onclick = createSession;
    $("openTabBtn").onclick = openTab;
    $("undoBtn").onclick = undo;
    $("redoBtn").onclick = redo;
    $("refreshBtn").onclick = refreshAll;
    $("analyticsBtn").onclick = loadAnalytics;

    $("sessionSelect").onchange = async (e) => {
        currentSessionId = Number(e.target.value);
        await refreshAll();
    };

    $("openUrlInput").addEventListener("keydown", (e) => {
        if(e.key === "Enter") openTab();
    });
}

async function boot(){
    bindUI();
    await refreshAll();

    // auto-refresh tabs (for expiration updates)
    setInterval(async () => {
        if(currentSessionId == null) return;
        await loadTabs();
    }, 2000);
}

boot().catch(err => {
    console.error(err);
    toast("Error: open console (F12) to see details");
});
