package com.alex.versions_storage.parsers;

import com.alex.versions_storage.service.StorageManager;
import com.alex.versions_storage.commands.Command;
import com.alex.versions_storage.commands.CreateCommand;

import java.nio.file.Path;

public class CreateCommandParser implements CommandParser {
    @Override
    public Command parse(String line) {
        if(line==null||line.isEmpty())throw new IllegalArgumentException("Argument must not be empty or null");
        String[] args = line.split(" ");
        return new CreateCommand(new StorageManager(Path.of(args[0])));
    }


}