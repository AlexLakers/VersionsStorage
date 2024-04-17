package com.alex.versions_storage.utill;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

public class JsonUtill {
    public static JSONObject load(Reader reader)throws IOException, ParseException {
        JSONParser jsonParser =new JSONParser();
        if(Objects.isNull(reader)) {
            throw new IllegalArgumentException("You try to use null arg");
        }
        JSONObject jNode = (JSONObject) jsonParser.parse(reader);
        return jNode;

    }
    public static int getIntProp(JSONObject src,String pName){
        if(src==null||pName==null||pName.isEmpty()){
            throw new IllegalArgumentException("You try to use null arg");
        }
        Long val=(Long)src.get(pName);
        /*Optional.of(val).orElseThrow( ()-> new IllegalArgumentException("Property is not found"));*/
        if(val==null){
            throw new IllegalArgumentException("Property is not found");
        }
        return val.intValue();
    }
    public static Path getPathProp(JSONObject src, String pName){
        if(src==null||pName==null||pName.isEmpty()){
            throw new IllegalArgumentException("You try to use null arg");
        }
        String val=((String)src.get(pName));
        if(val==null){
            throw new IllegalArgumentException("Property is not found");
        }
        return Path.of(val);
    }
}
