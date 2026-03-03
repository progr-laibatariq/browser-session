package com.dsa.browsersession.dsa.map;

public final class CustomHashMap<K, V> {
    private Object[] keys;
    private Object[] values;
    private byte[] states;
    private int size;
    private int threshold; // resize when size >= threshold

    public interface EntryVisitor<K, V> {
        void visit(K key, V value);
    }

    public CustomHashMap() {
        this(16);
    }

    public CustomHashMap(int initialCapacity) {
        int cap = 1;
        while (cap < initialCapacity) cap <<= 1; // power of two
        keys = new Object[cap];
        values = new Object[cap];
        states = new byte[cap];
        size = 0;
        threshold = (int) (cap * 0.65);
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public V get(K key) {
        if (key == null) return null;
        int idx = findIndex(key);
        if (idx < 0) return null;
        @SuppressWarnings("unchecked")
        V v = (V) values[idx];
        return v;
    }

    public boolean containsKey(K key) {
        return findIndex(key) >= 0;
    }

    public V put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("null keys not allowed");
        if (size >= threshold) rehash();

        int slot = findSlotForInsert(key);
        if (states[slot] == HashEntryState.OCCUPIED) {
            @SuppressWarnings("unchecked")
            V old = (V) values[slot];
            values[slot] = value;
            return old;
        } else {
            keys[slot] = key;
            values[slot] = value;
            states[slot] = HashEntryState.OCCUPIED;
            size++;
            return null;
        }
    }

    public V remove(K key) {
        if (key == null) return null;
        int idx = findIndex(key);
        if (idx < 0) return null;

        @SuppressWarnings("unchecked")
        V old = (V) values[idx];

        keys[idx] = null;
        values[idx] = null;
        states[idx] = HashEntryState.DELETED;
        size--;
        return old;
    }

    public void clear() {
        // no Arrays.fill
        for (int i = 0; i < keys.length; i++) {
            keys[i] = null;
            values[i] = null;
            states[i] = HashEntryState.EMPTY;
        }
        size = 0;
    }

    public void forEach(EntryVisitor<K, V> visitor) {
        for (int i = 0; i < keys.length; i++) {
            if (states[i] == HashEntryState.OCCUPIED) {
                @SuppressWarnings("unchecked")
                K k = (K) keys[i];
                @SuppressWarnings("unchecked")
                V v = (V) values[i];
                visitor.visit(k, v);
            }
        }
    }

    private int findIndex(K key) {
        int mask = keys.length - 1;
        int h = spread(hash(key));
        int idx = h & mask;

        int probes = 0;
        while (probes < keys.length) {
            byte st = states[idx];
            if (st == HashEntryState.EMPTY) return -1;
            if (st == HashEntryState.OCCUPIED) {
                @SuppressWarnings("unchecked")
                K k = (K) keys[idx];
                if (equalsKey(k, key)) return idx;
            }
            idx = (idx + 1) & mask;
            probes++;
        }
        return -1;
    }

    private int findSlotForInsert(K key) {
        int mask = keys.length - 1;
        int h = spread(hash(key));
        int idx = h & mask;

        int firstDeleted = -1;
        int probes = 0;

        while (probes < keys.length) {
            byte st = states[idx];
            if (st == HashEntryState.EMPTY) {
                return (firstDeleted >= 0) ? firstDeleted : idx;
            }
            if (st == HashEntryState.DELETED) {
                if (firstDeleted < 0) firstDeleted = idx;
            } else { // OCCUPIED
                @SuppressWarnings("unchecked")
                K k = (K) keys[idx];
                if (equalsKey(k, key)) return idx;
            }
            idx = (idx + 1) & mask;
            probes++;
        }
        return (firstDeleted >= 0) ? firstDeleted : idx;
    }

    private void rehash() {
        Object[] oldKeys = keys;
        Object[] oldValues = values;
        byte[] oldStates = states;

        int newCap = oldKeys.length * 2;
        keys = new Object[newCap];
        values = new Object[newCap];
        states = new byte[newCap];
        size = 0;
        threshold = (int) (newCap * 0.65);

        for (int i = 0; i < oldKeys.length; i++) {
            if (oldStates[i] == HashEntryState.OCCUPIED) {
                @SuppressWarnings("unchecked")
                K k = (K) oldKeys[i];
                @SuppressWarnings("unchecked")
                V v = (V) oldValues[i];
                put(k, v);
            }
        }
    }

    private int hash(K key) {
        return key.hashCode();
    }

    private int spread(int h) {
        h ^= (h >>> 16);
        return h;
    }

    private boolean equalsKey(K a, K b) {
        return a == b || (a != null && a.equals(b));
    }
}
