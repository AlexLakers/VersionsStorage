package com.alex.versions_storage.parsers;

import com.alex.versions_storage.commands.Command;


//This interface helps us to parse implementations of commands
public interface CommandParser {
    Command parse(String line);

}