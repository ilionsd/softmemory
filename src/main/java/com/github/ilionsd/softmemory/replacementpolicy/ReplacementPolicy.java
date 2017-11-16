package com.github.ilionsd.softmemory.replacementpolicy;

public interface ReplacementPolicy<K> {
    K replaceKey();
    void discard(K key);
    void used(K key);
}
