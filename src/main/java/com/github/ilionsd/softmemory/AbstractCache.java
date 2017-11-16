package com.github.ilionsd.softmemory;

import com.github.ilionsd.softmemory.replacementpolicy.ReplacementPolicy;

import java.io.Serializable;

public abstract class AbstractCache<K, V extends Serializable> implements Cache<K, V> {
    private Memory<K, V> memory;
    private ReplacementPolicy<K> replacementPolicy;

    @Override
    public Memory<K, V> getMemory() {
        return memory;
    }

    @Override
    public ReplacementPolicy<K> getReplacementPolicy() {
        return replacementPolicy;
    }

    @Override
    public void setReplacementPolicy(ReplacementPolicy<K> replacementPolicy) {
        this.replacementPolicy = replacementPolicy;
    }
}
