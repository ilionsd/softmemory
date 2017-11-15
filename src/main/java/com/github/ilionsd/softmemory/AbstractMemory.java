package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Optional;

public abstract class AbstractMemory<K, V extends Serializable> implements Memory<K, V> {
    private long capacity;
    private long size;

    @Override
    public long capacity() {
        return capacity;
    }

    @Override
    public long size() {
        return size;
    }

    protected synchronized void incrementSizeL() {
        if (isFull())
            throw new OutOfMemoryError("Size of used memory reached capacity and cant be enlarged!");
        else
            ++size;
    }

    protected synchronized void decrementSizeL() {
        --size;
    }
}
