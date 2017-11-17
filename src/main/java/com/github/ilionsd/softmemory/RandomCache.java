package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class RandomCache<K, V extends Serializable> extends AbstractCache<K, V>  {

    public RandomCache(Memory<K, V> memory) {
        super(memory);
    }

    @Override
    protected K replacementKey() {
        long index = ThreadLocalRandom.current().nextLong(0L, getMemory().size());
        Iterator<K> iterator = getMemory().keySet().iterator();
        while (index-- != 0)
            iterator.next();
        return iterator.next();
    }

    @Override
    public Optional<V> tryLoad(K key) {
        return getMemory().load(key);
    }

    @Override
    public Optional<V> keep(K key, V value) {
        Optional<V> oValue = getMemory().load(key);
        if (!oValue.isPresent() && getMemory().isFull())
            oValue = discard(replacementKey());
        getMemory().store(key, value);
        return oValue;
    }

    @Override
    public Optional<V> discard(K key) {
        return getMemory().remove(key);
    }
}
