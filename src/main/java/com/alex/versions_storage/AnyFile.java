package com.alex.versions_storage;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;


public class AnyFile implements Serializable {
    private transient ByteArrayOutputStream dataBaos;
    private static final long serialVersionUID = 1L;
    private transient Path path;

    public AnyFile(Path path) throws IOException {
        this.path=path;
        dataBaos = new ByteArrayOutputStream();
        byte[] data = readData(path);
        dataBaos.write(data);

    }
        private byte[] readData(Path path)throws IOException{
        ByteArrayOutputStream bos =new ByteArrayOutputStream();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try(FileChannel fileChanel= (FileChannel) Files.newByteChannel(path, StandardOpenOption.READ)) {
            int countOfBytes = 0;
            while ((countOfBytes = fileChanel.read(byteBuffer)) > 0) {
                bos.write(byteBuffer.array(),0,countOfBytes);
            }
        }
        return bos.toByteArray();

    }


    public ByteArrayOutputStream getByteOutputStream() {
        return dataBaos;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if ((o==null) || (this.getClass() != o.getClass())) return false;
        AnyFile file = (AnyFile) o;
        return (dataBaos != null ? Arrays.equals(this.dataBaos.toByteArray(), file.dataBaos.toByteArray()) : file.dataBaos==null);

    }

    public int hashCode() {
        int result = 31 * (dataBaos==null ? 0 : Arrays.hashCode(dataBaos.toByteArray()));
        return result;
    }

    public Path getPath() {
        return path;
    }

    private void writeObject(ObjectOutputStream output) throws IOException{

            output.defaultWriteObject();
            output.writeObject(dataBaos.toByteArray());
            output.writeObject(path.toString());

    }

    private void readObject(ObjectInputStream input)throws IOException,ClassNotFoundException {

            input.defaultReadObject();
            dataBaos = new ByteArrayOutputStream();
            dataBaos.write(((byte[]) input.readObject()));
             path=Path.of((String)input.readObject());


    }


}
