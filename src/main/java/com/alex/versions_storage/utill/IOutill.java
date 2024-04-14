package com.alex.versions_storage.utill;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

public final class IOutill {

    private static BufferedReader buffReader;

    static{
        InputStream console=System.in;
        InputStreamReader inputStreamReader=new InputStreamReader(console,Charset.forName("UTF-8"));
        buffReader = new BufferedReader(inputStreamReader);

    }
    private IOutill(){

    }
    public static String readConsole()throws IOException{
        String inputData="";
        inputData = buffReader.readLine();
        return inputData;



    }
    public static void writeConsole(String line){
        System.out.println(line);
    }
    /*public static void writeData(byte[] data, Path pathToFile)throws IOException{
        //Path is null
        try(FileChannel fileChannel =(FileChannel) Files.newByteChannel(pathToFile, StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.CREATE)){
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            fileChannel.write(byteBuffer);
        }


    }*/
    public static void writeData(byte[] data, OutputStream out)throws IOException{
        //Path is null
        try(WritableByteChannel fileChannel = Channels.newChannel(out)){
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            fileChannel.write(byteBuffer);
        }


    }
    public static byte[] readData(InputStream in)throws IOException{
        ByteArrayOutputStream bos =new ByteArrayOutputStream();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try(ReadableByteChannel fileChanel= Channels.newChannel(in)) {
            int countOfBytes = 0;
            while ((countOfBytes = fileChanel.read(byteBuffer)) > 0) {
                bos.write(byteBuffer.array(),0,countOfBytes);
            }
        }
        return bos.toByteArray();
    }


    public static void writeJsonData(Writer writer, String jStr)throws IOException {
        if (jStr == null||writer==null) {
            throw new IllegalArgumentException("One or more args are null");
        }
        writer.write(jStr);

    }
    public static String[] trySplit(String srcLine,int countArgs)throws IllegalArgumentException{
        if (srcLine==null || srcLine.isEmpty()) {
            throw new IllegalArgumentException("You entered empty command,try it again!");
        }
        if(countArgs<1){
            throw new IllegalArgumentException("Count of args must be positive");
        }
        String[] tokens = srcLine.split("\\s");
        if (tokens.length > countArgs) {
            String message = String.format("You entered too much args[count=%1$d] of command,try it again!",tokens.length);
            throw new IllegalArgumentException(message);
        }
        return tokens;
    }

    public static JSONObject readJsonData(Reader reader)throws IOException, ParseException {
        JSONParser jsonParser =new JSONParser();
        if(Objects.isNull(reader)) {
            throw new IllegalArgumentException("You try to use null arg");
        }
        JSONObject jNode = (JSONObject) jsonParser.parse(reader);
        return jNode;


    }
    public static int getIntProp(JSONObject src,String pName){
        Long val=(Long)src.get(pName);
        return val.intValue();
    }
    public static Path getPathProp(JSONObject src,String name){
        String val=((String)src.get(name));
        return Path.of(val);
    }
    public static Path createPathFromParts(Path absolutePath,String... parts){
        if(Objects.isNull(parts))
            throw new IllegalArgumentException();
        if(!absolutePath.isAbsolute())
            throw new IllegalArgumentException();
        Path path= Arrays.stream(parts)
                .map(part->Path.of(part))
                .reduce(absolutePath,(res,nextPath)->res.resolve(nextPath));

        return path;
    }
    public static DirectoryStream<Path> getPaths(Path rootPath, DirectoryStream.Filter<Path> filterDirectory)throws IOException{
        if(Objects.isNull(rootPath))throw new IllegalArgumentException("Nulll path");
        DirectoryStream<Path> pathes=null;

        if (Objects.nonNull(filterDirectory)) {
            pathes = Files.newDirectoryStream(rootPath, filterDirectory);

        } else {
            pathes = Files.newDirectoryStream(rootPath);
        }

        return pathes;
    }
    public static <T extends Enum>T trySearchEnum(Class<T> enums,String strSample){
        T resConst=null;
        for(T enumConst:enums.getEnumConstants()){
            String strConst=enumConst.name();
            if(strConst.equalsIgnoreCase(strSample)){
                resConst=enumConst;
                return resConst;
            }

        }
        return resConst;
    }
}