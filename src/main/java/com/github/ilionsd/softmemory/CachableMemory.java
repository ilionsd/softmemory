package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Optional;

public interface CachableMemory<K, V extends Serializable> extends Memory<K, V> {
    void setCache(Memory<K, V> cache);
    Optional<Memory<K, V>> getCache();
}
