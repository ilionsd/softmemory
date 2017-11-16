package com.github.ilionsd.softmemory;

import com.github.ilionsd.softmemory.replacementpolicy.ReplacementPolicy;

import java.io.Serializable;
import java.util.Optional;

public interface Cache<K, V extends Serializable> {
    Memory<K, V> getMemory();
    ReplacementPolicy<K> getReplacementPolicy();
    void setReplacementPolicy(ReplacementPolicy<K> replacementPolicy);

    default Optional<V> tryLoad(K key) {
        getReplacementPolicy().used(key);
        return getMemory().load(key);
    }

    default void keep(K key, V value) {
        if (getMemory().isFull()) synchronized (this) {
            K replaceKey = getReplacementPolicy().replaceKey();
            getMemory().remove(replaceKey);
            getReplacementPolicy().discard(replaceKey);
        }
        getReplacementPolicy().used(key);
        getMemory().store(key, value);
    }

    default Optional<V> discard(K key) {
        Optional<V> oValue;
        synchronized (this) {
            getReplacementPolicy().discard(key);
            oValue = getMemory().remove(key);
        }
        return oValue;
    }
}
