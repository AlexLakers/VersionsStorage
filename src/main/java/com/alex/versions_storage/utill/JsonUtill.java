package com.alex.versions_storage.utill;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

/**
 * It is utility class.
 * It contains the methods that's helps you to work with the fallowing lib:org.json.simple.
 */
public class JsonUtill {
    /**
     * Reads and returns JsonObject from some Reader that's source data(.json).
     * It occurs with using JsonParser.
     *
     * @param reader Reader which contains data(.json).
     * @return new JSONObject.
     * @throws IOException    if either IO error has been detected.For example,the Reader's stream has incorrect path.
     * @throws ParseException if data(.json) in Reader's stream has incorrect json-structure which impossible for parsing.
     * @see 'com.googlecode.json-simple'
     */
    public static JSONObject load(Reader reader) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        if (Objects.isNull(reader)) {
            throw new IllegalArgumentException("You try to use null arg");
        }
        JSONObject jNode = (JSONObject) jsonParser.parse(reader);
        return jNode;

    }

    /**
     * Returns integer's value from the transmitted JsonObject by property's name.
     * It is possible if property value has some numeric type.
     *
     * @param src   JSONObject for analyzing.
     * @param pName property's name.
     * @return property value as integer.
     * @throws IllegalArgumentException if the one of the transmitted args is null or empty.
     *                                  Also you can see it if property's value in not found in the JSONObject.
     * @see 'com.googlecode.json-simple'
     */
    public static int getIntProp(JSONObject src, String pName) {
        if (src == null || pName == null || pName.isEmpty()) {
            throw new IllegalArgumentException("You try to use null arg");
        }
        Long val = (Long) src.get(pName);
        if (val == null) {
            throw new IllegalArgumentException("Property is not found");
        }
        return val.intValue();
    }

    /**
     * Returns path from the transmitted sonObject by property's name.
     * It is possible if property value has string type.
     *
     * @param src   JSONObject for analyzing.
     * @param pName property's name.
     * @return property value as Path type.
     * @throws IllegalArgumentException if the one of the transmitted args is null or empty.
     *                                  Also you can see it if property's value in not found in the JSONObject.
     * @see 'com.googlecode.json-simple'
     */
    public static Path getPathProp(JSONObject src, String pName) {
        if (src == null || pName == null || pName.isEmpty()) {
            throw new IllegalArgumentException("You try to use null arg");
        }
        String val = ((String) src.get(pName));
        if (val == null) {
            throw new IllegalArgumentException("Property is not found");
        }
        return Path.of(val);
    }
}