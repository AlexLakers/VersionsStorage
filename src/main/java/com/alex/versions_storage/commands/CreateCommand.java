package com.alex.versions_storage.commands;

import com.alex.versions_storage.StorageManagerCommand;
import com.alex.versions_storage.service.StorageManager;
import com.alex.versions_storage.exceptions.ExecutingException;
import com.alex.versions_storage.exceptions.PathIncorrectException;

/**
 * The creating command.
 * It describes the command of creating versions storage(rootInfo.json,database.dat,.VersionsStorage).
 */
public class CreateCommand extends StorageManagerCommand {
    /**
     * Creates a new CompareCommand with the provider functions.
     *
     * @param manager functions provider for some command.
     */
    public CreateCommand(StorageManager manager) {
        super(manager);
    }

    /**
     * Performs the action of creating versions storage.
     *
     * @throws ExecutingException if some error has been detected during creating versions storage.
     */
    @Override
    public void execute() throws ExecutingException {
        try {
            getManager().createStorage();
        } catch (PathIncorrectException e) {
            throw new ExecutingException("An error of creating storage has been detected:" + e.getMessage(), e);
        }
    }


}
