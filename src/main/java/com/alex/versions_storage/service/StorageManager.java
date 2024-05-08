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

/**
 * It describes the functions provider for the versions storage.
 * You can use the follow functional:creating versions storage,adding data to the versions storage,
 * comparing data and restoring data.
 * @see RootInfo RootInfo.
 * @see RootData RootData.
 */
public final class StorageManager {
    /**
     * It contains the main names that are used in the version storage.
     */
    public static class ConstNames {
        public static final String SERVICE_DIR_NAME = ".VersionsStorage";
        public static final String VERSIONS_FILE_NAME = "rootInfo.json";
        public static final String DB_NAME = "database_.dat";

    }

    /**
     * It contains the formatted messages that are used in the version storage.
     * For example,they are used for describing some exceptions and other information.
     */
    public static class ConstMsgs {
        public static final String CHANGES_NOT_FOUND = "A checking directory [%1$s] doesn't have unhandled changes.";
        public static final String CHANGES_FOUND = "A checking directory [%1$s] has an unhandled changes.%nYou can use 'add' to save all the changes.";
        public static final String SERVICE_FILE_NOT_FOUND = "The service file [%1$s] doesn't exists.%nMaybe you didn't use 'create' action earlier";
        public static final String DATABASE_NOT_FOUND = "The database [%1$s] doesn't exists.";
        public static final String CLASS_PATH_NOT_FOUND = "The class path is wrong.";
        public static final String PATH_NOT_FOUND = "The path [%1$s] is not found.";
        public static final String SERVICE_FILE_INVALID = "The service file [%1$s] have an incorrect structure";

    }

    private RootInfo info;
    private RootData data;
    private final Path rootPath;
    private final Path servicePath;
    private final Path infoPath;
    private FactoryHelper factoryHelper;

    /**
     * Creates storage manager with a path to selected root directory.
     *
     * @param rootPath path to directory.
     */
    public StorageManager(Path rootPath) {
        this.rootPath = rootPath;
        this.servicePath = rootPath.resolve(ConstNames.SERVICE_DIR_NAME);
        this.infoPath = servicePath.resolve(ConstNames.VERSIONS_FILE_NAME);
        this.factoryHelper = new FactoryHelper();
    }

    /**
     * Creates new StorageManager with a path to selected root directory.
     * You need to give a factory helper additionally.
     * That's necessary for the mocking process(Unit tests).
     *
     * @param rootPath      path to directory.
     * @param factoryHelper factory helper for tests.
     */
    public StorageManager(Path rootPath, FactoryHelper factoryHelper) {
        this(rootPath);
        this.factoryHelper = factoryHelper;
    }

    /**
     * This factory describes some methods for creating parts of storage by another ways.
     * It is necessary for unit tests.
     */
    static class FactoryHelper {
        RootInfo createRootInfo(Path rootPath) {
            return new RootInfo(rootPath);
        }

        RootData createRootData(Path rootPath, String serviceDir) throws IOException {
            return new RootData(rootPath, serviceDir);
        }

        RootInfo loadRootInfo(Path path) throws IOException, ParseException {
            return RootInfo.loadFromFile(path);
        }

        RootData loadRootData(Path path) throws IOException, ClassNotFoundException {
            return RootData.loadFromDB(path);
        }

    }

    /**
     * Perform the changing source data algorithm with using data and folders structure that are available in the {@link RootData RootData.class}.
     * The source data which will be changed is located in the selected root directory.
     * <b>Algorithm:</b>Firstly,occurs iterating the list of nested directories and if some directory doesn't exist then it will be created.
     * Secondary,after restoring directories occurs writing data(bytes) to all the files in each a nested directory.
     * If the file exists then occurs the changing data in it.In another way occurs creating file with the necessary content.
     *
     * @param dir part of storage that's contains structure of folders and all the files int it.
     * @throws IOException if not some IO error has been detected.For example,the using path is incorrect.
     * @see IOUtill  IOUtill
     */
    private void changeRootData(RootData dir) throws IOException {

        //Directories
        List<Path> pathsDirs = dir.getDirs();
        if (!pathsDirs.isEmpty()) {
            for (Path path : pathsDirs) {
                if (Files.notExists(path)) {
                    Files.createDirectory(path);
                }
            }
        }
        //Files
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

    /**
     * Performs the creating all the necessary files and directories for versions storage(rootInfo.json,database.dat,.VersionsStorage).
     * Firstly, reads all the structure of directories and nested data from the selected root directory.
     * Secondary, creates the directory for the versions storage(.VersionsStorage).
     * And then the data is saved(serialize) to versions storage(database.dat).
     * Specific information such as a number of version and hash code are saved to versions storage(rootInfo.json).
     *
     * @throws PathIncorrectException if path to selected root directory is not found or other IO errors have been detected.
     */
    public void createStorage() throws PathIncorrectException {
        try {
            if (Files.notExists(servicePath)) {
                Files.createDirectory(servicePath);
            }
            data = factoryHelper.createRootData(rootPath, ConstNames.SERVICE_DIR_NAME);
            info = factoryHelper.createRootInfo(rootPath);

            info.addNode(data.getVersion(), data.getHashCode());
            data.store(newPathDBbyVersion(data.getVersion()));
            info.store(infoPath);
        } catch (IOException ioExc) {
            throw new PathIncorrectException(String.format(ConstMsgs.PATH_NOT_FOUND, rootPath), ioExc);
        }

    }

    /**
     * Performs the comparing data between the selected root directory and the versions storage(rootInfo.json).
     * The comparing data by hash code occurs with using algorithm that's realised in the {@link #compareDataByHash(Path, Path) CompareDataByHash}.
     * And then if changes in the source selected directory were not found(version's value equals -1) we will see the information message about some changes are found.
     * If any changes were detected we will see another information message about some changes are not found.
     *
     * @throws PathIncorrectException        if path to selected root directory or some paths to versions storage are not found or other IO errors have been detected.
     * @throws ServiceFileStructureException if the versions storage(rootInfo.json) has incorrect structure.
     */
    public void compareData() throws PathIncorrectException, ServiceFileStructureException {
        int version = compareDataByHash(rootPath, infoPath);
        if (version > 0) {
            IOUtill.writeString(String.format(ConstMsgs.CHANGES_NOT_FOUND, rootPath));
        } else {
            IOUtill.writeString(String.format(ConstMsgs.CHANGES_FOUND, rootPath));
        }
    }

    /**
     * Returns the version number of changes data in the selected root directory.
     * The comparing data occurs between the selected directory and the versions storage(rootInfo.json) by hash code.
     * The version takes from the versions storage(rootInfo.json) by hash code if it possible and versions storage(database.dat,rootInfo.json) exist.
     * <Strong>Algorithm:</Strong>Firstly occurs search an available version in the versions storage(rootInfo.json) by hash code of data in the selected root directory.
     * And then if hash code of the root directory corresponds the hash code in the versions storage(rootInfo.json) then corresponding the number version is found
     * else -version was not found(-1).
     *
     * @param rootPath the selected root directory path.
     * @param infoPath the path versions storage(rootInfo.json).
     * @return version number of the changes root directory or -1 if version wasn't found.
     * @throws PathIncorrectException        if path to selected root directory or some paths to versions storage are not found or other IO errors have been detected.
     * @throws ServiceFileStructureException if the versions storage(rootInfo.json) has an incorrect structure.
     */
    private int compareDataByHash(Path rootPath, Path infoPath) throws PathIncorrectException, ServiceFileStructureException {
        try {
            if (Files.exists(infoPath) && Files.exists(rootPath)) {

                data = factoryHelper.createRootData(rootPath, ConstNames.SERVICE_DIR_NAME);
                info = factoryHelper.loadRootInfo(infoPath);

                return info.findVersionByHash(data.getHashCode());
            } else {
                throw new PathIncorrectException(String.format(ConstMsgs.SERVICE_FILE_NOT_FOUND, infoPath));
            }
        } catch (IOException ioExc) {
            throw new PathIncorrectException(String.format(ConstMsgs.PATH_NOT_FOUND, rootPath), ioExc);
        } catch (ParseException parseExc) {
            throw new ServiceFileStructureException(String.format(ConstMsgs.SERVICE_FILE_INVALID, infoPath), parseExc);
        }
    }

    /**
     * Performs the adding a new version source data to the versions storage(database.dat).The comparing between the source data
     * and modified data occurs with the {@link #compareDataByHash(Path, Path) CompareDataByHash}.
     * If all the necessary files in the versions storage(database.dat,rootInfo.json) exists then
     * reads all the structure of directories and nested data from the selected root directory.
     * And then if source data in root directory is modified then received data is saved to versions storage(database.dat)
     * and specific information such a version number and a hash code are saved to the versions storage(rootInfo.json).
     *
     * @throws PathIncorrectException        if path to selected root directory is not found or other IO errors have been detected.
     * @throws ServiceFileStructureException if the versions storage(rootInfo.json) has an incorrect structure.
     */
    public void addData() throws PathIncorrectException, ServiceFileStructureException {
        try {
            int result = compareDataByHash(rootPath, infoPath);
            if (result > 0) {
                IOUtill.writeString(String.format(ConstMsgs.CHANGES_NOT_FOUND, rootPath));
            } else {
                int version = info.getLastVersionDir() + 1;

                info.addNode(version, data.getHashCode());
                info.store(infoPath);
                data.store(newPathDBbyVersion(version));
            }
        } catch (IOException ioExc) {
            throw new PathIncorrectException(String.format(ConstMsgs.PATH_NOT_FOUND, rootPath), ioExc);

        }

    }

    /**
     * Performs the restoring source data in the selected root directory by version number of changes.
     * Firstly, builds the path to a new versions storage(database.dat) by version number - {@link #newPathDBbyVersion(int) NewPathDBByVersion}.
     * After it if all the necessary files in the versions storage(database.dat,rootInfo.json) exists then
     * reads all the structure of directories and nested data from the selected root directory.If they don't exist
     * - will be thrown the {@link PathIncorrectException PathIncorrectException}.
     * If version number of changes is found in the versions storage(rootInfo.json) then
     * occurs changing all the data in the selected root directory -{@link #changeRootData(RootData) ChangeRootData}.
     * In another way we can see information message about some changes are not found.
     *
     * @param version - version number of changes.
     * @throws PathIncorrectException        - if path to versions storage(new file database.dat,rootInfo.json) is not found
     *                                       or other IO errors have been detected.
     * @throws ServiceFileStructureException if the versions storage(rootInfo.json) has an incorrect structure.
     * @see RootData part of version storage(database.dat).
     * @see RootInfo part of version storage(rootInfo.json).
     */
    public void restoreData(int version) throws PathIncorrectException, ServiceFileStructureException {
        Path dbPath = newPathDBbyVersion(version);

        try {
            if (Files.exists(dbPath) && Files.exists(infoPath)) {
                info = factoryHelper.loadRootInfo(infoPath);

                if (info.isVersionExist(version)) {
                    data = factoryHelper.loadRootData(dbPath);
                    changeRootData(data);
                } else {
                    IOUtill.writeString(String.format(ConstMsgs.CHANGES_NOT_FOUND, rootPath));
                }

            } else {
                throw new PathIncorrectException(String.format(ConstMsgs.DATABASE_NOT_FOUND, dbPath));
            }
        } catch (ClassNotFoundException cnfe) {
            throw new PathIncorrectException(ConstMsgs.CLASS_PATH_NOT_FOUND, cnfe);

        } catch (IOException ioExc) {
            throw new PathIncorrectException(String.format(ConstMsgs.PATH_NOT_FOUND, rootPath), ioExc);
        } catch (ParseException parseExc) {
            throw new ServiceFileStructureException(String.format(ConstMsgs.SERVICE_FILE_INVALID, infoPath), parseExc);
        }
    }

    //Returns the path to a new database by version of root directory.
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




