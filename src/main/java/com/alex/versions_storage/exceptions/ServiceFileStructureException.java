package com.alex.versions_storage.exceptions;

/**
 * It describes an exception that will throw during executing some command
 * if the version storage(rootInfo.json) has an incorrect structure.
 */
public class ServiceFileStructureException extends Exception {
    public ServiceFileStructureException(String message) {
        super(message);
    }

    public ServiceFileStructureException(String message, Throwable cause) {
        super(message, cause);
    }
}
