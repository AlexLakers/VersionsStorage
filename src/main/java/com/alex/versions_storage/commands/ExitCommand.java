package com.alex.versions_storage.commands;


import com.alex.versions_storage.utill.IOUtill;

/**
 * The exit command.
 * It describes the application closing.
 */
public class ExitCommand implements Command {

    /**
     * Performs the closing of application.
     */
    @Override
    public void execute() {
        IOUtill.writeString("The version storage is closed");
    }
}
