package com.alex.versions_storage.commands;

import com.alex.versions_storage.StorageManagerCommand;
import com.alex.versions_storage.exceptions.ExecutingException;
import com.alex.versions_storage.exceptions.PathIncorrectException;
import com.alex.versions_storage.exceptions.ServiceFileStructureException;
import com.alex.versions_storage.service.StorageManager;

public class AddCommand extends StorageManagerCommand {

    public AddCommand(StorageManager manager) {
        super(manager);
    }

    @Override
    public void execute() throws ExecutingException {
        try {
            getManager().addData();
        } catch (ServiceFileStructureException | PathIncorrectException e) {
            throw new ExecutingException("An error of adding data has been detected:" + e.getMessage(), e);
        }

    }
}
