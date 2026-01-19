package com.dsa.browsersession.service;

import com.dsa.browsersession.dsa.map.CustomHashMap;
import org.springframework.stereotype.Component;

@Component
public class RuntimeStore {
    private final CustomHashMap<Integer, SessionRuntime> map = new CustomHashMap<>(64);

    public SessionRuntime get(int sessionId) {
        return map.get(sessionId);
    }

    public void put(int sessionId, SessionRuntime rt) {
        map.put(sessionId, rt);
    }

    public void forEach(CustomHashMap.EntryVisitor<Integer, SessionRuntime> visitor) {
        map.forEach(visitor);
    }
}
