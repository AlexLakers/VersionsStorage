package com.alex.versions_storage.parsers;

import com.alex.versions_storage.commands.Command;


/**
 * The command interface.
 * It allows you to realize a specific command parser.
 */
public interface CommandParser {
    /**
     * Performs a specific parsing by the transmitted string.
     */
    Command parse(String line);
}