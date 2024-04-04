package com.alex.versions_storage;
import java.nio.file.Path;

public final class Provider {
    
    private  Path rootPath;
    private Path servicePath;
    private int version;


    public Provider(Path rootpath, int version) {

    }


        //This method creates a Service directory if it doesn't exist.
        // Then this method reads all the structure of directories and nested data from the root directory.
        // And then the data is saved to database file.Specific information such as a number of version and hashCode are saved to service json-file.
    public void createStorage(){
    }


        //This method reads all the structure of directories and nested data from the root directory.
        // After it the data is compared with the data from service file.
    public void compareData() {
    }


    // This method reads all the structure of directories and nested data from the root directory.
    // And then the data is saved to database file also specific information such as a number of version and hashCode are saved to service-file.
    public void addData() {
    }
    
    //This method reads all the structure of directories and nested data from the root directory.
    // After it the data is compared with the data from service-file.
    // The comparing process is used hashCode.
    public void restoreData(){
        
    }




}
