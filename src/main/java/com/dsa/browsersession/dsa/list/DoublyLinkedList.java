package com.dsa.browsersession.dsa.list;

public final class DoublyLinkedList<T> {
    private DoublyNode<T> head;
    private DoublyNode<T> tail;
    private int size;

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public DoublyNode<T> headNode() { return head; }
    public DoublyNode<T> tailNode() { return tail; }

    public DoublyNode<T> addFirst(T value) {
        DoublyNode<T> node = new DoublyNode<>(value);
        if (head == null) {
            head = tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
        size++;
        return node;
    }

    public DoublyNode<T> addLast(T value) {
        DoublyNode<T> node = new DoublyNode<>(value);
        if (tail == null) {
            head = tail = node;
        } else {
            node.prev = tail;
            tail.next = node;
            tail = node;
        }
        size++;
        return node;
    }

    public T removeFirst() {
        if (head == null) return null;
        DoublyNode<T> node = head;
        removeNode(node);
        return node.value;
    }

    public T removeLast() {
        if (tail == null) return null;
        DoublyNode<T> node = tail;
        removeNode(node);
        return node.value;
    }

    public void moveToFront(DoublyNode<T> node) {
        if (node == null || node == head) return;
        // detach
        DoublyNode<T> p = node.prev;
        DoublyNode<T> n = node.next;
        if (p != null) p.next = n;
        if (n != null) n.prev = p;
        if (node == tail) tail = p;

        // attach at head
        node.prev = null;
        node.next = head;
        if (head != null) head.prev = node;
        head = node;
        if (tail == null) tail = head;
    }

    public void removeNode(DoublyNode<T> node) {
        if (node == null) return;

        DoublyNode<T> p = node.prev;
        DoublyNode<T> n = node.next;

        if (p != null) p.next = n;
        else head = n;

        if (n != null) n.prev = p;
        else tail = p;

        node.prev = null;
        node.next = null;
        size--;
    }

    public void clear() {
        DoublyNode<T> cur = head;
        while (cur != null) {
            DoublyNode<T> next = cur.next;
            cur.prev = null;
            cur.next = null;
            cur.value = null;
            cur = next;
        }
        head = tail = null;
        size = 0;
    }
}
