package com.alex.versions_storage.commands;

import com.alex.versions_storage.exceptions.ExecutingException;

public interface Command {
    void execute() throws ExecutingException;
}
