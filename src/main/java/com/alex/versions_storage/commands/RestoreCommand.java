package com.alex.versions_storage.commands;

import com.alex.versions_storage.provider.StorageManager;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Path;

public class RestoreCommand extends StorageManagerCommand {
    private int version;


    public RestoreCommand(StorageManager manager, int version) {
        super(manager);
        this.version=version;
    }

    @Override
    public void execute()throws IOException,ParseException,ClassNotFoundException {
            getManager().restoreData(version);


    }


}
