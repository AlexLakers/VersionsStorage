package com.alex.versions_storage;


import com.alex.versions_storage.commands.Command;
import com.alex.versions_storage.service.StorageManager;


//This class allows us to use StorageManager when we implements Command.
public abstract class StorageManagerCommand implements Command {
    private StorageManager manager;

    public StorageManagerCommand(StorageManager manager) {
        this.manager = manager;
    }

    public StorageManager getManager() {
        return manager;
    }
}
