package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Optional;

public abstract class AbstractCache<K, V extends Serializable> implements Cache<K, V> {
    private Optional<Memory<K, V>> maybeCache = Optional.empty();

    @Override
    public void setCache(Memory<K, V> memory) {
        maybeCache = Optional.of(memory);
    }

    @Override
    public Optional<Memory<K, V>> getCache() {
        return maybeCache;
    }
}
