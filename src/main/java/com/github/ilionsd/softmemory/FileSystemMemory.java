package com.github.ilionsd.softmemory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class FileSystemMemory<K, V extends Serializable> extends AbstractMemory<K, V> {
    private Path storagePath;
    private ConcurrentMap<Object, String> keyFileNameMap;

    protected Path getFileByKey(Object key) {
        Path file;
        if (keyFileNameMap.containsKey(key))
            file = storagePath.resolve(keyFileNameMap.get(key));
        else synchronized (this){
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
    public Optional<V> load(Object key) {
        Path file = getFileByKey(key);
        return Optional.ofNullable( (V)read(file) );
    }

    @Override
    public void store(K key, V value) {
        if (!keyFileNameMap.containsKey(key))
            incrementSizeL();
        Path file = getFileByKey(key);
        write(file, value);
    }

    @Override
    public Optional<V> discard(Object key) {
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
}
