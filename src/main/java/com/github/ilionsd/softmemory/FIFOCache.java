package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;

public final class FIFOCache<K, V extends Serializable> extends AbstractCache<K, V> {
    private Deque<K> deque = new LinkedList<>();

    public FIFOCache(Memory<K, V> buffer) {
        super(buffer);
    }

    @Override
    protected K replacementKey() {
        return deque.element();
    }

    @Override
    public Optional<V> tryLoad(K key) {
        return getBuffer().load(key);
    }

    @Override
    public Optional<Entry<K, V>> keep(K key, V value) {
        Optional<V> oValue = getBuffer().load(key);
        K replacementKey = null;
        if (oValue.isPresent()) {
            replacementKey = key;
            discard(key);
        }
        else if (getBuffer().isFull()) {
            replacementKey = replacementKey();
            oValue = discard(replacementKey);
        }
        K replacedKey = replacementKey;
        deque.add(key);
        getBuffer().store(key, value);
        return oValue.map(replacedValue -> new SimpleEntry<>(replacedKey, replacedValue));
    }

    @Override
    public Optional<V> discard(K key) {
        deque.remove(key);
        return getBuffer().remove(key);
    }
}
