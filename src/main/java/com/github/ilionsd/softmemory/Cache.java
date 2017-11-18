package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Map.Entry;
import java.util.Optional;

public interface Cache<K, V extends Serializable> {
    Memory<K, V> getBuffer();

    Optional<V> tryLoad(K key);
    Optional<Entry<K, V>> keep(K key, V value);
    Optional<V> discard(K key);
}
