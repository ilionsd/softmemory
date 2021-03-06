package com.github.ilionsd.softmemory.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Optional;

public class TempFileCreator {

    public static Path makeOrThrow(Path parentDirectory,
                                   String prefix,
                                   String suffix,
                                   FileAttribute<?>... attrs) throws IOException {
        return Files.createTempFile(parentDirectory, prefix, suffix, attrs);
    }

    public static Optional<Path> makeOptional(Path parentDirectory,
                                              String prefix,
                                              String suffix,
                                              FileAttribute<?>... attrs) {
        Optional<Path> file = Optional.empty();
        try {
            file = Optional.of(makeOrThrow(parentDirectory, prefix, suffix, attrs));
        } catch (IOException e) {
            //-- Nothing to do here --
        }
        return file;
    }

    public static Path makeOrDie(Path parentDirectory,
                                 String prefix,
                                 String suffix,
                                 FileAttribute<?>... attrs) {
        Path file;
        try {
            file = makeOrThrow(parentDirectory, prefix, suffix, attrs);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
        return file;
    }

}
