# VersionsStorage
## Description
  This app allows a user to manage diffirent versions of the selected root directory(<strong>srdir</strong>).
The app has all the necessary functions for it.At the moment you can use the fallowing functions:
 - <strong>ADD</strong> - addes a new version of data changes to the versions storage(rootInfo.json,database.dat).
 - <strong>COMPARE</strong> - compares data between the srdir and versions storage(rootInfo.json,database.dat).
 - <strong>CREATE</strong> - creates and inits a versions storage(rootInfo.json,database.dat,.VersionsStorage) in your OS.
 - <strong>RESTORE</strong> - restores a specific version of changes srdir from versions storage(rootInfo.json,database.dat).
 - <strong>EXIT</strong> - closes the application.

## How does it work?
Firstly, you should enter a specific command.After it you needs to enter required arguments,as a rule,it is the path to srdir and the version nummer of changes additionally for the restoring command.And then occurs the executing a specific command action. 
  <br>You need to execute the creating command(<strong>CREATE</strong>) to init the versions storage(rootInfo.json,database.dat,.VersionsStorage) in your OS.Also the data of 
srdir is saved to versions storage(database.dat) and specific information such as a number of version and hash code are saved to the versions storage(rootInfo.json).</br>
  <br>The hash code value is the main comparing component and this value calculate thanks to the deffined hash function.After creating if you change the data in the srdir then can use <strong>ADD</strong>-function a new version of these changes to the versions storage(rootInfo.json,database.dat) thus the new data,the new hash code value and the new version value(next) will be saved.In particular ,an adding process occurs if the srdir has unsaved changes.</br>
  <br>And here you can use another useful function is comparing function(<strong>COMPARE</strong>) which contains the algorithm for the search an any available changes in the srdir with using the hash code value.
And if some changes wasn't found thus you cant't use the adding function described early, but we can use the restoring function.</br>
  <br>The restoring process(<strong>RESTORE</strong>) began with the comparing process that was described early, and in dependence from the taked result restoring process will be started or stoped.During the restoring process occurs the restoring a specific data srdir from the versions storage(database.dat) by version of changes data which you enter as a arg in the console.The entered version is searhed in the versions storage(rootInfo.json) therefore you need to enter only the avaliable version.And it one more condition for the correct restoring process.</br>
  <br>Also this app has the system of handling exceptions.</br>

## The example(using the Linux OS) 
I preapared for this situation and created the directory "/home/alexlakers/test/myRoot/" which contains the following structure:
- myRoot
  - dir1
    - fil1.txt
  - dir2
    - file2.txt
  
After the creating command(CREATE) the versions storage init and now will be added a service dir ".VersionStorage": 
- myRoot
  - dir1
    - fil1.txt
  - dir2
    - file2.txt
  - .VersionsStorage
    - rootInfo.json
    - database1.dat
  
The file "/home/alexlakers/test/myRoot/.VersionsStorage/rootInfo.json" contains the following data:
```json
{
"rootPath":"/home/alexlakers/test/myRoot",
"versionsInfo":[{"hashCode":2126790656,"version":1}]
}
  ```
As you see the versions storage has been created and now you can use all the opportunities of the app. 
4) For example, if you try to use the comparing command(COMPARE) you'll see the following message:
"A checking directory [/home/alexlakers/test/myRoot] doesn't have unhandled changes".

Ok,and now I change file1.txt.It contains the string "some change text" instead of "some text". After the change we'll repeat the comparing command(COMPARE) and now we can see another message:
  "A checking directory [/home/alexlakers/test/myRoot] has an unhandled changes.You can use "ADD" to save all the changes".
I'm following this message and execute the adding command(ADD).After it will be created a new database in the versions storage:
...
- .VersionsStorage
    - rootInfo.json
    - database1.dat
    - database2.dat

And now the file "/home/alexlakers/test/myRoot/.VersionsStorage/rootInfo.json" contains the following data:
```json
{
"rootPath":"/home/alexlakers/test/myRoot",
"versionsInfo":[{"hashCode":2126790656,"version":1},
                {"hashCode":-2060460032,"version":2}]
}
```
Ok,now we can restore data which contains in the srdir.For example,we try to restore to the previous version of changes(1);
Performs restoring command(RESTORE) and we can see that file1.txt contains the string "some text" again.

> [!IMPORTANT]
>In this project I used the additional lib that helps me to work with .JSON:
> com.googlecode.json-simple https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple
