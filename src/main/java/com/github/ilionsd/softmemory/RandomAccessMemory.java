package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class RandomAccessMemory<K, V extends Serializable> extends AbstractMemory<K, V> {
    private ConcurrentMap<K, V> map;

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        if (!containsKey(key))
            incrementSizeL();
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        V value = map.remove(key);
        decrementSizeL();
        return value;
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
