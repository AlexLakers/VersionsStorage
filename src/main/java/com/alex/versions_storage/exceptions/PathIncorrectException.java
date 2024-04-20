package com.alex.versions_storage.exceptions;

//Exception describes invalid path
public class PathIncorrectException extends Exception {
    public PathIncorrectException(String message){
        super(message);
    }
    public PathIncorrectException(String message, Throwable cause){
        super(message,cause);
    }

}