package com.github.ilionsd.softmemory.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Optional;

public class TempDirectoryCreator {

    public static Path makeOrThrow(String prefix,
                                   FileAttribute<?>... attrs) throws IOException {
        Path directory = Files.createTempDirectory(prefix, attrs);
        directory.toFile().deleteOnExit();
        return directory;
    }

    public static Optional<Path> makeOptional(String prefix,
                                              FileAttribute<?>... attrs) {
        Optional<Path> directory = Optional.empty();
        try {
            directory = Optional.of(makeOrThrow(prefix, attrs));
        } catch (IOException e) {
            //-- Nothing to do here --
        }
        return directory;
    }

    public static Path makeOrDie(String prefix,
                                 FileAttribute<?>... attrs) {
        Path directory;
        try {
            directory = makeOrThrow(prefix, attrs);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
        return directory;
    }

}
