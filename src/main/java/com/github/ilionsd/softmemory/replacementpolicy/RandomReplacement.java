package com.github.ilionsd.softmemory.replacementpolicy;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class RandomReplacement<K> implements ReplacementPolicy<K> {
    private final Set<K> keySet = new HashSet<>();

    @Override
    public K replaceKey() {
        int index = ThreadLocalRandom.current().nextInt(0, keySet.size());
        Iterator<K> iterator = keySet.iterator();
        while (index-- != 0)
            iterator.next();
        K key = iterator.next();
        return key;
    }

    @Override
    public void discard(K key) {
        keySet.remove(key);
    }

    @Override
    public void used(K key) {
        keySet.add(key);
    }
}
