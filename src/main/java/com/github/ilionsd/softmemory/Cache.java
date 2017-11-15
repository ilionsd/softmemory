package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Optional;

public interface Cache<K, V extends Serializable> extends Memory<K, V> {
    void setCache(Memory<K, V> memory);
    Optional<Memory<K, V>> getCache();
    Memory<K, V> getMemory();

    @Override
    default long capacityL() {
        return getMemory().capacityL();
    }

    @Override
    default long sizeL() {
        return getMemory().sizeL();
    }

    @Override
    default boolean isEmpty() {
        return getMemory().isEmpty();
    }

    @Override
    default boolean containsKey(Object key) {
        return getMemory().containsKey(key);
    }

    @Override
    default boolean containsValue(Object value) {
        return getMemory().containsValue(value);
    }

    @Override
    default V get(Object key) {
        return getCache()
                .flatMap( memory -> Optional.ofNullable(memory.get(key)) )
                .orElseGet( () -> getMemory().get(key) );
    }
}
