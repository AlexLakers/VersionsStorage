package com.alex.versions_storage;

import com.alex.versions_storage.commands.*;
import com.alex.versions_storage.exceptions.ExecutingException;
import com.alex.versions_storage.parsers.*;
import com.alex.versions_storage.utill.IOUtill;

import java.util.Map;


/**
 * The executor allows you to run one of the actions that are defined in the {@link Actions actions}.
 */
public final class Executor {

    private Executor() {
    }

    static Map<Actions, CommandParser> parsers;

    static {
        parsers = Map.of(
                Actions.CREATE, new CreateCommandParser(),
                Actions.COMPARE, new CompareCommandParser(),
                Actions.ADD, new AddCommandParser(),
                Actions.RESTORE, new RestoreCommandParser()
        );
    }

    /**
     * Performs one of the defined actions with using a specific{@link Actions action}.
     * Firstly occurs a search for a specific realisation of {@link CommandParser CommandParser} by a transmitted action.
     * And then occurs the parsing a specific{@link Command command} and executing it.
     * If the command equals 'exit' then parsing process will be skipped.
     *
     * @param action specific action.
     * @throws ExecutingException if some error has been detected (during executing).
     * @see CommandParser CommandParser
     * @see Command Command
     */
    public static void run(Actions action) throws ExecutingException {
        Command command = null;
        if (action != Actions.EXIT) {
            CommandParser parser = parsers.get(action);
            command = parser.parse(IOUtill.readWithMessage("Enter args:"));
        } else {
            command = new ExitCommand();
        }
        command.execute();

    }
}
