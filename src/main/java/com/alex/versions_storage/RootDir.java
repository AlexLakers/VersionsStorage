package com.alex.versions_storage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class RootDir implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<AnyFile> files;
    private transient List<Path> pathsDirs;
    private transient Path path;

    public RootDir(Path path, String serviceDir) throws IOException {
        this.path=path;
        this.pathsDirs = new ArrayList<>();
        this.files = new ArrayList<>();
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (Objects.isNull(o) || (this.getClass() != o.getClass())) return false;
        RootDir tree = (RootDir) o;
        if (files != null ? !files.equals(tree.files) : tree.files!=null) return false;
        return pathsDirs != null ? pathsDirs.equals(tree.pathsDirs) : tree.pathsDirs==null;
    }

    public int hashCode() {
        int result = pathsDirs==null ? 0 : pathsDirs.hashCode();
        result = result * 31 + (files==null ? 0 : files.hashCode());
        return result;
    }

    private void readObject(ObjectInputStream input)throws ClassNotFoundException,IOException {
            input.defaultReadObject();
            path=Path.of((String)input.readObject());
            List<String> strPathsDirs = (List<String>) input.readObject();
            pathsDirs = strPathsDirs.stream()
                    .map(Path::of)
                    .toList();

    }

    private void writeObject(ObjectOutputStream output)throws IOException {
            output.defaultWriteObject();
            output.writeObject(path.toString());
            List<String> strPathsDir = pathsDirs.stream()
                    .map(Path::toString)
                    .toList();
            output.writeObject(strPathsDir);

    }

  


    public List<Path> getPathsDirs() {
        return pathsDirs;
    }

    public List<AnyFile> getFiles() {
        return files;
    }
}
