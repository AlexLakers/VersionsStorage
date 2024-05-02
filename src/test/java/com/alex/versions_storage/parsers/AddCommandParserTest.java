package com.alex.versions_storage.parsers;

import com.alex.versions_storage.commands.AddCommand;
import com.alex.versions_storage.commands.Command;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddCommandParserTest {
    @Test
    void parse_shouldReturnAddCommand_whenArgIsValid() {
        Command command=new AddCommandParser().parse("/root/subroot/");

        Assertions.assertNotNull(command);
        Assertions.assertTrue(command instanceof AddCommand);
    }
    @ParameterizedTest
    @NullAndEmptySource
    void parse_shouldThrowIllegalArgumentException_whenArgIsNullOrEmpty(String line) {
        Throwable thrown=Assertions.assertThrows(IllegalArgumentException.class,()->new AddCommandParser().parse(line));
        String expected="Argument must not be empty or null";
        String actual=thrown.getMessage();
        new AddCommandParser().parse("31231");
        Assertions.assertEquals(expected,actual);
    }

}