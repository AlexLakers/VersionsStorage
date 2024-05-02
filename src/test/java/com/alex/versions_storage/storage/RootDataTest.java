package com.alex.versions_storage.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class RootDataTest {
    @TempDir
    static private Path tempDir;
    private static final String SERVICE_DIR = ".serviceDir";
    private RootData rootData;

    @BeforeEach
    void initRootData() throws IOException {
        rootData = new RootData(tempDir, SERVICE_DIR);
    }

    @ParameterizedTest
    @MethodSource("getInvalidPaths")
    void testConstructorRootData_shouldThrowIOException_whenPathNotFound(Path path) {
        Throwable thrown = Assertions.assertThrows(IOException.class, () -> new RootData(path, SERVICE_DIR));
        String expected = path.toString();

        String actual = thrown.getMessage();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testParseFromDB_whenPathValid_thenReturnCorrectRootData() throws IOException, ClassNotFoundException {
        Path tempDirTarget = tempDir.resolve("TargetDir");
        try (OutputStream outputStream = Files.newOutputStream(tempDirTarget);
             ObjectOutput out = new ObjectOutputStream(outputStream)) {
            out.writeObject(rootData);
        }
        RootData actual = RootData.loadFromDB(tempDirTarget);

        Assertions.assertEquals(rootData, actual);
    }

    @ParameterizedTest
    @MethodSource("getInvalidPaths")
    void testParseFromDB_shouldThrowIOException_whenPathNotFound(Path path) {
        Throwable thrown = Assertions.assertThrows(IOException.class, () -> RootData.loadFromDB(path));
        String expected = path.toString();

        String actual = thrown.getMessage();

        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getInvalidPaths")
    void testStore_shouldThrowIOException_whenPathNotFound(Path path) {
        Throwable thrown = Assertions.assertThrows(IOException.class, () -> rootData.store(path));
        String expected = path.toString();

        String actual = thrown.getMessage();

        Assertions.assertEquals(expected, actual);
    }

    static Stream<Arguments> getInvalidPaths() {
        return Stream.of(
                Arguments.of(Path.of("/invalidPath")),
                Arguments.of(Path.of("/1/2/4")),
                Arguments.of(Path.of("/etc/sh/0"))
        );
    }

    @Test
    void testStore_whenPathValid_thenStoreRootDataToPath() throws IOException, ClassNotFoundException {
        Path tempDirTarget = tempDir.resolve("TargetDir");
        RootData expected;

        rootData.store(tempDirTarget);
        try (InputStream inputStream = Files.newInputStream(tempDirTarget);
             ObjectInputStream in = new ObjectInputStream(inputStream)) {
            expected = (RootData) in.readObject();
        }
        Assertions.assertEquals(expected, rootData);
    }
}