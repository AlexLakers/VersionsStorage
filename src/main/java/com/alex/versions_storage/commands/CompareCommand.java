package com.alex.versions_storage.commands;

import com.alex.versions_storage.StorageManagerCommand;
import com.alex.versions_storage.exceptions.ExecutingException;
import com.alex.versions_storage.exceptions.PathIncorrectException;
import com.alex.versions_storage.exceptions.ServiceFileStructureException;
import com.alex.versions_storage.service.StorageManager;

public class CompareCommand extends StorageManagerCommand {


    public CompareCommand(StorageManager manager) {
        super(manager);

    }

    @Override
    public void execute() throws ExecutingException {

        try {
            getManager().compareData();
        } catch (ServiceFileStructureException | PathIncorrectException e) {
            throw new ExecutingException("An error of comparing data has been detected:" + e.getMessage(), e);
        }
    }

}
