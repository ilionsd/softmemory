package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * @param <K>
 * @param <V>
 */
public interface Memory<K, V extends Serializable> extends Map<K, V> {
    long capacityL();
    long sizeL();

    default boolean isFull() {
        return Long.compare(sizeL(), capacityL()) >= 0;
    }

    default boolean isNotFull() {
        return Long.compare(sizeL(), capacityL()) < 0;
    }

    @Override
    default int size() {
        if (Long.compare(Integer.MAX_VALUE, sizeL()) <= 0)
            return Integer.MAX_VALUE;
        return (int)sizeL();
    }

    @Override
    default void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

}
