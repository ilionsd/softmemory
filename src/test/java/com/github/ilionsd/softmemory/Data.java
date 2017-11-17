package com.github.ilionsd.softmemory;

import java.util.*;

public interface Data {
    public static final long DEFAULT_CAPACITY = 10;
    public static final Set<Integer> INDEX_SET = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    public static final Set<Integer> NUMBER_OF_ZEROS_SET = new HashSet<>(
            Arrays.asList(
                    1,
                    10,
                    100,
                    1000,
                    10000,
                    100000,
                    1000000));
    public static final Set<Integer> REPEAT_DIGIT_SET = new HashSet<>(
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

    public static <K, V> Map<K, V> toMap(Set<K> keys, Set<V> vals) {
        Iterator<K> keysIt = keys.iterator();
        Iterator<V> valsIt = vals.iterator();
        Map<K, V> map = new HashMap<>();
        while (keysIt.hasNext() && valsIt.hasNext())
            map.put(keysIt.next(), valsIt.next());
        return map;
    }
}
