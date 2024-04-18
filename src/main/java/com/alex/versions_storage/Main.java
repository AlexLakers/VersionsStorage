package com.alex.versions_storage;
import com.alex.versions_storage.utill.IOUtill;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Map;


public class Main {
    static Map<Actions, Integer> countArgsCommands = null;

    static {
        countArgsCommands = Map.of(
                Actions.CREATE, 1,
                Actions.COMPARE, 1,
                Actions.ADD, 1,
                Actions.RESTORE, 2,
                Actions.EXIT, 0
        );
    }


    public static void main(String[] args) {

        Actions action = null;
        try {
            do {
                action = getAction();
                Executor.run(action);
            }
            while (action != Actions.EXIT);
        }
        catch(IOException| ParseException|ClassNotFoundException someExc){
            //!!!!I need to catch exceptions on all the levels!!
            System.out.println(someExc.getMessage());
        }
    }




        private static Actions getAction () {
            String line = IOUtill.readWithMessage("Enter the command");
            Actions action = Actions.valueOf(line.split(" ")[0].trim());
            return action;
        }


    }




