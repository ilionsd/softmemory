package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Optional;

public interface CachedMemory<K, V extends Serializable> extends Memory<K, V> {
    Cache<K, V> getCache();
    void setCache(Cache<K, V> cache);
    Memory<K, V> getMemory();

    @Override
    default long capacity() {
        return getMemory().capacity();
    }

    @Override
    default long size() {
        return getMemory().size();
    }
}
