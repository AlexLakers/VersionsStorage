package com.alex.versions_storage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

import com.alex.versions_storage.utill.IOutill;
import org.json.simple.parser.ParseException;

public class StorageManager {
    public static class ConstNames {
        public static final String SERVICE_DIR_NAME = "VersionsStorage";
        public static final String VERSIONS_FILE_NAME = "rootInfo.json";
        public static final String DB_NAME = "database_.dat";

    }


    private final Path rootPath;
    private final Path servicePath;
    private final Path infoPath;
    private int version;


    public StorageManager(Path rootpath) {
        if (rootpath == null) {
            throw new IllegalArgumentException("The path won't be null");
        }
        this.version=1;
        this.rootPath = rootpath;
        this.servicePath = rootPath.resolve(ConstNames.SERVICE_DIR_NAME);
        this.infoPath = servicePath.resolve(ConstNames.VERSIONS_FILE_NAME);
    }

    //This method changes the source data in the target root directory.
    private void changeRootData(RootData dir) throws IOException {

        //DIRECTORIES
        List<Path> pathsDirs = dir.getDirs();
        if (!pathsDirs.isEmpty()) {
            for (Path path : pathsDirs) {
                if (Files.notExists(path)) {
                    Files.createDirectory(path);
                }
            }
        }
        //FILES
        Map<String, byte[]> files = dir.getFiles();
        if (!files.isEmpty()) {
            for (Map.Entry<String, byte[]> file : files.entrySet()) {
                try (OutputStream out = Files.newOutputStream(
                        Path.of(file.getKey()),
                        StandardOpenOption.WRITE,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING)
                ) {
                    IOutill.writeData(file.getValue(), out);
                }
            }
        }

    }

    //This method creates a Service directory if it doesn't exist.
    // Then this method reads all the structure of directories and nested data from the root directory.
    // And then the data is saved to database file.Specific information such as a number of version and hashCode are saved to service json-file.
    public void createStorage() throws IOException {

        if (Files.notExists(servicePath)) {
            //creating a service dir
            Files.createDirectory(servicePath);
        }
        RootData rootData = new RootData(rootPath);
        RootInfo rootInfo = new RootInfo(rootPath);
        rootInfo.addNode(version, rootData.hashCode());
        //write data
        rootData.store(rootPath.resolve(ConstNames.DB_NAME));
        //write json-data
        rootInfo.store(infoPath);
    }




    //If a root directory and service json-file exists.Then this method reads all the structure of directories and nested data from the root directory.
    // After it the data is compared with the data from service json-file.
    // The comparing process is used hashCode.In dependence by result the user will see the message.
    public void compareData() throws IOException,ParseException{
        if (Files.exists(infoPath) && Files.exists(rootPath)) {
            RootData rootDir = new RootData(rootPath);
            RootInfo rootInfo = RootInfo.parseFromJSON(infoPath);
            version = rootInfo.findVersionByHash(rootDir.hashCode());
            if (version > 0) {
                System.out.println("The changes doesn't exist");
            } else {
                System.out.println("The changes exist");
            }
        } else {
            System.out.println("Some action maybe");

        }
    }


    //If a root directory and service json-file exists.Then this method reads all the structure of directories and nested data from the root directory.
    // And then the data is saved to database file.Specific information such as a number of version and hashCode are saved to service json-file.
    public void addData() throws ParseException,IOException{
            if (Files.exists(infoPath) && Files.exists(rootPath)) {
                RootData rootData = new RootData(rootPath);
                RootInfo rootInfo = RootInfo.parseFromJSON(infoPath);
                version = rootInfo.findVersionByHash(rootData.hashCode());
                if (version > 0) {
                    IOutill.writeConsole("Sorry,but the changes wasn't detected");
                } else {
                    version = rootInfo.getLastVersionDir() + 1;
                    rootInfo.addNode(version, rootData.hashCode());
                    rootInfo.store(infoPath);
                    rootData.store(rootPath.resolve(ConstNames.DB_NAME));

                }
            }
            }

    //If a service json-file and the database file exist.Then this method reads all the structure of directories and nested data from the root directory.
    // After it the data is compared with the data from service json-file.
    // The comparing process is used hashCode.If they don't equal each other, then the data which is stored in database file
    // will be saved in root directory with changes.
    public void restoreData() throws IOException,ParseException,ClassNotFoundException{
                Path dbPath = servicePath.resolve(".database.data");
                if (Files.exists(dbPath) && Files.exists(infoPath)) {
                    RootInfo rootInfo = RootInfo.parseFromJSON(infoPath);
                    if (rootInfo.isVersionExist(version)) {
                        RootData rootData = RootData.parseFromDB(dbPath);
                        changeRootData(rootData);
                    } else {
                        System.out.println("changes are not found");
                    }
                }
            }
        }




