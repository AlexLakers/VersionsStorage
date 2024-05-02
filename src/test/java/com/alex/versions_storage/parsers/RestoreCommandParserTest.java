package com.alex.versions_storage.parsers;

import com.alex.versions_storage.commands.Command;
import com.alex.versions_storage.commands.RestoreCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestoreCommandParserTest {
    @Test
    void parse_shouldReturnRestoreCommand_whenArgIsValid() {
        Command command = new RestoreCommandParser().parse("/root/subroot/ 1");

        Assertions.assertNotNull(command);
        Assertions.assertTrue(command instanceof RestoreCommand);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void parse_shouldThrowIllegalArgumentException_whenArgIsNullOrEmpty(String line) {
        Throwable thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> new RestoreCommandParser().parse(line));
        String expected = "Argument must not be empty or null";

        String actual = thrown.getMessage();

        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"path"})
    void parse_shouldThrowIllegalArgumentException_whenLineContainsOneArg(String line) {
        Throwable thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> new RestoreCommandParser().parse(line));
        String expected = "You need to input two arguments";

        String actual = thrown.getMessage();

        Assertions.assertEquals(expected, actual);
    }

}