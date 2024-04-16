package com.alex.versions_storage;

import com.alex.versions_storage.commands.*;
import com.alex.versions_storage.utill.IOutill;

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
                command=new RestoreCommand(Path.of(line.split(" ")[0]));
            }
            case EXIT -> {
             command =new ExitCommand();
            }
        }
        return command;
    }




}
