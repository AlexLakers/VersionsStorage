package com.alex.versions_storage;

import com.alex.versions_storage.commands.*;

import java.nio.file.Path;

public  final class FactoryCommand {

    public  static Command createCommand(Actions action,String line){

        Command command=null;
        switch (action){
            case CREATE -> {
                command=new CreateCommand(Path.of(line.split(" ")[0]));
            }
            case COMPARE ->{
                command=new CompareCommand(Path.of(line.split(" ")[0]));
            }
            case ADD ->{
                command = new AddCommand(Path.of(line.split(" ")[0]));
            }
            case RESTORE ->{
                String[] args =line.split(" ");
                command=new RestoreCommand(Path.of(args[0]),Integer.parseInt(args[1]));
            }
            case EXIT -> {
             command =new ExitCommand();
            }
        }
        return command;
    }




}
