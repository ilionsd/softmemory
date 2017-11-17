package com.github.ilionsd.softmemory;

import java.io.Serializable;

public abstract class AbstractCache<K, V extends Serializable> implements Cache<K, V> {
    private Memory<K, V> memory;

    public AbstractCache(Memory<K, V> memory) {
        this.memory = memory;
    }

    @Override
    public Memory<K, V> getMemory() {
        return memory;
    }

    protected abstract K replacementKey();
}
