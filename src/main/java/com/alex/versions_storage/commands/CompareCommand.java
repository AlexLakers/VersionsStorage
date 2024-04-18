package com.alex.versions_storage.commands;

import com.alex.versions_storage.provider.StorageManager;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Path;

public class CompareCommand extends StorageManagerCommand {


    public CompareCommand(StorageManager manager) {
        super(manager);

    }

    @Override
    public void execute() throws IOException, ParseException {

        getManager().compareData();


    }
}
