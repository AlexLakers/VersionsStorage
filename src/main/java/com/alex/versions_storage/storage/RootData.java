package com.alex.versions_storage.storage;

import com.alex.versions_storage.utill.IOUtill;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;


/**
 * It describes the main part of versions storage - database.dat.
 */
public class RootData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_VERSION = 1;
    private Map<String, byte[]> files;
    private transient List<Path> dirs;
    private transient Path path;
    private int version;

    /**
     * Creates a new RootData with path to root directory and service directory name.
     * During creating occurs the recursive filling {@link #files files list} and {@link #dirs dirs list}
     * It occurs with using {@link #collectFilesAndDirsByRecursive(Path, DirectoryStream.Filter) CollectFilesAndDirsByRecursive}.
     *
     * @param path       root directory path.
     * @param serviceDir service directory name.
     * @throws IOException if path to selected root directory is not found or other IO errors have been detected.
     */
    public RootData(Path path, String serviceDir) throws IOException {
        this.path = path;
        this.dirs = new ArrayList<>();
        this.files = new HashMap<>();
        this.version = DEFAULT_VERSION;
        collectFilesAndDirsByRecursive(path, (name) -> !name.endsWith(serviceDir));
    }

    /**
     * Creates new RootData with path to root directory,specific version root directory and service directory name.
     *
     * @param path       root directory path.
     * @param serviceDir service directory name.
     * @throws IOException if path to selected root directory is not found or other IO errors have been detected.
     * @see RootData#RootData(Path, String) new RootData(Path,String).
     */
    public RootData(Path path, int version, String serviceDir) throws IOException {
        this(path, serviceDir);
        this.version = version;
    }

    public int getHashCode() {
        return this.hashCode();
    }

    public int getVersion() {
        return version;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (Objects.isNull(o) || (this.getClass() != o.getClass())) return false;
        RootData tree = (RootData) o;
        if (files.size() != tree.files.size()) return false;
        for (var entry : files.entrySet()) {
            byte[] bytes = entry.getValue();
            byte[] bytesEq = tree.files.get(entry.getKey());
            if (bytes != null ? !Arrays.equals(bytes, bytesEq) : bytesEq != null) return false;
        }

        return dirs != null ? dirs.equals(tree.dirs) : tree.dirs == null;
    }

    public int hashCode() {
        int result = dirs == null ? 0 : dirs.hashCode();
        for (var key : files.keySet()) {
            byte[] val = files.get(key);
            result *= (key == null) ? 0 : key.hashCode();
            result *= (val == null) ? 0 : Arrays.hashCode(val);
        }
        return result;

    }

    @Serial
    private void readObject(ObjectInputStream input) throws ClassNotFoundException, IOException {
        input.defaultReadObject();
        path = Path.of((String) input.readObject());
        List<String> strPathsDirs = (List<String>) input.readObject();
        dirs = strPathsDirs.stream()
                .map(Path::of)
                .toList();


    }

    @Serial
    private void writeObject(ObjectOutputStream output) throws IOException {
        output.defaultWriteObject();
        output.writeObject(path.toString());
        List<String> strPathsDir = dirs.stream()
                .map(Path::toString)
                .toList();
        output.writeObject(strPathsDir);

    }

    /**
     * Performs a recursive filling {@link #files files list} and {@link #dirs dirs list}.
     *
     * @param pathDir path to selected root directory.
     * @param filter  service directory name.
     * @throws IOException if path to selected root directory is not found or other IO errors have been detected.
     */
    private void collectFilesAndDirsByRecursive(Path pathDir, DirectoryStream.Filter<Path> filter) throws IOException {
        List<Path> tempPaths = new ArrayList<>();
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(pathDir, filter)) {
            Iterator<Path> iteratorPaths = paths.iterator();
            while (iteratorPaths.hasNext()) {
                Path path = iteratorPaths.next();
                if (Files.isRegularFile(path)) {
                    try (InputStream in = Files.newInputStream(path, StandardOpenOption.READ)) {
                        byte[] data = IOUtill.readBytes(in, 1024);
                        files.put(path.toString(), data);
                    }
                } else {
                    tempPaths.add(path);
                }
            }
        }

        dirs.addAll(tempPaths);

        for (Path path : tempPaths) {
            collectFilesAndDirsByRecursive(path, filter);
        }
    }


    public List<Path> getDirs() {
        return Collections.unmodifiableList(dirs);
    }

    public Map<String, byte[]> getFiles() {
        return Collections.unmodifiableMap(files);
    }

    /**
     * Returns a new RootData from version storage(database.dat) with path to it.
     *
     * @param path - path to database.dat
     * @return new RootData.
     * @throws IOException            if path to version storage(database.dat) is not found or other IO errors have been detected.
     * @throws ClassNotFoundException if class is not found during compile.
     */
    public static RootData loadFromDB(Path path) throws IOException, ClassNotFoundException {
        try (InputStream input = Files.newInputStream(path, StandardOpenOption.READ);
             ObjectInputStream in = new ObjectInputStream(input)) {
            return (RootData) in.readObject();
        }
    }

    /**
     * Performs the saving(serialising) all the data from selected root directory to version storage(database.dat).
     *
     * @param path path to database.dat
     * @throws IOException if path to version storage(database.dat) is not found or other IO errors have been detected.
     */
    public void store(Path path) throws IOException {

        try (OutputStream output = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
             ObjectOutput out = new ObjectOutputStream(output)) {
            out.writeObject(this);
        }
    }
}