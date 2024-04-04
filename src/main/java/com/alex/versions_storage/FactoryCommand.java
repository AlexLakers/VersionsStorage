package com.alex.versions_storage;

import com.alex.versions_storage.commands.*;

public  final class FactoryCommand {

    public  static Command createCommand(Actions action){

        Command command=null;
        switch (action){
            case CREATE -> {
                command=new CreateCommand();
            }
            case COMPARE ->{
                command=new CompareCommand();
            }
            case ADD ->{
                command = new AddCommand();
            }
            case RESTORE ->{
                command=new RestoreCommand();
            }
            case EXIT -> {
             command =new ExitCommand();
            }
        }
        return command;
    }




}
