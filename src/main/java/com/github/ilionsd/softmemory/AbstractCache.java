package com.github.ilionsd.softmemory;

import java.io.Serializable;

public abstract class AbstractCache<K, V extends Serializable> implements Cache<K, V> {
    private Memory<K, V> memory;
    private DiscardPolicy<K> discardPolicy;

    @Override
    public Memory<K, V> getMemory() {
        return memory;
    }

    @Override
    public DiscardPolicy<K> getDiscardPolicy() {
        return discardPolicy;
    }

    @Override
    public void setDiscardPolicy(DiscardPolicy<K> discardPolicy) {
        this.discardPolicy = discardPolicy;
    }
}
