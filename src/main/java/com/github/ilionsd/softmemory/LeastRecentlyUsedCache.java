package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public final class LeastRecentlyUsedCache<K, V extends Serializable> extends AbstractCache<K, V> {
    private AtomicLong recencyCounter = new AtomicLong(Long.MIN_VALUE);
    private Map<K, Long> recency = new HashMap<>();

    public LeastRecentlyUsedCache(Memory<K, V> buffer) {
        super(buffer);
    }

    protected long getCurrentRecency() {
        return recencyCounter.getAndIncrement();
    }

    @Override
    protected K replacementKey() {
        Iterator<Entry<K, Long>> iterator = recency.entrySet().iterator();
        Entry<K, Long> leastRecency = iterator.next();
        while (iterator.hasNext()) {
            Entry<K, Long> entry = iterator.next();
            if (Long.compare(leastRecency.getValue(), entry.getValue()) < 0)
                leastRecency = entry;
        }
        return leastRecency.getKey();
    }

    @Override
    public Optional<V> tryLoad(K key) {
        Optional<V> oValue = getBuffer().load(key);
        if (oValue.isPresent())
            recency.put(key, getCurrentRecency());
        return oValue;
    }

    @Override
    public Optional<Entry<K, V>> keep(K key, V value) {
        Optional<V> oValue = getBuffer().load(key);
        K replacementKey = null;
        if (oValue.isPresent())
            replacementKey = key;
        else if (getBuffer().isFull()) {
            replacementKey = replacementKey();
            oValue = discard(replacementKey);
        }
        K replacedKey = replacementKey;
        recency.put(key, getCurrentRecency());
        getBuffer().store(key, value);
        return oValue.map(replacedValue -> new SimpleEntry<>(replacedKey, replacedValue));
    }

    @Override
    public Optional<V> discard(K key) {
        recency.remove(key);
        return getBuffer().remove(key);
    }
}
