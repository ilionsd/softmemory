package com.github.ilionsd.softmemory;

import org.junit.Before;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public abstract class AbstractTest<K, V, T> {
    protected long memoryCapacity = 10;

    protected Map<K, V> iData;
    protected Map<K, V> aData;

    protected Class<T> clazz;

    public AbstractTest(Class<T> clazz,
                      long memoryCapacity,
                      Map<K, V> iData,
                      Map<K, V> aData) {
        this.clazz = clazz;
        this.memoryCapacity = memoryCapacity;
        this.iData = iData;
        this.aData = aData;
    }
}
