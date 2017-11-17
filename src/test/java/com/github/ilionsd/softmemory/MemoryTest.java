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
public class MemoryTest extends AbstractTest<Integer, Integer, Memory<Integer, Integer>> {
    protected Memory<Integer, Integer> memory;

    @Parameterized.Parameters(name = "{index}: {0} with capacity {1}")
    public static Collection<Object[]> testCases() {
        List<Object[]> list = new ArrayList<Object[]>();
        list.add( new Object[]{
                RandomAccessMemory.class,
                Data.DEFAULT_CAPACITY,
                Data.toMap(Data.INDEX_SET, Data.NUMBER_OF_ZEROS_SET),
                Data.toMap(Data.REPEAT_DIGIT_SET, Data.REPEAT_DIGIT_SET) });
        list.add( new Object[]{
                FileSystemMemory.class,
                Data.DEFAULT_CAPACITY,
                Data.toMap(Data.INDEX_SET, Data.NUMBER_OF_ZEROS_SET),
                Data.toMap(Data.REPEAT_DIGIT_SET, Data.REPEAT_DIGIT_SET) });
        return list;
    }

    public MemoryTest(Class<Memory<Integer, Integer>> clazz,
                      long memoryCapacity,
                      Map<Integer, Integer> iData,
                      Map<Integer, Integer> aData) {
        super(clazz, memoryCapacity, iData, aData);
    }

    @Before
    public void init() {
        try {
            memory = clazz.getConstructor(long.class).newInstance(memoryCapacity);
        } catch (NoSuchMethodException
                | IllegalAccessException
                | InstantiationException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        memory.storeAll(iData);
    }

    @Test
    public void size() {
        assertEquals((long)iData.size(), memory.size());
    }

    @Test(expected = OutOfMemoryError.class)
    public void isFull() {
        Iterator<Map.Entry<Integer, Integer>> iterator = aData.entrySet().iterator();
        Map.Entry<Integer, Integer> entry;
        while (!memory.isFull()) {
            assertFalse(memory.isFull());
            entry = iterator.next();
            memory.store(entry.getKey(), entry.getValue());
        }
        assertTrue(memory.isFull());
        entry = iterator.next();
        memory.store(entry.getKey(), entry.getValue());
    }

    @Test
    public void load() {
        for (Map.Entry<Integer, Integer> entry : iData.entrySet()) {
            Optional<Integer> oValue = memory.load(entry.getKey());
            assertTrue("Workaround Optional<T> to compare with <T>",
                    oValue.map(value -> Integer.compare(value, entry.getValue()) == 0).orElse(false));
        }
    }

    @Test(expected = OutOfMemoryError.class)
    public void store() {
        for (Map.Entry<Integer, Integer> entry : aData.entrySet()) {
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
        for (Map.Entry<Integer, Integer> entry : iData.entrySet()) {
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
