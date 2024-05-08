package com.alex.versions_storage.exceptions;

/**
 * It describes an exception that will throw during executing some command
 * if the path used is incorrect.
 */
public class PathIncorrectException extends Exception {
    public PathIncorrectException(String message) {
        super(message);
    }

    public PathIncorrectException(String message, Throwable cause) {
        super(message, cause);
    }

}