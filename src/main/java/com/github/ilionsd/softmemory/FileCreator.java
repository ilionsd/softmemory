package com.github.ilionsd.softmemory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Optional;

public class FileCreator {

    public static Path makeTempOrThrow(Path parentDirectory,
                                String prefix,
                                String suffix,
                                FileAttribute<?>... attrs) throws IOException {
        return Files.createTempFile(parentDirectory, prefix, suffix, attrs);
    }

    public static Optional<Path> makeOptionalTemp(Path parentDirectory,
                               String prefix,
                               String suffix,
                               FileAttribute<?>... attrs) {
        Optional<Path> file = Optional.empty();
        try {
            file = Optional.of(makeTempOrThrow(parentDirectory, prefix, suffix, attrs));
        } catch (IOException e) {
            //-- Nothing to do here --
        }
        return file;
    }

    public static Path makeTempOrDie(Path parentDirectory,
                              String prefix,
                              String suffix,
                              FileAttribute<?>... attrs) {
        Path file;
        try {
            file = makeTempOrThrow(parentDirectory, prefix, suffix, attrs);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
        return file;
    }

}
