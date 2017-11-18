package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

public class WriteThroughCachedMemory<K, V extends Serializable> extends AbstractCachedMemory<K, V> {

    public WriteThroughCachedMemory(Memory<K, V> memory, Cache<K, V> cache) {
        super(memory, cache);
    }

    @Override
    protected Optional<V> cacheMiss(K key) {
        Optional<V> oValue = getMemory().load(key);
        oValue.ifPresent(value -> getCache().keep(key, value));
        return oValue;
    }

    @Override
    public Optional<V> load(K key) {
        Optional<V> oValue = getCache().tryLoad(key);
        return oValue.map(Optional::of).orElseGet(() -> cacheMiss(key));
    }

    @Override
    public Optional<V> store(K key, V value) {
        getCache().keep(key, value);
        return getMemory().store(key, value);
    }

    @Override
    public Optional<V> remove(K key) {
        getCache().discard(key);
        return getMemory().remove(key);
    }

    @Override
    public Set<K> keySet() {
        return getMemory().keySet();
    }
}
