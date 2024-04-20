package com.alex.versions_storage;

import com.alex.versions_storage.commands.*;
import com.alex.versions_storage.exceptions.ExecutingException;
import com.alex.versions_storage.parsers.*;
import com.alex.versions_storage.utill.IOUtill;

import java.util.Map;


//This class allow us to execute any command
public final class Executor {

    private Executor() {

    }

    static Map<Actions, CommandParser> parsers;

    static {
        parsers = Map.of(
                Actions.CREATE, new CreateCommandParser(),
                Actions.COMPARE, new CompareCommandParser(),
                Actions.ADD, new AddCommandParser(),
                Actions.RESTORE, new RestoreCommandParser());
    }

    public static void run(Actions action) throws ExecutingException {
        CommandParser parser = parsers.get(action);
        Command command = parser.parse(IOUtill.readWithMessage("Enter args:"));
        command.execute();

    }
}
