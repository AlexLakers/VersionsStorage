package com.alex.versions_storage;
import com.alex.versions_storage.commands.Command;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



public class VersionsStorage {

    public static void main(String[] args){
        Actions action = null;
        action=getAction();
        do {
            action = getAction();
            Command command = FactoryCommand.createCommand(action);
            command.execute();
        }
        while (action != Actions.EXIT);

    }

    private static Actions getAction(){
        System.out.println("Enter the command:");
        Actions action=null;
        String line = "";
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                line =reader.readLine();
            } catch (IOException ioExc) {
                System.out.println("ErrorIO");
            }
            action = Actions.valueOf(line.trim());
            return action;
        }


    }




