package com.dsa.browsersession.dsa.deque;

import com.dsa.browsersession.dsa.list.DoublyLinkedList;
import com.dsa.browsersession.dsa.list.DoublyNode;

public final class CustomDeque<T> {
    private final DoublyLinkedList<T> list = new DoublyLinkedList<>();

    public int size() { return list.size(); }
    public boolean isEmpty() { return list.isEmpty(); }

    public void pushBack(T value) { list.addLast(value); }
    public void pushFront(T value) { list.addFirst(value); }

    public T popFront() { return list.removeFirst(); }
    public T popBack() { return list.removeLast(); }

    public T peekFront() {
        DoublyNode<T> n = list.headNode();
        return (n == null) ? null : n.value;
    }

    public T peekBack() {
        DoublyNode<T> n = list.tailNode();
        return (n == null) ? null : n.value;
    }
}
