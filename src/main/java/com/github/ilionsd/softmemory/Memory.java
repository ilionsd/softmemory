package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * @param <K>
 * @param <V>
 */
public interface Memory<K, V extends Serializable> {
    long capacity();
    long size();

    default boolean isFull() {
        return Long.compare(size(), capacity()) >= 0;
    }

    default boolean isNotFull() {
        return Long.compare(size(), capacity()) < 0;
    }

    Optional<V> load(K key);
    void store(K key, V value);
    Optional<V> discard(K key);

    default void storeAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            store(entry.getKey(), entry.getValue());
        }
    }

}
