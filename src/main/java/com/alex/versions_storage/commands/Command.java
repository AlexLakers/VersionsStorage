package com.alex.versions_storage.commands;

import com.alex.versions_storage.exceptions.ExecutingException;

/**
 * The command interface.
 * It allows you to realize a specific command.
 */
@FunctionalInterface
public interface Command {
    /**
     * Performs a specific action.
     *
     * @throws ExecutingException if some error has been detected.
     */
    void execute() throws ExecutingException;
}