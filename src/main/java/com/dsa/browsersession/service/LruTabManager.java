package com.dsa.browsersession.service;

import com.dsa.browsersession.dsa.list.DoublyLinkedList;
import com.dsa.browsersession.dsa.list.DoublyNode;
import com.dsa.browsersession.dsa.map.CustomHashMap;

public final class LruTabManager {
    private final DoublyLinkedList<Integer> list = new DoublyLinkedList<>();
    private final CustomHashMap<Integer, DoublyNode<Integer>> nodeByTabId = new CustomHashMap<>(64);

    public int size() { return list.size(); }

    public void clear() {
        list.clear();
        nodeByTabId.clear();
    }

    public void addToFront(int tabId) {
        DoublyNode<Integer> n = list.addFirst(tabId);
        nodeByTabId.put(tabId, n);
    }

    public void remove(int tabId) {
        DoublyNode<Integer> n = nodeByTabId.remove(tabId);
        if (n != null) list.removeNode(n);
    }

    public void touch(int tabId) {
        DoublyNode<Integer> n = nodeByTabId.get(tabId);
        if (n != null) list.moveToFront(n);
    }

    public int peekLeastRecentlyUsed() {
        DoublyNode<Integer> t = list.tailNode();
        return (t == null) ? -1 : t.value.intValue();
    }

    // Avoid evicting active tab if possible
    public int pickEvictionCandidate(int activeTabId) {
        DoublyNode<Integer> cur = list.tailNode();
        while (cur != null) {
            int id = cur.value.intValue();
            if (id != activeTabId) return id;
            cur = cur.prev;
        }
        // only active exists
        return activeTabId;
    }

    public int[] snapshotOrder() {
        int n = list.size();
        int[] arr = new int[n];
        DoublyNode<Integer> cur = list.headNode();
        int i = 0;
        while (cur != null) {
            arr[i++] = cur.value.intValue();
            cur = cur.next;
        }
        return arr;
    }

    public void restoreOrder(int[] order) {
        clear();
        if (order == null) return;
        for (int i = 0; i < order.length; i++) {
            // rebuild from head->tail using addLast
            DoublyNode<Integer> n = list.addLast(order[i]);
            nodeByTabId.put(order[i], n);
        }
    }
}
