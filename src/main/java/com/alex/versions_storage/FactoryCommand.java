package com.alex.versions_storage;

import com.alex.versions_storage.commands.*;
import com.alex.versions_storage.provider.StorageManager;

import java.nio.file.Path;

public  final class FactoryCommand {

    public  static Command createCommand(Actions action,String line){

        Command command=null;
        switch (action){
            case CREATE -> {
                command=new CreateCommand(new StorageManager(Path.of(line.split(" ")[0])));;
            }
            case COMPARE ->{
                command=new CompareCommand(new StorageManager(Path.of(line.split(" ")[0])));
            }
            case ADD ->{
                command = new AddCommand(new StorageManager(Path.of(line.split(" ")[0])));
            }
            case RESTORE ->{
                String[] args =line.split(" ");
                command=new RestoreCommand(new StorageManager(Path.of(args[0])),Integer.parseInt(args[1]));
            }
            case EXIT -> {
             command =new ExitCommand();
            }
        }
        return command;
    }




}
