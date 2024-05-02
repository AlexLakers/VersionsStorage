package com.alex.versions_storage.utill;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class JsonUtillTest {
    private static JSONObject json;

    @BeforeAll
    static void initJson() throws ParseException {
        json = (JSONObject) new JSONParser().parse("{\"intProp\":1131413504,\"intProp1\":1,\"pathProp\":\"/etc/temp\",\"pathProp1\":\"/etc/temp1/\"}");
    }

    @Test
    void load_shouldReturnJSONObjectFromFile_whenReaderIsCorrect() throws IOException, ParseException {
        String expected = "{\"versionsInfo\":[{\"hashCode\":1131413504,\"version\":1}],\"rootPath\":\"some path\"}";

        JSONObject jsonObject = JsonUtill.load(new CharArrayReader(expected.toCharArray()));
        String actual = jsonObject.toString();

        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @NullSource
    void load_shouldThrowIllegalArgumentException_whenArgReaderIsNull(Reader reader) throws IOException, ParseException {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> JsonUtill.load(reader));
        String expected = "You try to use null arg";

        Assertions.assertEquals(expected, thrown.getMessage());
    }

    static Stream<Arguments> getInvalidArguments() {
        return Stream.of(
                Arguments.of("\"versionsInfo\":[{\"hashCode\":1131413504\"version\":1}]\"rootPath\":\"some path\"}"),
                Arguments.of("versionsInfo:[{hashCode:1131413504,version:1}],\"rootPath\":\"some path\"}"),
                Arguments.of("{\"versionsInfo\"=[{\"hashCode\"=1131413504,\"version\":1}],\"rootPath\"=\"some path\"}")
        );
    }

    @ParameterizedTest
    @MethodSource("getInvalidArguments")
    void load_shouldThrowParseException_whenIncorrectJSON(String jsonString) {
        Assertions.assertThrows(ParseException.class, () -> JsonUtill.load(new CharArrayReader(jsonString.toCharArray())));
    }

    static Stream<Arguments> getArgumentsForGetIntProp() {
        return Stream.of(
                Arguments.of(json, "intProp", 1131413504),
                Arguments.of(json, "intProp1", 1)
        );
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForGetIntProp")
    void getIntProp_shouldReturnIntValue_whenValueIsExistsInFile(JSONObject json, String pName, int expected) {
        int actual = JsonUtill.getIntProp(json, pName);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getIntProp_shouldThrowIllegalException_whenSomeArgIsNull() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> JsonUtill.getIntProp(null, null));
        String expected = "You try to use null arg";

        Assertions.assertEquals(expected, thrown.getMessage());
    }

    @Test
    void getIntProp_shouldThrowIllegalException_whenNamePropNotFound() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> JsonUtill.getIntProp(json, "invalidPropName"));
        String expected = "Property is not found";

        Assertions.assertEquals(expected, thrown.getMessage());
    }

    static Stream<Arguments> getArgumentsForGetPathProp() {
        return Stream.of(
                Arguments.of(json, "pathProp", "/etc/temp"),
                Arguments.of(json, "pathProp1", "/etc/temp1")
        );
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForGetPathProp")
    void getPathProp_shouldReturnPathValue_whenValueIsExistsInFile(JSONObject json, String pName, Path expected) {
        Path actual = JsonUtill.getPathProp(json, pName);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getPathProp_shouldThrowIllegalException_whenSomeArgIsNull() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> JsonUtill.getPathProp(null, null));
        String expected = "You try to use null arg";

        Assertions.assertEquals(expected, thrown.getMessage());
    }

    @Test
    void getPathProp_shouldThrowIllegalException_whenNamePropNotFound() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> JsonUtill.getIntProp(json, "invalidPropName"));
        String expected = "Property is not found";

        Assertions.assertEquals(expected, thrown.getMessage());
    }
}