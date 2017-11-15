package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Optional;

public interface Cache<K, V extends Serializable> extends Memory<K, V> {
    Memory<K, V> getMemory();
    DiscardPolicy<K> getDiscardPolicy();
    void setDiscardPolicy(DiscardPolicy<K> discardPolicy);

    @Override
    default long capacity() {
        return getMemory().capacity();
    }

    @Override
    default long size() {
        return getMemory().size();
    }

    @Override
    default Optional<V> load(K key) {
        getDiscardPolicy().used(key);
        return getMemory().load(key);
    }

    @Override
    default void store(K key, V value) {
        if (getMemory().isFull()) synchronized (this) {
            getMemory().discard(getDiscardPolicy().discardKey());
        }
        getDiscardPolicy().used(key);
        getMemory().store(key, value);
    }

    @Override
    default Optional<V> discard(K key) {
        Optional<V> oValue;
        synchronized (this) {
            getDiscardPolicy().discard(key);
            oValue = getMemory().discard(key);
        }
        return oValue;
    }
}
