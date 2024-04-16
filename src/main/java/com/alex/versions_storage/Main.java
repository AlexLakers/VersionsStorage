package com.alex.versions_storage;
import com.alex.versions_storage.commands.Command;
import com.alex.versions_storage.utill.IOutill;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;


public class Main {

    public static void main(String[] args)throws IOException, ParseException,ClassNotFoundException {

        System.out.println("Enter the command:");
        Actions action=Actions.valueOf(IOutill.readConsole().trim());

            Command command = FactoryCommand.createCommand(action, IOutill.readConsole());
            command.execute();


    }

    private static Actions getAction()throws IOException{
        System.out.println("Enter the command:");
        Actions action=null;
            String line=IOutill.readConsole();
            return Actions.valueOf(line);
        }


    }




