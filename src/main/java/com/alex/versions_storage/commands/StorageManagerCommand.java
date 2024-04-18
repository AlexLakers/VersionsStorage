package com.alex.versions_storage.commands;


import com.alex.versions_storage.provider.StorageManager;

public abstract class StorageManagerCommand implements Command {
    private StorageManager manager;

    public StorageManagerCommand(StorageManager manager) {
        this.manager = manager;
    }

    public StorageManager getManager() {
        return manager;
    }
}
