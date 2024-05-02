package com.alex.versions_storage.service;

import com.alex.versions_storage.exceptions.PathIncorrectException;
import com.alex.versions_storage.exceptions.ServiceFileStructureException;
import com.alex.versions_storage.storage.RootData;
import com.alex.versions_storage.storage.RootInfo;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@ExtendWith(MockitoExtension.class)
class StorageManagerTest {
    @TempDir
    static Path tempDir;
    static Path tempServiceDir;
    static Path tempServiceFile;
    static Path tempDatabaseFile;
    public static final String SERVICE_DIR = ".VersionsStorage";
    @Mock
    private StorageManager.FactoryHelper factoryHelper;
    @Mock
    RootData mockData;
    @Mock
    RootInfo mockInfo;
    @InjectMocks
    private StorageManager storageManager = new StorageManager(tempDir);

    @BeforeAll
    static void initServiceDir() throws IOException {
        tempServiceDir = tempDir.resolve(".VersionsStorage");
        tempServiceFile = tempServiceDir.resolve("rootInfo.json");
        tempDatabaseFile = tempServiceDir.resolve("database1.dat");
        Files.createDirectory(tempServiceDir);
        Files.createFile(tempServiceFile);
        Files.createFile(tempDatabaseFile);
        String jsonString = String.format("{\"rootPath\":\"%1$s\",\"versionsInfo\":[{\"hashCode\":343,\"version\":20}]}", tempDir.toString());
        try (BufferedWriter writer = Files.newBufferedWriter(tempServiceFile)) {
            writer.write(jsonString);
        }
    }

    @Test
    void createStorage_shouldCallAddNodeAndStoreMethods_whenPathIsFound() throws PathIncorrectException, IOException {
        Mockito.when(mockData.getVersion()).thenReturn(1);
        Mockito.when(factoryHelper.createRootInfo(Mockito.any(Path.class))).thenReturn(mockInfo);
        Mockito.when(factoryHelper.createRootData(Mockito.any(Path.class), Mockito.eq(SERVICE_DIR))).thenReturn(mockData);

        storageManager.createStorage();

        Mockito.verify(mockInfo).addNode(Mockito.eq(1), Mockito.anyInt());
        Mockito.verify(mockInfo).store(Mockito.any(Path.class));
        Mockito.verify(mockData).store(Mockito.any(Path.class));

    }

    @Test
    void addData_shouldCallAddNodeAndStoreMethods_whenChangesIsFound() throws IOException, PathIncorrectException, ServiceFileStructureException, ParseException {
        Mockito.when(mockInfo.getLastVersionDir()).thenReturn(1);
        Mockito.when(mockData.getHashCode()).thenReturn(132);
        Mockito.when(factoryHelper.createRootData(Mockito.any(Path.class), Mockito.eq(SERVICE_DIR))).thenReturn(mockData);
        Mockito.when(factoryHelper.loadRootInfo(Mockito.any(Path.class))).thenReturn(mockInfo);

        storageManager.addData();

        Mockito.verify(mockInfo).addNode(Mockito.eq(2), Mockito.anyInt());
        Mockito.verify(this.mockInfo).store(Mockito.any(Path.class));
        Mockito.verify(this.mockData).store(Mockito.any(Path.class));
    }

    @Test
    void addData_shouldThrowPathIncorrectException_whenIOException() throws IOException {
        Mockito.doThrow(IOException.class).when(factoryHelper).createRootData(Mockito.any(Path.class), Mockito.eq(SERVICE_DIR));
        String expected = String.format("The path [%1$s] is not found.", tempDir);

        Throwable thrown = Assertions.assertThrows(PathIncorrectException.class, () -> storageManager.addData());

        Assertions.assertEquals(expected, thrown.getMessage());
    }

    @Test
    void addData_shouldThrowServiceFileStructureException_whenParseException() throws ParseException, IOException {
        Mockito.doThrow(ParseException.class).when(factoryHelper).loadRootInfo(Mockito.any(Path.class));
        String expected = String.format("The service file [%1$s] have an incorrect structure", tempServiceFile);

        Throwable thrown = Assertions.assertThrows(ServiceFileStructureException.class, () -> storageManager.addData());

        Assertions.assertEquals(expected, thrown.getMessage());
    }

    @Test
    void restoreData_shouldCallLoadMethods_whenVersionExist() throws ParseException, IOException, ClassNotFoundException, PathIncorrectException, ServiceFileStructureException {
        Mockito.when(mockInfo.isVersionExist(1)).thenReturn(true);
        Mockito.when(factoryHelper.loadRootInfo(Mockito.any(Path.class))).thenReturn(mockInfo);
        Mockito.when(factoryHelper.loadRootData(Mockito.any(Path.class))).thenReturn(this.mockData);

        storageManager.restoreData(1);

        Mockito.verify(factoryHelper).loadRootData(Mockito.any(Path.class));
        Mockito.verify(factoryHelper).loadRootInfo(Mockito.any(Path.class));
    }

    @Test
    void restoreData_shouldThrowPathIncorrectException_whenIOException() throws IOException, ClassNotFoundException, ParseException {
        Mockito.when(mockInfo.isVersionExist(1)).thenReturn(true);
        Mockito.doThrow(IOException.class).when(factoryHelper).loadRootData(Mockito.any(Path.class));
        Mockito.when(factoryHelper.loadRootInfo(Mockito.any(Path.class))).thenReturn(mockInfo);
        String expected = String.format("The path [%1$s] is not found.", tempDir);

        Throwable thrown = Assertions.assertThrows(PathIncorrectException.class, () -> storageManager.restoreData(1));

        Assertions.assertEquals(expected, thrown.getMessage());
    }

    @Test
    void restoreData_shouldThrowServiceFileStructureException_whenParseException() throws ParseException, IOException {
        Mockito.doThrow(ParseException.class).when(factoryHelper).loadRootInfo(Mockito.any(Path.class));
        String expected = String.format("The service file [%1$s] have an incorrect structure", tempServiceFile);

        Throwable thrown = Assertions.assertThrows(ServiceFileStructureException.class, () -> storageManager.restoreData(1));

        Assertions.assertEquals(expected, thrown.getMessage());
    }
}