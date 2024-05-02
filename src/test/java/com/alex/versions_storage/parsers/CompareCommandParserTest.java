package com.alex.versions_storage.parsers;

import com.alex.versions_storage.commands.Command;
import com.alex.versions_storage.commands.CompareCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompareCommandParserTest {
    @Test
    void parse_shouldReturnCompareCommand_whenArgIsValid() {
        Command command = new CompareCommandParser().parse("/root/subroot/");

        Assertions.assertNotNull(command);
        Assertions.assertTrue(command instanceof CompareCommand);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void parse_shouldThrowIllegalArgumentException_whenArgIsNullOrEmpty(String line) {
        Throwable thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> new CompareCommandParser().parse(line));
        String expected = "Argument must not be empty or null";

        String actual = thrown.getMessage();

        Assertions.assertEquals(expected, actual);
    }
}