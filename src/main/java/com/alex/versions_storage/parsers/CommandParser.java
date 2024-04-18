package com.alex.versions_storage.parsers;

import com.alex.versions_storage.commands.Command;

public interface CommandParser {
    Command parse(String line);

}