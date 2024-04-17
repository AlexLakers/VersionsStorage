package com.alex.versions_storage.provider;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

import com.alex.versions_storage.storage.RootData;
import com.alex.versions_storage.storage.RootInfo;
import com.alex.versions_storage.utill.IOUtill;
import org.json.simple.parser.ParseException;


public final class StorageManager {
    public static class ConstNames {
        public static final String SERVICE_DIR_NAME = ".VersionsStorage";
        public static final String VERSIONS_FILE_NAME = "rootInfo.json";
        public static final String DB_NAME = "database_.dat";

    }
    public static class ConstMsgs {
        public static final String CHANGES_NOT_FOUND = "A checking directory [%1$s] doesn't have unhandled changes.";
        public static final String CHANGES_FOUND = "A checking directory [%1$s] has an unhandled changes.%nYou can use 'add' to save all the changes.";
        public static final String SERVICE_FILE_NOT_FOUND = "The service file [%1$s] doesn't exists.%nMaybe you didn't use 'create' action earlier";


    }

    private RootInfo info;
    private RootData data;
    private final Path rootPath;
    private final Path servicePath;
    private final Path infoPath;


    public StorageManager(Path rootPath) {
        this.rootPath = rootPath;
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
        Map<String, byte[]> files = dir.getFiles();
        if (!files.isEmpty()) {
            for (Map.Entry<String, byte[]> file : files.entrySet()) {
                try (OutputStream out = Files.newOutputStream(
                        Path.of(file.getKey()),
                        StandardOpenOption.WRITE,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING)
                ) {
                    IOUtill.writeBytes(file.getValue(), out);
                }
            }
        }

    }

    //This method creates a Service directory if it doesn't exist.
    // Then this method reads all the structure of directories and nested data from the root directory.
    // And then the data is saved to database file.Specific information such as a number of version and hashCode are saved to service json-file.
    public void createStorage() throws IOException{
            if (Files.notExists(servicePath)) {
                Files.createDirectory(servicePath);
            }
            data = new RootData(rootPath);
            info = new RootInfo(rootPath);

            info.addNode(data.getVersion(), data.hashCode());
            data.store(newPathDBbyVersion(1));
            info.store(infoPath);


    }


    //If a root directory and service json-file exists.Then this method reads all the structure of directories and nested data from the root directory.
    // After it the data is compared with the data from service json-file.
    // The comparing process is used hashCode.In dependence by result the user will see the message.
    public void compareData() throws IOException,ParseException{
        /*try {*/
        int version = compareDataByHash(rootPath, infoPath);
        if (version > 0) {
            IOUtill.writeString(String.format(ConstMsgs.CHANGES_NOT_FOUND, rootPath));
        } else {
            IOUtill.writeString(String.format(ConstMsgs.CHANGES_FOUND, rootPath));
        }
    }
    //This method needs to compare data by hashCodes
    private int compareDataByHash(Path rootPath, Path infoPath) throws IOException,ParseException {
        int version=0;
            if (Files.exists(infoPath) && Files.exists(rootPath)) {

                data = new RootData(rootPath);
                info = RootInfo.load(infoPath);

               version = info.findVersionByHash(data.hashCode());
            } else {
              IOUtill.writeString(ConstMsgs.SERVICE_FILE_NOT_FOUND);
            }
        return version;

    }


    //If a root directory and service json-file exists.Then this method reads all the structure of directories and nested data from the root directory.
    // And then the data is saved to database file.Specific information such as a number of version and hashCode are saved to service json-file.
    public void addData() throws IOException,ParseException{
            int result = compareDataByHash(rootPath, infoPath);
            if (result > 0) {
                IOUtill.writeString(String.format(ConstMsgs.CHANGES_NOT_FOUND,rootPath));
            }
            else {
                int version = info.getLastVersionDir();

                info.addNode(version, data.hashCode());
                info.store(infoPath);
                data.store(newPathDBbyVersion(version));
            }


    }



    //If a service json-file and the database file exist.Then this method reads all the structure of directories and nested data from the root directory.
    // After it the data is compared with the data from service json-file.
    // The comparing process is used hashCode.If they don't equal each other, then the data which is stored in database file
    // will be saved in root directory with changes.
    public void restoreData(int version) throws IOException,ParseException,ClassNotFoundException {
        Path dbPath = newPathDBbyVersion(version);

            if (Files.exists(dbPath) && Files.exists(infoPath)) {
                info = RootInfo.load(infoPath);

                if (info.isVersionExist(version)) {
                    data = RootData.parseFromDB(dbPath);
                    changeRootData(data);
                } else {
                    IOUtill.writeString(ConstMsgs.CHANGES_NOT_FOUND);
                }

            } else {
                IOUtill.writeString(ConstMsgs.SERVICE_FILE_NOT_FOUND);
            }

    }


    public String toString() {
        return rootPath.toString();
    }

    //This function allows to form a new name of database by version of root directory.
    private Path newPathDBbyVersion(int version) {
        String[] tokens = ConstNames.DB_NAME.split("_");
        StringBuilder name = new StringBuilder();
        name.append(tokens[0])
                .append(version)
                .append(tokens[1]);
        Path dbPath = servicePath.resolve(name.toString());
        return dbPath;
    }



}




