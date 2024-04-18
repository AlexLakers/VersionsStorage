package com.alex.versions_storage.commands;

import com.alex.versions_storage.provider.StorageManager;

import java.io.IOException;
import java.nio.file.Path;

public class CreateCommand extends StorageManagerCommand{


    public CreateCommand(StorageManager manager) {
        super(manager);
    }

    @Override
    public void execute()throws IOException{
            getManager().createStorage();


    }


}
