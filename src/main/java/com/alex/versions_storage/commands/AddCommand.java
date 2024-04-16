package com.alex.versions_storage.commands;

import com.alex.versions_storage.StorageManager;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Path;

public class AddCommand implements Command {

    private Path path;
    public AddCommand(Path path){
        this.path=path;
    }
    public void execute()throws IOException, ParseException {
        new StorageManager(path).addData();
    }
}
