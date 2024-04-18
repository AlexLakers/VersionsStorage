package com.alex.versions_storage.parsers;

import com.alex.versions_storage.provider.StorageManager;
import com.alex.versions_storage.commands.Command;
import com.alex.versions_storage.commands.CreateCommand;

import java.nio.file.Path;

public class CreateCommandParser implements CommandParser{
    @Override
    public Command parse(String line){
        String[] args= line.split(" ");//Maybe change just split
        return new CreateCommand(new StorageManager(Path.of(args[0])));
    }



}