package com.alex.versions_storage.commands;

import com.alex.versions_storage.provider.StorageManager;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Path;

public class CompareCommand implements Command{

    private Path path;
    public CompareCommand(Path path){
        this.path=path;
    }
    public void execute()throws IOException, ParseException {
        new StorageManager(path).compareData();
    }
}
