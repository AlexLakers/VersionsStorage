package com.alex.versions_storage.commands;

import com.alex.versions_storage.StorageManagerCommand;
import com.alex.versions_storage.exceptions.ExecutingException;
import com.alex.versions_storage.exceptions.PathIncorrectException;
import com.alex.versions_storage.exceptions.ServiceFileStructureException;
import com.alex.versions_storage.service.StorageManager;

/**
 * The adding command.
 * It describes the command of adding data to the versions storage(rootInfo.json,database.dat).
 */
public class AddCommand extends StorageManagerCommand {
    /**
     * Creates a new AddCommand with the provider functions.
     *
     * @param manager provider of functions for command.
     */
    public AddCommand(StorageManager manager) {
        super(manager);
    }

    /**
     * Performs the action of adding data.
     *
     * @throws ExecutingException if some error has been detected during adding data.
     */
    @Override
    public void execute() throws ExecutingException {
        try {
            getManager().addData();
        } catch (ServiceFileStructureException | PathIncorrectException e) {
            throw new ExecutingException("An error of adding data has been detected:" + e.getMessage(), e);
        }

    }
}
