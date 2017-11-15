package com.github.ilionsd.softmemory;

import java.util.Optional;

public interface DiscardPolicy<K> {
    K discardKey();
    void discard(K key);
    void used(K key);
}
