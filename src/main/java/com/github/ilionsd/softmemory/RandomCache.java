package com.github.ilionsd.softmemory;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomCache<K, V extends Serializable> extends AbstractCache<K, V>  {

    public RandomCache(Memory<K, V> buffer) {
        super(buffer);
    }

    @Override
    protected K replacementKey() {
        long index = ThreadLocalRandom.current().nextLong(0L, getBuffer().size());
        Iterator<K> iterator = getBuffer().keySet().iterator();
        while (index-- != 0)
            iterator.next();
        return iterator.next();
    }

    @Override
    public Optional<V> tryLoad(K key) {
        return getBuffer().load(key);
    }

    @Override
    public Optional<Entry<K, V>> keep(K key, V value) {
        Optional<V> oValue = getBuffer().load(key);
        K replacementKey = null;
        if (oValue.isPresent())
            replacementKey = key;
        else if (getBuffer().isFull()) {
            replacementKey = replacementKey();
            oValue = discard(replacementKey);
        }
        K replacedKey = replacementKey;
        getBuffer().store(key, value);
        return oValue.map(replacedValue -> new SimpleEntry<>(replacedKey, replacedValue));
    }

    @Override
    public Optional<V> discard(K key) {
        return getBuffer().remove(key);
    }
}
