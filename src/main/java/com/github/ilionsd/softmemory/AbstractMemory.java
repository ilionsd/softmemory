package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Optional;

public abstract class AbstractMemory<K, V extends Serializable> implements Memory<K, V> {
    private long capacityL;
    private long sizeL;

    @Override
    public long capacityL() {
        return capacityL;
    }

    @Override
    public long sizeL() {
        return sizeL;
    }

    protected synchronized void incrementSizeL() {
        if (isFull())
            throw new OutOfMemoryError("Size of used memory reached capacity and cant be enlarged!");
        else
            ++sizeL;
    }

    protected synchronized void decrementSizeL() {
        --sizeL;
    }
}
