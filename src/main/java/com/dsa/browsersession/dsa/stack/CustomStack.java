package com.dsa.browsersession.dsa.stack;

import com.dsa.browsersession.dsa.array.DynamicArray;

public final class CustomStack<T> {
    private final DynamicArray<T> arr;

    public CustomStack() {
        this.arr = new DynamicArray<>();
    }

    public int size() {
        return arr.size();
    }

    public boolean isEmpty() {
        return arr.isEmpty();
    }

    public void push(T value) {
        arr.add(value);
    }

    public T pop() {
        return arr.removeLast();
    }

    public T peek() {
        int n = arr.size();
        if (n == 0) return null;
        return arr.get(n - 1);
    }

    public void clear() {
        arr.clear();
    }
}
