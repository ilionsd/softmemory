package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class WriteThroughCache<K, V extends Serializable> extends AbstractCache<K, V> {
    private Memory<K, V> memory;

    @Override
    public Memory<K, V> getMemory() {
        return memory;
    }



    @Override
    public V put(K key, V value) {
        return null;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }
}
