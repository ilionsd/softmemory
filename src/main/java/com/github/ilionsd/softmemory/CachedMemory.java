package com.github.ilionsd.softmemory;

import java.io.Serializable;

/**
 * Extension of {@link Memory}
 * with realization of <a href="https://en.wikipedia.org/wiki/Cache_(computing)#Writing_policies">writing policies</a> over
 * {@link Memory} memory and caching {@link Cache} buffer
 * @param <K> Key type of <code>memory</code> and <code>cache</code>
 * @param <V> Value type of <code>memory</code> and <code>cache</code>
 */
public interface CachedMemory<K, V extends Serializable> extends Memory<K, V> {

    /**
     * Returns {@link Cache} cache
     * @return {@link Cache} cache
     * @see Cache
     */
    Cache<K, V> getCache();

    /**
     * Sets <code>cache</code>
     * @param cache
     * @see Cache
     */
    void setCache(Cache<K, V> cache);

    /**
     * Returns {@link Memory} buffer
     * @return {@link Memory} buffer
     * @see Memory
     */
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
