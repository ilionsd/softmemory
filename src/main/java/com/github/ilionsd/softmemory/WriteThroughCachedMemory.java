package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Optional;

public class WriteThroughCachedMemory<K, V extends Serializable> extends AbstractCachedMemory<K, V> {
    protected Optional<V> cacheMiss(K key) {
        Optional<V> oValue = getMemory().load(key);
        oValue.ifPresent(value -> getCache().store(key, value));
        return oValue;
    }

    @Override
    public Optional<V> load(K key) {
        Optional<V> oValue = getCache().load(key);
        return oValue.map(Optional::of).orElseGet(() -> cacheMiss(key));
    }

    @Override
    public void store(K key, V value) {
        getMemory().store(key, value);
    }

    @Override
    public Optional<V> discard(K key) {
        getCache().discard(key);
        return getMemory().discard(key);
    }
}
