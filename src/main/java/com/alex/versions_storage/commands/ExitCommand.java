package com.alex.versions_storage.commands;


public class ExitCommand implements Command{

    public void execute(){
        System.out.println("The version storage is closed");
    }
}
