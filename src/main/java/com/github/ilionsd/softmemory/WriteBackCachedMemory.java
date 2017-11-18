package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class WriteBackCachedMemory<K, V extends Serializable> extends AbstractCachedMemory<K, V> {

    public WriteBackCachedMemory(Memory<K, V> memory, Cache<K, V> cache) {
        super(memory, cache);
    }

    @Override
    protected Optional<V> cacheMiss(K key) {
        Optional<V> oValue = getMemory().load(key);
        Optional<Map.Entry<K, V>> oEntry = oValue.flatMap(value -> getCache().keep(key, value));
        oEntry.ifPresent(entry -> getMemory().store(entry.getKey(), entry.getValue()));
        return oValue;
    }

    @Override
    public Optional<V> load(K key) {
        Optional<V> oValue = getCache().tryLoad(key);
        return oValue.map(Optional::of).orElseGet(() -> cacheMiss(key));
    }

    @Override
    public Optional<V> store(K key, V value) {
        Optional<Map.Entry<K, V>> oEntry = getCache().keep(key, value);
        Optional<V> oValue;
        if (oEntry.map(entry -> entry.getKey().equals(key)).orElse(false))
            oValue = oEntry.map(entry -> entry.getValue());
        else
            oValue = oEntry.flatMap(entry -> getMemory().store(entry.getKey(), entry.getValue()));
        return oValue;
    }

    @Override
    public Optional<V> remove(K key) {
        Optional<V> oValue = getCache().discard(key);
        getMemory().remove(key);
        return oValue;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = getMemory().keySet();
        keySet.addAll(getCache().getBuffer().keySet());
        return keySet;
    }
}
