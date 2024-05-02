package com.alex.versions_storage.service;

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
import com.alex.versions_storage.exceptions.PathIncorrectException;
import com.alex.versions_storage.exceptions.ServiceFileStructureException;

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
        public static final String DATABASE_NOT_FOUND = "The database [%1$s] doesn't exists.";
        public static final String CLASS_PATH_NOT_FOUND="The class path is wrong.";
        public static final String PATH_NOT_FOUND="The path [%1$s] is not found.";
        public static final String SERVICE_FILE_INVALID="The service file [%1$s] have an incorrect structure";
    }

    private RootInfo info;
    private RootData data;
    private final Path rootPath;
    private final Path servicePath;
    private final Path infoPath;
    private FactoryHelper factoryHelper;


    public StorageManager(Path rootPath) {
        this.rootPath = rootPath;
        this.servicePath = rootPath.resolve(ConstNames.SERVICE_DIR_NAME);
        this.infoPath = servicePath.resolve(ConstNames.VERSIONS_FILE_NAME);
        this.factoryHelper=new FactoryHelper();
    }
    public StorageManager(Path rootPath,FactoryHelper factoryHelper){
        this(rootPath);
        this.factoryHelper = factoryHelper;
    }
    static class FactoryHelper{
        RootInfo createRootInfo(Path rootPath){
            return new RootInfo(rootPath);
        }
        RootData createRootData(Path rootPath,String serviceDir)throws IOException{
            return new RootData(rootPath,serviceDir);
        }
        RootInfo loadRootInfo(Path path)throws IOException,ParseException{
            return RootInfo.loadFromFile(path);
        }
        RootData loadRootData(Path path)throws IOException,ClassNotFoundException{
            return RootData.loadFromDB(path);
        }

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
    public void createStorage() throws PathIncorrectException {
        try {
            if (Files.notExists(servicePath)) {
                Files.createDirectory(servicePath);
            }
            data= factoryHelper.createRootData(rootPath,ConstNames.SERVICE_DIR_NAME);
            info= factoryHelper.createRootInfo(rootPath);

            info.addNode(data.getVersion(), data.getHashCode());
            data.store(newPathDBbyVersion(data.getVersion()));
            info.store(infoPath);
        } catch (IOException ioExc) {
            throw new PathIncorrectException(String.format(ConstMsgs.PATH_NOT_FOUND,rootPath), ioExc);
        }
    }


    //If a root directory and service json-file exists.Then this method reads all the structure of directories and nested data from the root directory.
    // After it the data is compared with the data from service json-file.
    // The comparing process is used hashCode.In dependence by result the user will see the message.
    public void compareData() throws PathIncorrectException, ServiceFileStructureException {
        int version = compareDataByHash(rootPath, infoPath);
        if (version > 0) {
            IOUtill.writeString(String.format(ConstMsgs.CHANGES_NOT_FOUND, rootPath));
        } else {
            IOUtill.writeString(String.format(ConstMsgs.CHANGES_FOUND, rootPath));
        }
    }

    private int compareDataByHash(Path rootPath, Path infoPath) throws PathIncorrectException, ServiceFileStructureException {
        try {
            if (Files.exists(infoPath) && Files.exists(rootPath)) {

                data=factoryHelper.createRootData(rootPath,ConstNames.SERVICE_DIR_NAME);
                info = factoryHelper.loadRootInfo(infoPath);

                return info.findVersionByHash(data.getHashCode());
            } else {
                throw new PathIncorrectException(String.format(ConstMsgs.SERVICE_FILE_NOT_FOUND,infoPath));
            }
        } catch (IOException ioExc) {
            throw new PathIncorrectException(String.format(ConstMsgs.PATH_NOT_FOUND,rootPath), ioExc);
        } catch (ParseException parseExc) {
            throw new ServiceFileStructureException(String.format(ConstMsgs.SERVICE_FILE_INVALID,infoPath), parseExc);
        }
    }

    //If a root directory and service json-file exists.Then this method reads all the structure of directories and nested data from the root directory.
    // And then the data is saved to database file.Specific information such as a number of version and hashCode are saved to service json-file.
    public void addData() throws PathIncorrectException, ServiceFileStructureException {
        try {
            int result = compareDataByHash(rootPath, infoPath);
            if (result > 0) {
                IOUtill.writeString(String.format(ConstMsgs.CHANGES_NOT_FOUND,rootPath));
            }
            else {
                int version = info.getLastVersionDir()+1;

                info.addNode(version, data.getHashCode());
                info.store(infoPath);
                data.store(newPathDBbyVersion(version));
            }
        } catch (IOException ioExc) {
            throw new PathIncorrectException(String.format(ConstMsgs.PATH_NOT_FOUND,rootPath), ioExc);
        }

    }

    //If a service json-file and the database file exist.Then this method reads all the structure of directories and nested data from the root directory.
    // After it the data is compared with the data from service json-file.
    // The comparing process is used hashCode.If they don't equal each other, then the data which is stored in database file
    // will be saved in root directory with changes.
    public void restoreData(int version) throws PathIncorrectException, ServiceFileStructureException {
        Path dbPath = newPathDBbyVersion(version);
        try {
            if (Files.exists(dbPath) && Files.exists(infoPath)) {
                info=factoryHelper.loadRootInfo(infoPath);

                if (info.isVersionExist(version)) {
                    data=factoryHelper.loadRootData(dbPath);
                    changeRootData(data);
                } else {
                    IOUtill.writeString(String.format(ConstMsgs.CHANGES_NOT_FOUND,rootPath));
                }
            } else {
                throw new PathIncorrectException(String.format(ConstMsgs.DATABASE_NOT_FOUND, dbPath));
            }
        } catch (ClassNotFoundException cnfe) {
            throw new PathIncorrectException(ConstMsgs.CLASS_PATH_NOT_FOUND, cnfe);
        } catch (IOException ioExc) {
            throw new PathIncorrectException(String.format(ConstMsgs.PATH_NOT_FOUND,rootPath), ioExc);
        } catch (ParseException parseExc) {
            throw new ServiceFileStructureException(String.format(ConstMsgs.SERVICE_FILE_INVALID,infoPath), parseExc);
        }
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




