package com.alex.versions_storage.exceptions;


//Exception describes invalid structure of service file(json)
public class ServiceFileStructureException extends Exception {
    public ServiceFileStructureException(String message) {
        super(message);
    }

    public ServiceFileStructureException(String message, Throwable cause) {
        super(message, cause);
    }
}
