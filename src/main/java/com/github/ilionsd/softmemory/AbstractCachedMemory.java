package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Optional;

public abstract class AbstractCachedMemory<K, V extends Serializable> implements CachedMemory<K, V> {
    private Memory<K, V> memory;
    private Cache<K, V> cache;

    public AbstractCachedMemory(Memory<K, V> memory, Cache<K, V> cache) {
        this.memory = memory;
        this.cache = cache;
    }

    @Override
    public void setCache(Cache<K, V> cache) {
        this.cache = cache;
    }

    @Override
    public Cache<K, V> getCache() {
        return cache;
    }

    @Override
    public Memory<K, V> getMemory() {
        return memory;
    }
}
