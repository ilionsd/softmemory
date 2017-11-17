package com.github.ilionsd.softmemory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class CacheTest extends AbstractTest<Integer, Integer, Cache<Integer, Integer>> {

    Cache<Integer, Integer> cache;
    Class<Memory<Integer, Integer>> memoryClazz;

    @Parameterized.Parameters(name = "{index}: {0} with buffer of {1}")
    public static Collection<Object[]> testCases() {
        List<Object[]> list = new ArrayList<Object[]>();
        list.add( new Object[]{
                RandomCache.class,
                RandomAccessMemory.class,
                Data.DEFAULT_CAPACITY,
                Data.toMap(Data.INDEX_SET, Data.NUMBER_OF_ZEROS_SET),
                Data.toMap(Data.REPEAT_DIGIT_SET, Data.REPEAT_DIGIT_SET) });
        return list;
    }

    public CacheTest(Class<Cache<Integer, Integer>> clazz,
                      Class<Memory<Integer, Integer>> memoryClazz,
                      long memoryCapacity,
                      Map<Integer, Integer> iData,
                      Map<Integer, Integer> aData) {
        super(clazz, memoryCapacity, iData, aData);
        this.memoryClazz = memoryClazz;
    }

    @Before
    public void init() {
        try {
            Memory<Integer, Integer> memoryInstance = memoryClazz
                    .getConstructor(long.class)
                    .newInstance(memoryCapacity);
            cache = clazz
                    .getConstructor(Memory.class)
                    .newInstance(memoryInstance);
        } catch (NoSuchMethodException
                | IllegalAccessException
                | InstantiationException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        for (Map.Entry<Integer, Integer> entry : iData.entrySet()) {
            cache.keep(entry.getKey(), entry.getValue());
        }
    }

    @Test
    public void cacheHit() {
        for (Map.Entry<Integer, Integer> entry : iData.entrySet()) {
            Optional<Integer> oValue = cache.tryLoad(entry.getKey());
            assertTrue("Workaround Optional<T> to compare with <T>",
                    oValue.map(value -> Integer.compare(value, entry.getValue()) == 0).orElse(false));
        }
    }

    @Test
    public void cacheMiss() {
        for (Map.Entry<Integer, Integer> entry : aData.entrySet()) {
            Optional<Integer> oValue = cache.tryLoad(entry.getKey());
            assertFalse("Workaround Optional<T> to compare with <T>",
                    oValue.map(value -> Integer.compare(value, entry.getValue()) == 0).orElse(false));
        }
    }

    @Test
    public void keep() {
        Optional<Integer> oValue;
        Iterator<Map.Entry<Integer, Integer>> iterator = aData.entrySet().iterator();
        do {
            Map.Entry<Integer, Integer> entry = iterator.next();
            oValue = cache.keep(entry.getKey(), entry.getValue());
        } while (!oValue.isPresent());
        do {
            Map.Entry<Integer, Integer> entry = iterator.next();
            oValue = cache.keep(entry.getKey(), entry.getValue());
        } while (iterator.hasNext());
    }
}
