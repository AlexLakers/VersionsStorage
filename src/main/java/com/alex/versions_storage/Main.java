package com.alex.versions_storage;
import com.alex.versions_storage.commands.Command;
import com.alex.versions_storage.utill.IOUtill;
import org.json.simple.parser.ParseException;

import java.io.IOException;


public class Main {

    public static void main(String[] args)throws IOException, ParseException,ClassNotFoundException {

        Actions action=Actions.valueOf(IOUtill.readWithMessage("Enter the command:").trim());

            Command command = FactoryCommand.createCommand(action, IOUtill.readWithMessage("Enter args:").trim());
            command.execute();


    }



    }




