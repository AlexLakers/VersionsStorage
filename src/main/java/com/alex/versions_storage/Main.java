package com.alex.versions_storage;

import com.alex.versions_storage.exceptions.ExecutingException;
import com.alex.versions_storage.utill.IOUtill;



public class Main {

    public static void main(String[] args) {
        Actions action = null;
        do {
            try {
                action = getAction();
                Executor.run(action);
            } catch (IllegalArgumentException iae) {
                IOUtill.writeString("WARNING:" + iae.getMessage());
            } catch (ExecutingException e) {
                action = Actions.EXIT;
                IOUtill.writeString("ERROR:" + e.getMessage());
            }
        }
        while (action != Actions.EXIT);
    }

    private static Actions getAction() {
        String line = IOUtill.readWithMessage("Enter the command");
        Actions action = Actions.valueOf(line.split(" ")[0].trim());
        return action;
    }
}




