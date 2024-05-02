package com.alex.versions_storage.commands;

import com.alex.versions_storage.StorageManagerCommand;
import com.alex.versions_storage.service.StorageManager;
import com.alex.versions_storage.exceptions.ExecutingException;
import com.alex.versions_storage.exceptions.PathIncorrectException;
import com.alex.versions_storage.exceptions.ServiceFileStructureException;

public class RestoreCommand extends StorageManagerCommand {
    private int version;

    public RestoreCommand(StorageManager manager, int version) {
        super(manager);
        this.version = version;
    }

    @Override
    public void execute() throws ExecutingException {
        try {
            getManager().restoreData(version);
        } catch (ServiceFileStructureException | PathIncorrectException e) {
            throw new ExecutingException("An Error restoring data has been detected:" + e.getMessage(), e);
        }
    }


}
