package com.alex.versions_storage.commands;

import com.alex.versions_storage.StorageManager;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Path;

public class RestoreCommand implements Command {

    private Path path;
    public RestoreCommand(Path path){
        this.path=path;
    }
    public void execute()throws IOException, ParseException,ClassNotFoundException {
        new StorageManager(path).restoreData();
    }
}
