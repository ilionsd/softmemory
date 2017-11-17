package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RandomAccessMemory<K, V extends Serializable> extends AbstractMemory<K, V> {
    public static final long DEFAULT_CAPACITY = 100L;

    private ConcurrentMap<K, V> map;

    public RandomAccessMemory() {
        this(DEFAULT_CAPACITY);
    }

    public RandomAccessMemory(long capacity) {
        super(capacity);
        map = new ConcurrentHashMap<>();
    }

    @Override
    public Optional<V> load(K key) {
        return Optional.ofNullable(map.get(key));
    }

    @Override
    public Optional<V> store(K key, V value) {
        Optional<V> oValue = load(key);
        if (!oValue.isPresent())
            incrementSizeL();
        map.put(key, value);
        return oValue;
    }

    @Override
    public Optional<V> remove(K key) {
        Optional<V> value = Optional.ofNullable(map.remove(key));
        if (value.isPresent())
            decrementSizeL();
        return value;
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }
}
