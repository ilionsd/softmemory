package com.github.ilionsd.softmemory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class FileSystemMemory<K, V extends Serializable> implements Memory<K, V> {
    private long capacityL;
    private long sizeL;
    private Path storagePath;
    private ConcurrentMap<Object, String> keyFileNameMap;

    protected Path getFileByKey(Object key) {
        Path file;
        if (keyFileNameMap.containsKey(key))
            file = storagePath.resolve(keyFileNameMap.get(key));
        else {
            file = FileCreator.makeTempOrDie(storagePath, "", "");
            keyFileNameMap.put(key, file.toFile().getName());
        }
        return file;
    }

    protected Object read(Path file) {
        Object o = null;
        try (ObjectInputStream is = new ObjectInputStream( Files.newInputStream(file, StandardOpenOption.READ) )) {
            o = is.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
        return o;
    }

    protected void write(Path file, Object o) {
        try (ObjectOutputStream os = new ObjectOutputStream( Files.newOutputStream(file, StandardOpenOption.WRITE) )) {
            os.writeObject(o);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    protected void delete(Path file) {
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public long capacityL() {
        return capacityL;
    }

    @Override
    public long sizeL() {
        return sizeL;
    }

    @Override
    public int size() {
        if (Long.compare(Integer.MAX_VALUE, sizeL) < 0)
            return Integer.MAX_VALUE;
        return (int)sizeL;
    }

    @Override
    public boolean isEmpty() {
        return keyFileNameMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return keyFileNameMap.containsKey(key);
    }

    /**
     * to-do Later
     * @param value
     * @return
     */
    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        Path file = getFileByKey(key);
        return (V)read(file);
    }

    @Override
    public V put(K key, V value) {
        Path file = getFileByKey(key);
        write(file, value);
        return value;
    }

    @Override
    public synchronized V remove(Object key) {
        Path file = getFileByKey(key);
        V value = (V)read(file);
        keyFileNameMap.remove(key);
        delete(file);
        return value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }
}
