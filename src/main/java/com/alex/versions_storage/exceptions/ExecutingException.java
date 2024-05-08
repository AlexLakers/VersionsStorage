package com.alex.versions_storage.exceptions;

/**
 * It describes a general exception that will throw during executing some command.
 */
public class ExecutingException extends Exception {
    public ExecutingException(String message) {
        super(message);
    }

    public ExecutingException(String message, Throwable causeExc) {
        super(message, causeExc);
    }

}

