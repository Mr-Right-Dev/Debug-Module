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
import com.debug.Debugger;
import com.debug.Logger;

public class template {
  public static void main(String args[]) {
    new File("/opt/serverlogs/"); // Create an folder in case it doe'nst exists.
    Logger.attachSavingFolder("/opt/serverlogs/"); // Attach the folder into the logger.
    Logger.setLogPriority(Priorities.All); // The default value is High.
    
    // LOGS EXAMPLES:
    Debugger.sucess("System online, no error while starting up.");
    Debugger.log("Reached flag.");

    File folder = new File("/opt/");
    if (folder.mkdir()) {
        Debugger.sucess("Folder created with no errors.");
    } else {
        Debugger.critical("Fail to create file. ", new Exception("Required file have not been created."));
    }

    // SHUTDOWN LOGIC.
    Logger.shutdownHook(); // Attach to your shutdown logic.
  }
}
```

## 📘 Documentation
