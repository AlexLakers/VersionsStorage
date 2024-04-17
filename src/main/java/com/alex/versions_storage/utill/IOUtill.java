package com.alex.versions_storage.utill;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.channels.Channels;

public final class IOUtill{

    private static BufferedReader buffReader;

    static{
        InputStream console=System.in;
        InputStreamReader inputStreamReader=new InputStreamReader(console, Charset.forName("UTF-8"));
        buffReader = new BufferedReader(inputStreamReader);

    }
    private IOUtill(){

    }
    //For reading a string data from Console with some message
    public static String readWithMessage(String message){
        String inputData="";
        IOUtill.writeString(message);
        try {
            inputData = buffReader.readLine();
        }
        catch(IOException ioExc){
            IOUtill.writeString("A problem of reading from the console was detected.");
            System.exit(1);
        }
        return inputData;

    }
    //For writing some String to Console
    public static void writeString(String message){
        System.out.println(message);
    }

    //For writing array of bytes
    public static void writeBytes(byte[] data, OutputStream out)throws IOException{
        try(WritableByteChannel fileChannel = Channels.newChannel(out)){
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            fileChannel.write(byteBuffer);
        }

    }
    public static byte[] readBytes(InputStream in, int sz)throws IOException{
        ByteArrayOutputStream bos =new ByteArrayOutputStream();
        ByteBuffer byteBuffer = ByteBuffer.allocate(sz);
        try(ReadableByteChannel fileChanel= Channels.newChannel(in)) {
            int countOfBytes = 0;
            while ((countOfBytes = fileChanel.read(byteBuffer)) > 0) {
                bos.write(byteBuffer.array(),0,countOfBytes);
            }
        }
        return bos.toByteArray();
    }

}