package com.github.ilionsd.softmemory;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RunWith(Parameterized.class)
public class CachedMemoryTest extends MemoryTest {
    protected long bufferCapacity;
    protected Class<Memory<Integer, Integer>> bufferClazz;
    protected Class<Cache<Integer, Integer>> cacheClazz;
    protected Class<Memory<Integer, Integer>> memoryClazz;

    @Parameterized.Parameters(name = "{index}: {0} writing policy with memory {1}, {2} replacing policy and buffer {3}")
    public static Collection<Object[]> testCases() {
        List<Object[]> list = new ArrayList<Object[]>();
        list.add( new Object[]{
                WriteThroughCachedMemory.class,
                RandomAccessMemory.class,
                RandomCache.class,
                RandomAccessMemory.class,
                Data.DEFAULT_CAPACITY,
                Data.DEFAULT_CAPACITY / 2,
                Data.toMap(Data.INDEX_SET, Data.NUMBER_OF_ZEROS_SET),
                Data.toMap(Data.REPEAT_DIGIT_SET, Data.REPEAT_DIGIT_SET) });
        list.add( new Object[]{
                WriteThroughCachedMemory.class,
                FileSystemMemory.class,
                RandomCache.class,
                RandomAccessMemory.class,
                Data.DEFAULT_CAPACITY,
                Data.DEFAULT_CAPACITY / 2,
                Data.toMap(Data.INDEX_SET, Data.NUMBER_OF_ZEROS_SET),
                Data.toMap(Data.REPEAT_DIGIT_SET, Data.REPEAT_DIGIT_SET) });
        return list;
    }

    public CachedMemoryTest(Class<Memory<Integer, Integer>> clazz,
                            Class<Memory<Integer, Integer>> memoryClazz,
                            Class<Cache<Integer, Integer>> cacheClazz,
                            Class<Memory<Integer, Integer>> bufferClazz,
                            long memoryCapacity,
                            long bufferCapacity,
                            Map<Integer, Integer> iData,
                            Map<Integer, Integer> aData) {
        super(clazz, memoryCapacity, iData, aData);
        this.memoryClazz = memoryClazz;
        this.cacheClazz = cacheClazz;
        this.bufferClazz = bufferClazz;
        this.bufferCapacity = bufferCapacity;
    }

    @Before
    public void init() {
        try {
            Memory<Integer, Integer> buffer = bufferClazz
                    .getConstructor(long.class)
                    .newInstance(bufferCapacity);
            Cache<Integer, Integer> cache = cacheClazz
                    .getConstructor(Memory.class)
                    .newInstance(buffer);
            Memory<Integer, Integer> mainMemory = memoryClazz
                    .getConstructor(long.class)
                    .newInstance(memoryCapacity);
            memory = clazz.getConstructor(Memory.class, Cache.class).newInstance(mainMemory, cache);
        } catch (NoSuchMethodException
                | IllegalAccessException
                | InstantiationException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        memory.storeAll(iData);
    }
}
