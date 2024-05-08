package com.alex.versions_storage.utill;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.channels.Channels;

/**
 * It is utility class.
 * It contains the methods that help you to work with IO system.
 */
public final class IOUtill {

    private static BufferedReader buffReader;

    /**
     * Initializing the {@link IOUtill#buffReader BufferedReader} to further using it a lot of times without reinit.
     */
    static {
        InputStream console = System.in;
        InputStreamReader inputStreamReader = new InputStreamReader(console, Charset.forName("UTF-8"));
        buffReader = new BufferedReader(inputStreamReader);
    }

    private IOUtill() {

    }

    /**
     * Reads and returns a string line from the input console and prints the transmitted message to the output console.
     *
     * @param message message for output.
     * @return string line.
     */
    public static String readWithMessage(String message) {
        String inputData = "";
        IOUtill.writeString(message);
        try {
            inputData = buffReader.readLine();
        } catch (IOException ioExc) {
            IOUtill.writeString("A problem of reading from the console was detected.");
            System.exit(1);
        }
        return inputData;

    }

    /**
     * Writes the transmitted message to console.
     *
     * @param message message for output.
     */
    public static void writeString(String message) {
        System.out.println(message);
    }

    /**
     * Writes a bytes array to a transmitted output stream.
     * It occurs with NIO system(channels and buffer).
     *
     * @param data array of bytes.
     * @param out  output stream.
     * @throws IOException if  either IO error has been detected.For example,the output stream has incorrect path.
     * @see java.nio nio package.
     */
    public static void writeBytes(byte[] data, OutputStream out) throws IOException {
        //Path is null
        try (WritableByteChannel fileChannel = Channels.newChannel(out)) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            fileChannel.write(byteBuffer);
        }

    }

    /**
     * Reads and returns an array of bytes from some input stream.Also you need to give size of buffer.
     * It occurs with NIO system(channels and buffer).
     *
     * @param in input stream for reading
     * @param sz size of buffer.
     * @return array of bytes.
     * @throws IOException - if  either IO error has been detected.For example, the input stream has incorrect path.
     * @see java.nio nio package.
     */
    public static byte[] readBytes(InputStream in, int sz) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteBuffer byteBuffer = ByteBuffer.allocate(sz);
        try (ReadableByteChannel fileChanel = Channels.newChannel(in)) {
            int countOfBytes = 0;
            while ((countOfBytes = fileChanel.read(byteBuffer)) > 0) {
                bos.write(byteBuffer.array(), 0, countOfBytes);
            }
        }
        return bos.toByteArray();
    }

}