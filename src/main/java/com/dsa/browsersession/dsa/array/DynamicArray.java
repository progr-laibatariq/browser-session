package com.dsa.browsersession.dsa.array;

public final class DynamicArray<T> {
    private Object[] data;
    private int size;

    public DynamicArray() {
        this(16);
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity < 1)
            initialCapacity = 1;
        this.data = new Object[initialCapacity];
        this.size = 0;
    }

    public int size(){
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(T value) {
        ensureCapacity(size + 1);
        data[size++] = value;
    }

    public T get(int index) {
        rangeCheck(index);
        @SuppressWarnings("unchecked")
        T v = (T) data[index];
        return v;
    }

    public void set(int index, T value) {
        rangeCheck(index);
        data[index] = value;
    }

    public T removeLast() {
        if (size == 0)
            return null;
        int last = size - 1;
        @SuppressWarnings("unchecked")
        T v = (T) data[last];
        data[last] = null;
        size--;
        return v;
    }

    public void clear() {
        // No Arrays.fill (avoid built-in algorithms)
        for (int i = 0; i < size; i++)
            data[i] = null;
        size = 0;
    }

    private void ensureCapacity(int needed) {
        if (needed <= data.length)
            return;
        int newCap = data.length * 2;
        if (newCap < needed)
            newCap = needed;

        Object[] newData = new Object[newCap];
        for (int i = 0; i < size; i++)
            newData[i] = data[i];
        data = newData;
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
    }
}
