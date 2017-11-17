package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Optional;

public interface Cache<K, V extends Serializable> {
    Memory<K, V> getMemory();

    Optional<V> tryLoad(K key);
    Optional<V> keep(K key, V value);
    Optional<V> discard(K key);
}
