package com.alex.versions_storage.commands;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public interface Command {
    void execute()throws IOException, ParseException,ClassNotFoundException;
}
