package com.alex.versions_storage.commands;

import com.alex.versions_storage.StorageManagerCommand;
import com.alex.versions_storage.service.StorageManager;
import com.alex.versions_storage.exceptions.ExecutingException;
import com.alex.versions_storage.exceptions.PathIncorrectException;
import com.alex.versions_storage.exceptions.ServiceFileStructureException;

/**
 * The restoring command.
 * It describes the restoring data from versions storage(rootInfo.json,database.dat)
 * to the source selected directory.
 */
public class RestoreCommand extends StorageManagerCommand {
    private int version;

    /**
     * Creates a new RestoreCommand with the provider functions
     * and specific version data of selected directory for restoring.
     *
     * @param manager provider of functions for command.
     * @param version specific version data.
     */
    public RestoreCommand(StorageManager manager, int version) {
        super(manager);
        this.version = version;
    }

    /**
     * Performs the action of restoring data.
     *
     * @throws ExecutingException if some error has been detected during restoring data.
     */
    @Override
    public void execute() throws ExecutingException {
        try {
            getManager().restoreData(version);
        } catch (ServiceFileStructureException | PathIncorrectException e) {
            throw new ExecutingException("An Error restoring data has been detected:" + e.getMessage(), e);
        }
    }
}
