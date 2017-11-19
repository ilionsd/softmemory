package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Map-like interface to store association pairs
 * @param <K> Key type
 * @param <V> Value type
 */
public interface Memory<K, V extends Serializable> {

    /**
     * Maximum number of elements to be stored
     * @return capacity
     */
    long capacity();

    /**
     * Number of stored elements
     * @return size
     */
    long size();

    /**
     * Compares size and capacity
     * @return boolean
     */
    default boolean isFull() {
        return Long.compare(size(), capacity()) >= 0;
    }

    /**
     * Returns value associated with <code>key</code>
     * @param  key key of association
     * @return associated value wrapped in {@link Optional}.
     *         If there is no such value, returns {@link Optional#empty()}
     * @see    Optional
     */
    Optional<V> load(K key);

    /**
     * Creates or changes association <code>key</code>-><code>value</code>
     * @param  key   key of association
     * @param  value associated with <code>key</code> value
     * @return if association with <code>key</code> already existed, returns previous value,
     *         wrapped in {@link Optional}.
     *         If there is no such value, returns {@link Optional#empty()}
     */
    Optional<V> store(K key, V value);

    /**
     * Removes association with <code>key</code>
     * @param  key key of association
     * @return value of removed association, wrapped in {@link Optional}.
     *         If there is no such value, returns {@link Optional#empty()}
     * @see Optional
     */
    Optional<V> remove(K key);

    /**
     * Returns {@link Set} of keys of stored associations
     * @return set of keys
     * @see    Set
     */
    Set<K> keySet();

    /**
     * Creates or changes associations for all entries of {@link Map}
     * @param m associative container
     * @see Memory#store(Object, Serializable)
     */
    default void storeAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            store(entry.getKey(), entry.getValue());
        }
    }
}
