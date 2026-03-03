package com.dsa.browsersession.dsa.heap;

public final class MaxHeap<T extends Comparable<T>> {
    private Object[] data;
    private int size;

    public MaxHeap() {
        this(16);
    }
    public MaxHeap(int cap) {
        if (cap < 1)
            cap = 1;
        data = new Object[cap];
        size = 0;
    }

    public int size() {
        return size;
    }
    public boolean isEmpty() {
        return size == 0;
    }

    public T peek() {
        if (size == 0)
            return null;
        @SuppressWarnings("unchecked")
        T v = (T) data[0];
        return v;
    }

    public void push(T value) {
        ensureCapacity(size + 1);
        data[size] = value;
        siftUp(size);
        size++;
    }

    public T pop() {
        if (size == 0) return null;
        @SuppressWarnings("unchecked")
        T root = (T) data[0];
        size--;
        data[0] = data[size];
        data[size] = null;
        siftDown(0);
        return root;
    }

    private void siftUp(int i) {
        while (i > 0) {
            int p = (i - 1) / 2;
            if (compare(i, p) <= 0) break;
            swap(i, p);
            i = p;
        }
    }

    private void siftDown(int i) {
        while (true) {
            int l = 2 * i + 1;
            int r = 2 * i + 2;
            if (l >= size) break;

            int largest = l;
            if (r < size && compare(r, l) > 0) largest = r;

            if (compare(i, largest) >= 0) break;
            swap(i, largest);
            i = largest;
        }
    }

    private int compare(int i, int j) {
        @SuppressWarnings("unchecked")
        T a = (T) data[i];
        @SuppressWarnings("unchecked")
        T b = (T) data[j];
        return a.compareTo(b);
    }

    private void swap(int i, int j) {
        Object tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    private void ensureCapacity(int needed) {
        if (needed <= data.length) return;
        int newCap = data.length * 2;
        if (newCap < needed) newCap = needed;
        Object[] nd = new Object[newCap];
        for (int i = 0; i < size; i++) nd[i] = data[i];
        data = nd;
    }
}
