package com.github.ilionsd.softmemory;

import com.github.ilionsd.softmemory.util.TempDirectoryCreator;
import com.github.ilionsd.softmemory.util.TempFileCreator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FileSystemMemory<K, V extends Serializable> extends AbstractMemory<K, V> {
    public static final long DEFAULT_CAPACITY = 10000L;

    private Path storagePath;
    private ConcurrentMap<K, String> keyFileNameMap;

    public FileSystemMemory() {
        this(DEFAULT_CAPACITY, TempDirectoryCreator.makeOrDie(""));
    }

    public FileSystemMemory(long capacity) {
        this(capacity, TempDirectoryCreator.makeOrDie(""));
    }

    public FileSystemMemory(Path storagePath) {
        this(DEFAULT_CAPACITY, storagePath);
    }

    public FileSystemMemory(long capacity, Path storagePath) {
        super(capacity);
        this.keyFileNameMap = new ConcurrentHashMap<>();
        this.storagePath = storagePath;
    }

    protected Path getFileByKey(K key) {
        Path file;
        if (keyFileNameMap.containsKey(key))
            file = storagePath.resolve(keyFileNameMap.get(key));
        else synchronized (this){
            file = TempFileCreator.makeOrDie(storagePath, "", "");
            keyFileNameMap.put(key, file.toFile().getName());
        }
        return file;
    }

    protected Object read(Path file) {
        Object o = null;
        try (ObjectInputStream is = new ObjectInputStream( Files.newInputStream(file, StandardOpenOption.READ) )) {
            o = is.readObject();
        } catch (ClassNotFoundException | IOException e) {
            //throw new RuntimeException(e.getMessage(), e.getCause());
        }
        return o;
    }

    protected void write(Path file, Object o) {
        try (ObjectOutputStream os = new ObjectOutputStream( Files.newOutputStream(file, StandardOpenOption.WRITE) )) {
            synchronized (this) {
                os.writeObject(o);
            }
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
    public Optional<V> load(K key) {
        Path file = getFileByKey(key);
        return Optional.ofNullable( (V)read(file) );
    }

    @Override
    public Optional<V> store(K key, V value) {
        Optional<V> oValue = load(key);
        if (!oValue.isPresent())
            incrementSizeL();
        Path file = getFileByKey(key);
        write(file, value);
        return oValue;
    }

    @Override
    public Optional<V> remove(K key) {
        Path file = getFileByKey(key);
        Optional<V> value = Optional.ofNullable( (V)read(file) );
        if (value.isPresent()) {
            synchronized (this) {
                keyFileNameMap.remove(key);
                delete(file);
            }
            decrementSizeL();
        }
        return value;
    }

    @Override
    public Set<K> keySet() {
        return keyFileNameMap.keySet();
    }
}
