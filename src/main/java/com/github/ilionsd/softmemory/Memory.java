package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

    Optional<V> load(K key);
    Optional<V> store(K key, V value);
    Optional<V> remove(K key);

    Set<K> keySet();

    default void storeAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            store(entry.getKey(), entry.getValue());
        }
    }
}
