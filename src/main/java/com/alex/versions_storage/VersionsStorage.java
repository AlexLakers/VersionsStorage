package com.alex.versions_storage;
import com.alex.versions_storage.commands.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class VersionsStorage {

    public static void main(String[] args) {
        Actions action = null;
        do {
                action = getAction();
                Command command = FactoryCommand.createCommand(action);
                command.execute();    
        }
        while (action != Actions.EXIT);

    }

    private static Actions getAction() {
        System.out.println("Enter the command:");
        String line="";
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
            line=reader.readLine();
        }
        catch(IOException ioExc){
            System.out.println("ErrorIO");
        }
        Actions action = Actions.valueOf(line);
        return action;
    }


}




