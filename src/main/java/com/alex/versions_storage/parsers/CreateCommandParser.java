package com.alex.versions_storage.parsers;

import com.alex.versions_storage.service.StorageManager;
import com.alex.versions_storage.commands.Command;
import com.alex.versions_storage.commands.CreateCommand;

import java.nio.file.Path;

/**
 * It describes the parser that allows to convert(parse) the string of args
 * into the creating command.
 */
public class CreateCommandParser implements CommandParser {
    /**
     * Returns the creating command by string of args.
     *
     * @param line string of args.
     * @return the creating command.
     */
    @Override
    public Command parse(String line) {
        if (line == null || line.isEmpty()) throw new IllegalArgumentException("Argument must not be empty or null");
        String[] args = line.split(" ");//Maybe change just split
        return new CreateCommand(new StorageManager(Path.of(args[0])));
    }
}