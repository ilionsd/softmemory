package com.github.ilionsd.softmemory;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class MemoryTest {
    public static final long DEFAULT_CAPACITY = 10;

    private static Set<Integer> indexSet;
    private static Set<Integer> numberOfZerosSet;
    private static Set<Integer> repeatDigitsSet;

    private long memoryCapacity = 10;

    private Map<Integer, Integer> initialData;
    private Map<Integer, Integer> additionalData;

    private Class<Memory<Integer, Integer>> clazz;
    private Memory<Integer, Integer> memory;

    public static <K, V> Map<K, V> toMap(Set<K> keys, Set<V> vals) {
        Iterator<K> keysIt = keys.iterator();
        Iterator<V> valsIt = vals.iterator();
        Map<K, V> map = new HashMap<>();
        while (keysIt.hasNext() && valsIt.hasNext())
            map.put(keysIt.next(), valsIt.next());
        return map;
    }

    @Parameterized.Parameters(name = "{index}: {0} with capacity {1}")
    public static Collection<Object[]> testCases() {
        initialize();
        List<Object[]> list = new ArrayList<Object[]>();
        list.add( new Object[]{
                RandomAccessMemory.class,
                DEFAULT_CAPACITY,
                toMap(indexSet, numberOfZerosSet),
                toMap(repeatDigitsSet, repeatDigitsSet) });
        list.add( new Object[]{
                FileSystemMemory.class,
                DEFAULT_CAPACITY,
                toMap(indexSet, numberOfZerosSet),
                toMap(repeatDigitsSet, repeatDigitsSet) });
        return list;
    }

    public MemoryTest(Class clazz,
                      long memoryCapacity,
                      Map<Integer, Integer> initialData,
                      Map<Integer, Integer> additionalData) {
        this.clazz = clazz;
        this.memoryCapacity = memoryCapacity;
        this.initialData = initialData;
        this.additionalData = additionalData;
    }

    public static void initialize() {
        indexSet = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        numberOfZerosSet = new HashSet<>(
                Arrays.asList(
                        1,
                        10,
                        100,
                        1000,
                        10000,
                        100000,
                        1000000));
        repeatDigitsSet = new HashSet<>(
                Arrays.asList(
                        1111111,
                        2222222,
                        3333333,
                        4444444,
                        5555555,
                        6666666,
                        7777777,
                        8888888,
                        9999999));
    }

    @Before
    public void memoryInit() {
        try {
            memory = clazz.getConstructor(long.class).newInstance(memoryCapacity);
        } catch (NoSuchMethodException
                | IllegalAccessException
                | InstantiationException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        memory.storeAll(initialData);
    }

    @Test
    public void size() {
        assertEquals((long)initialData.size(), memory.size());
    }

    @Test
    public void load() {
        for (Map.Entry<Integer, Integer> entry : initialData.entrySet()) {
            Optional<Integer> oValue = memory.load(entry.getKey());
            assertTrue("Workaround Optional<T> to compare with <T>",
                    oValue.map(value -> Integer.compare(value, entry.getValue()) == 0).orElse(false));
        }
    }

    @Test(expected = OutOfMemoryError.class)
    public void store() {
        for (Map.Entry<Integer, Integer> entry : additionalData.entrySet()) {
            Optional<Integer> oValue;

            oValue = memory.store(entry.getKey(), entry.getValue());
            assertFalse("Workaround Optional<T> to compare with <T>",
                    oValue.isPresent());

            oValue = memory.load(entry.getKey());
            assertTrue("Workaround Optional<T> to compare with <T>",
                    oValue.isPresent());
        }
    }

    @Test
    public void remove() {
        for (Map.Entry<Integer, Integer> entry : initialData.entrySet()) {
            Optional<Integer> oValue;

            oValue = memory.load(entry.getKey());
            assertTrue("Workaround Optional<T> to compare with <T>",
                    oValue.isPresent());

            oValue = memory.remove(entry.getKey());
            assertTrue("Workaround Optional<T> to compare with <T>",
                    oValue.map(value -> Integer.compare(value, entry.getValue()) == 0).orElse(false));

            oValue = memory.load(entry.getKey());
            assertFalse("Workaround Optional<T> to compare with <T>",
                    oValue.isPresent());
        }
    }
}
