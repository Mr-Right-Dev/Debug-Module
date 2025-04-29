# Advanced Debug

A custom Java debug module with configurable output levels, JSON-based logs, and future HTML interface support.

---

## 🚀 Features

- Output types: `log`, `warn`, `error`, `critical`.
- Adjustable priorities per output type.
- Logs stored as JSON.
- Stack trace capture.
- (Coming soon) HTML UI for visual log inspection in real time.

---
## 📦 Installation

### Maven
```xml
<dependency>
    <groupId>com.debug</groupId>
    <artifactId>debug</artifactId>
    <version>1.0</version>
</dependency>
```
---

## ⚙️ Usage
```java
import java.io.File;

import com.debug.Debugger;
import com.debug.Logger;

public class template {
  public static void main(String args[]) {
    new File("/opt/serverlogs/"); // Create an folder in case it doe'nst exists.
    Logger.attachSavingFolder("/opt/serverlogs/"); // Attach the folder into the logger.
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
```

## 📘 Documentation
# Logger Class Documentation

This document describes the functions used in the `Logger` class for handling log settings, priorities, and saving logs. The methods allow configuring log parameters, adjusting log priorities, and attaching a saving folder for logs.

## Methods

### `newLogSettings(int maxLogLines, int maxLogFiles)`
This method sets the maximum number of log lines per file and the maximum number of log files. If the number of log files exceeds this limit, the oldest files are deleted.

**Parameters:**
- `maxLogLines`: The total number of log lines per file.
- `maxLogFiles`: The number of log files to keep. A value of `0` means never delete logs.

### `setPrioritys(OutputType outputType, Priorities priority)`
This method updates the priority for a specific `OutputType`.

**Parameters:**
- `outputType`: The type of output where logs are directed.
- `priority`: The priority level to set for the output type.

### `attachSavingFolder(String path)`
This method attaches a file path where the logs should be saved.

**Parameters:**
- `path`: The directory path where logs will be saved.

### `setLogPriority(Priorities priority)`
This method sets the priority level for logs to display. Only logs with this priority or higher will be shown.

**Parameters:**
- `priority`: The minimum priority level to display logs.

### `log(OutputType outputType, String message)`
This method logs a message, comparing its priority with the current priority to determine if it should be displayed. All messages are saved regardless of priority.

**Parameters:**
- `outputType`: The type of output (e.g., console, file).
- `message`: The message to log.

### `log(OutputType outputType, String message, Throwable throwable)`
This method logs a message along with the stack trace of an exception (`Throwable`). It compares the priority of the log message and includes additional information about the exception.

**Parameters:**
- `outputType`: The type of output (e.g., console, file).
- `message`: The message to log.
- `throwable`: The exception to log (its message and stack trace will be appended to the log).

### `shutdownHook()`
This method creates a thread that saves all logs before the application shuts down. The thread runs as a daemon thread and ensures that the logs are saved properly.

---

## Example Usage

```java
// Setting up log parameters
Logger.newLogSettings(1000, 5); // Max 1000 lines per file, 5 files

// Attaching a directory to save logs
Logger.attachSavingFolder("/path/to/logs");

// Setting log priority to ERROR
Logger.setLogPriority(Priorities.ERROR);

// Logging a message
Logger.log(OutputType.CONSOLE, "This is a test message");

// Logging an exception with message
try {
    throw new Exception("Test Exception");
} catch (Exception e) {
    Logger.log(OutputType.CONSOLE, "An error occurred", e);
}

// Saving logs before shutdown
Logger.shutdownHook();

