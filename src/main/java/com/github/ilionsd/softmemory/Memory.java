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
}
