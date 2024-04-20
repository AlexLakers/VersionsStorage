package com.alex.versions_storage.exceptions;

//Exception for an executing operation
public class ExecutingException extends Exception {

        public ExecutingException(String message){
            super(message);
        }
        public ExecutingException(String message, Throwable causeExc){
            super(message,causeExc);
        }

    }

