package com.debug;

import java.io.File;

public class example {
  public static void main(String args[]) {
    String path = "src/serverlogs/"; // NOT RECOMENDED, this is just an example.
    new File(path).mkdir(); // Create an folder in case it doe'nst exists.
    Logger.attachSavingFolder(path); // Attach the folder into the logger.
    Logger.setLogPriority(Priorities.All); // The default value is High.
    
    // LOGS EXAMPLES:
    Debugger.success("System online, no error while starting up.");
    Debugger.log("Reached flag.");

    File folder = new File("/opt/");
    if (folder.mkdir()) {
        Debugger.success("Folder created with no errors.");
    } else {
        Debugger.critical("Fail to create file. ", new Exception("Required file have not been created."));
    }

    // SHUTDOWN LOGIC.
    Logger.shutdownHook(); // Attach to your shutdown logic.
  }
}