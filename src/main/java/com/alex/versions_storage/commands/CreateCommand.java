package com.alex.versions_storage.commands;

import com.alex.versions_storage.StorageManager;

import java.io.IOException;
import java.nio.file.Path;

public class CreateCommand implements Command{
    private Path path;
    public CreateCommand(Path path){
        this.path=path;
    }
    public void execute()throws IOException{
    new StorageManager(path).createStorage();
    }

}
