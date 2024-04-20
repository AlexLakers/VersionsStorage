package com.alex.versions_storage.commands;

import com.alex.versions_storage.StorageManagerCommand;
import com.alex.versions_storage.service.StorageManager;
import com.alex.versions_storage.exceptions.ExecutingException;
import com.alex.versions_storage.exceptions.PathIncorrectException;

public class CreateCommand extends StorageManagerCommand {


    public CreateCommand(StorageManager manager) {
        super(manager);
    }

    @Override
    public void execute() throws ExecutingException {
        try {
            getManager().createStorage();
        } catch (PathIncorrectException e) {
            throw new ExecutingException("An error of creating storage has been detected:" + e.getMessage(), e);
        }
    }


}
