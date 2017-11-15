package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class RandomAccessMemory<K, V extends Serializable> extends AbstractMemory<K, V> {
    private ConcurrentMap<K, V> map;


    @Override
    public Optional<V> load(Object key) {
        return Optional.ofNullable(map.get(key));
    }

    @Override
    public void store(K key, V value) {
        if (!map.containsKey(key))
            incrementSizeL();
        map.put(key, value);
    }

    @Override
    public Optional<V> discard(Object key) {
        Optional<V> value = Optional.ofNullable(map.remove(key));
        if (value.isPresent())
            decrementSizeL();
        return value;
    }
}
