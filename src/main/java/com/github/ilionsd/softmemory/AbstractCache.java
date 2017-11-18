package com.github.ilionsd.softmemory;

import java.io.Serializable;

public abstract class AbstractCache<K, V extends Serializable> implements Cache<K, V> {
    private Memory<K, V> buffer;

    public AbstractCache(Memory<K, V> buffer) {
        this.buffer = buffer;
    }

    @Override
    public Memory<K, V> getBuffer() {
        return buffer;
    }

    protected abstract K replacementKey();
}
