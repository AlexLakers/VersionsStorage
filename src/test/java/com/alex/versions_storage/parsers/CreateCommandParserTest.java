package com.alex.versions_storage.parsers;

import com.alex.versions_storage.commands.Command;
import com.alex.versions_storage.commands.CreateCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateCommandParserTest {
    @Test
    void parse_shouldReturnCreateCommand_whenArgIsValid() {
        Command command = new CreateCommandParser().parse("/root/subroot/");

        Assertions.assertNotNull(command);
        Assertions.assertTrue(command instanceof CreateCommand);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void parse_shouldThrowIllegalArgumentException_whenArgIsNullOrEmpty(String line) {
        Throwable thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> new CreateCommandParser().parse(line));
        String expected = "Argument must not be empty or null";

        String actual = thrown.getMessage();

        Assertions.assertEquals(expected, actual);
    }

}