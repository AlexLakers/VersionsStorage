package com.alex.versions_storage.commands;

import com.alex.versions_storage.provider.StorageManager;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Path;

public class RestoreCommand implements Command {

    private Path path;
    private int version;
    public RestoreCommand(Path path,int version){
        this.path=path;
        this.version=version;
    }
    public void execute()throws IOException, ParseException,ClassNotFoundException {
        new StorageManager(path).restoreData(version);
    }
}
