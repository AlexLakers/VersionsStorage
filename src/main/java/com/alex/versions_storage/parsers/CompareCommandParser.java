package com.alex.versions_storage.parsers;

import com.alex.versions_storage.service.StorageManager;
import com.alex.versions_storage.commands.Command;
import com.alex.versions_storage.commands.CompareCommand;

import java.nio.file.Path;

public class CompareCommandParser implements CommandParser {
    @Override
    public Command parse(String line) {
        String[] args = line.split(" ");//Maybe change just split+
        return new CompareCommand(new StorageManager(Path.of(args[0])));
    }

}