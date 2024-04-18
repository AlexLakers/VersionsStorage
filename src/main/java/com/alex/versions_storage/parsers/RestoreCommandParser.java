package com.alex.versions_storage.parsers;

import com.alex.versions_storage.provider.StorageManager;
import com.alex.versions_storage.commands.Command;
import com.alex.versions_storage.commands.RestoreCommand;

import java.nio.file.Path;

public class RestoreCommandParser implements CommandParser {
    @Override
    public Command parse(String line) {
        String[] args = line.split(" ");//Maybe change just split
        return new RestoreCommand(new StorageManager(Path.of(args[0])),Integer.parseInt(args[1]));
    }
}