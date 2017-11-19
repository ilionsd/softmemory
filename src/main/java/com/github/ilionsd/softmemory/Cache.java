package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * Realization of <a href="https://en.wikipedia.org/wiki/Cache_replacement_policies">cache replacement policies</a> over
 * {@link Memory} buffer
 * @param <K> key of {@link Memory} buffer
 * @param <V> value of {@link Memory} buffer
 * @see Memory
 */
public interface Cache<K, V extends Serializable> {

    /**
     * Returns {@link Memory} buffer
     * @return {@link Memory} buffer
     * @see Memory
     */
    Memory<K, V> getBuffer();

    /**
     * Returns value associated with <code>key</code>
     * @param  key key of association
     * @return associated value wrapped in {@link Optional}.
     *         If there is no such value, returns {@link Optional#empty()}
     * @see    Optional
     */
    Optional<V> tryLoad(K key);

    /**
     * Creates or changes association <code>key</code>-><code>value</code> by discarding another association,
     * determined by implementation
     * @param  key   key of association
     * @param  value associated with <code>key</code> value
     * @return if association with <code>key</code> already existed, returns previous association,
     *         wrapped in {@link Optional},
     *         else if {@link Memory#isFull()} of buffer is <code>true</code> returns discarded association,
     *         wrapped in {@link Optional},
     *         else returns {@link Optional#empty()}
     * @see Memory
     * @see Optional
     */
    Optional<Entry<K, V>> keep(K key, V value);

    /**
     * Removes association with <code>key</code>
     * @param  key key of association
     * @return value of removed association, wrapped in {@link Optional}.
     *         If there is no such value, returns {@link Optional#empty()}
     * @see Optional
     */
    Optional<V> discard(K key);
}
