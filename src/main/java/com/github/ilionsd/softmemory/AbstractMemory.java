package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Optional;

public abstract class AbstractMemory<K, V extends Serializable> implements Memory<K, V> {
    private long capacity;
    private long size = 0;

    public AbstractMemory(long capacity) {
        if (Long.compare(0, capacity) > 0)
            this.capacity = 0;
        else
            this.capacity = capacity;
    }

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
