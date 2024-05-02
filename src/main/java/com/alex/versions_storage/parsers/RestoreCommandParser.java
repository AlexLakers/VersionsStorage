package com.alex.versions_storage.parsers;

import com.alex.versions_storage.service.StorageManager;
import com.alex.versions_storage.commands.Command;
import com.alex.versions_storage.commands.RestoreCommand;

import java.nio.file.Path;

public class RestoreCommandParser implements CommandParser {
    @Override
    public Command parse(String line) {
        if(line==null||line.isEmpty())throw new IllegalArgumentException("Argument must not be empty or null");
        String[] args = line.split(" ");
        if(args.length!=2)throw new IllegalArgumentException("You need to input two arguments");
        return new RestoreCommand(new StorageManager(Path.of(args[0])), Integer.parseInt(args[1]));
    }
}