package com.alex.versions_storage.parsers;

import com.alex.versions_storage.service.StorageManager;
import com.alex.versions_storage.commands.AddCommand;
import com.alex.versions_storage.commands.Command;

import java.nio.file.Path;

public class AddCommandParser implements CommandParser {
    @Override
    public Command parse(String line) {
        String[] args = line.split(" ");//Maybe change just split+
        return new AddCommand(new StorageManager(Path.of(args[0])));
    }
}