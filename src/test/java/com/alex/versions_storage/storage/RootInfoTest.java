package com.alex.versions_storage.storage;


import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class RootInfoTest {
    @TempDir
    static Path tempDir;
    private RootInfo rootInfo;

    @BeforeEach
    void initRootInfo() {
        rootInfo = new RootInfo(tempDir);
    }

    @Test
    void toJSONString_shouldReturnStringOfJSON_whenAllOtherDataIsValid() {
        rootInfo.addNode(20, 343);
        String expected = String.format("{\"rootPath\":\"%1$s\",\"versionsInfo\":[{\"hashCode\":343,\"version\":20}]}", tempDir.toString());

        String actual = rootInfo.toJSONString();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getLastVersionDir_shouldReturnLasVersionRootDirectory_whenThisVersionIsAvailable() {
        rootInfo.addNode(10, 343);

        int actual = rootInfo.getLastVersionDir();

        Assertions.assertEquals(10, actual);
    }

    @Test
    void addNode_shouldAddNewNodeInNodes_whenArgsIsValid() {
        rootInfo.addNode(10, 343);

        Assertions.assertTrue(rootInfo.getNodes().size() == 1);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, 0})
    void addNode_shouldThrowIllegalArgumentException_whenVersionIsNotPositive(int ints) {
        Throwable thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> rootInfo.addNode(ints, 301));
        String expected = "You try to add a negative version";

        String actual = thrown.getMessage();

        Assertions.assertEquals(expected, actual);
    }


    @Test
    void findVersionByHash_shouldReturnActualVersion_whenHashCodeCorrespondsSomeVersion() {
        rootInfo.addNode(10, 343);

        int actual = rootInfo.findVersionByHash(343);

        Assertions.assertEquals(10, actual);
    }

    @Test
    void findVersionByHash_shouldReturnMinusOne_whenHashCodeNotCorrespondsSomeVersion() {
        rootInfo.addNode(10, 343);

        int actual = rootInfo.findVersionByHash(345);

        Assertions.assertEquals(-1, actual);
    }

    @Test
    void isVersionExist_shouldReturnTrue_whenVersionIsExist() {
        rootInfo.addNode(10, 343);

        boolean condition = rootInfo.isVersionExist(10);

        Assertions.assertTrue(condition);
    }

    @Test
    void isVersionExist_shouldReturnFalse_whenVersionIsNotExist() {
        rootInfo.addNode(10, 343);

        boolean condition = rootInfo.isVersionExist(11);

        Assertions.assertFalse(condition);
    }

    @Test
    void store_shouldStoreRootDataToPath_whenPathIsFound(@TempDir Path tempDir) throws IOException {
        Path tempPathTarget = tempDir.resolve("TargetJSON.json");
        String expected = rootInfo.toJSONString();

        rootInfo.store(tempPathTarget);
        String actual = "";
        try (BufferedReader reader = Files.newBufferedReader(tempPathTarget)) {
            while (reader.ready()) {
                actual += reader.readLine();
            }
        }

        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getInvalidPaths")
    void store_shouldThrowIOException_whenPathIsNotFound(Path pathDir) {
        Path invalidPath = pathDir.resolve("TargetJSON.json");
        String expected = invalidPath.toString();

        Throwable thrown = Assertions.assertThrows(IOException.class, () -> rootInfo.store(invalidPath));
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
    void load_shouldReturnRootInfo_whenPathIsFound() throws IOException, ParseException {
        Path tempPathTarget = tempDir.resolve("TargetJSON.json");
        String expected = rootInfo.toJSONString();
        try (BufferedWriter writer = Files.newBufferedWriter(tempPathTarget)) {
            writer.write(expected);
        }
        String actual = RootInfo.loadFromFile(tempPathTarget).toJSONString();

        Assertions.assertEquals(expected, actual);
    }
}