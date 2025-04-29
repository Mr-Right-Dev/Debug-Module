package com.debug;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

public class Logger {
    private static JSONArray logs = new JSONArray();
    private static JSONObject itemsPrioritys = new JSONObject();
    private static Priorities logPriority = Priorities.High;
    private static int maxLogLines = 500;
    private static int maxLogFiles = 0; // 0 to never erase.
    private static boolean notify = false;

    private static DateTimeFormatter formatterLogger = DateTimeFormatter.ofPattern("MM-dd-yyyy_HH-mm-ss");
    private static DateTimeFormatter formatterLog = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss.nn");
    private static String attachedDirectory = null;
    
    // Default Setter.
    static {
        if (itemsPrioritys.isEmpty()) { 
            itemsPrioritys.put("log", Priorities.All);
            itemsPrioritys.put("success", Priorities.Low);
            itemsPrioritys.put("warn", Priorities.Medium);
            itemsPrioritys.put("error", Priorities.High);
            itemsPrioritys.put("critical", Priorities.Critical);

            System.out.println("Debug module initialized sucessfully."+ConsoleColors.Blue.getColor()+"[ Made by Mr-Right-Dev ]"+ConsoleColors.Reset.getColor());
        }

        Logger.internalShutdownHook();
    }

    private static void internalShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("[INTERNAL]: Shutdown hook called, saving logs.");
            Logger.saveLogs();
        }));
    }

    private static void saveLogs() {
        if (Logger.attachedDirectory == null) {
            Logger.print(OutputType.warn, "No directory to save files, the logs is getting dumped.");
            return;
        }

        String time = Logger.formatterLogger.format(LocalDateTime.now()).toString();
        File savingFile = new File(Logger.attachedDirectory+"/logFile"+time+".json");
        JSONObject fileSave = new JSONObject();
        fileSave.put("timestamp", Logger.formatterLog.format(LocalDateTime.now()).toString());
        fileSave.put("logs", Logger.logs);
        String logsInString = fileSave.toString();
        Logger.logs.clear();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(savingFile))) {
            writer.write(logsInString);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while saving logs to file.");
            e.printStackTrace();
        }

        Logger.clearFiles();
   }

   private static void clearFiles() {
        if (Logger.maxLogFiles <= 0) {
            return;
        }

        File folder = new File(Logger.attachedDirectory);
        File[] logs = folder.listFiles();
        if (logs == null) {
            return;
        }

        if (logs.length < maxLogFiles) {
            return;
        }
        File oldestFile = null;
        LocalDateTime oldestTimestamp = LocalDateTime.now();
        for (File log : logs) {
            try (BufferedReader reader = new BufferedReader(new FileReader(log))) {
                String line;
                String rawJson = "";
                while ((line = reader.readLine()) != null) {
                    rawJson = rawJson+line;
                }

                JSONObject obj = new JSONObject(rawJson);
                if (!obj.has("timestamp")) {
                    continue;
                }

                LocalDateTime dateTime = LocalDateTime.parse(obj.getString("timestamp"), Logger.formatterLog);
                if (dateTime.isBefore(oldestTimestamp)) {
                    oldestTimestamp = dateTime;
                    oldestFile = log;
                }
            } catch (IOException e) {
                System.out.println("Error while reading log file. Path: "+log.toPath().toString());
                e.printStackTrace();
            }
        }

        if (oldestFile == null) {
            return;
        }

        if (!oldestFile.delete()) {
            System.out.println("Fail to delete oldest file.");
        }
   }

    private static String getStackTraceAsString(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
    
    private static void print(OutputType outputType, String message) {
        int currentLevel = ((Priorities) itemsPrioritys.get(outputType.name().toLowerCase())).getLevel();

        String time = Logger.formatterLog.format(LocalDateTime.now()).toString();

        if (Logger.logs.length() == 0 && Logger.attachedDirectory == null && Logger.notify == false) {
            Logger.notify = true;
            Logger.print(OutputType.warn, "No saving dirctory attached, all saved logs will be dumped after execution exit.");
        } 

        JSONObject logItem = new JSONObject();
        logItem.put("message", message);
        logItem.put("type", outputType.name());
        logItem.put("prioritie", Integer.toString(currentLevel));
        logItem.put("timestamp", time);
        Logger.logs.put(logItem);

        if (Logger.logs.length() >= Logger.maxLogLines) {
            Thread saver = new Thread(Logger::saveLogs, "logsaver");
            saver.setDaemon(false);
        }

        if (Logger.logPriority.getLevel() > currentLevel) {
            return;
        }

        String prefix = "["+time+"] "+outputType.getColor().getColor()+outputType.name().toUpperCase()+ConsoleColors.Reset.getColor()+": ";
        System.out.println(prefix+message);
    }

    /**
     * DEFAULT: maxLogLines: 500, maxLogFiles: 0 
     * @param maxLogLines totals logs per file.
     * @param maxLogFiles How much files need to start deleting the oldest. (0 = never)
     */
    public static void newLogSettings(int maxLogLines, int maxLogFiles) {
        Logger.maxLogFiles = maxLogFiles;
        Logger.maxLogLines = maxLogLines;
    }

    /**
     * Update the priority of the selected OutputType.
     * @param outputType
     * @param priority
     */
    public static void setPrioritys(OutputType outputType, Priorities priority) {
        Logger.itemsPrioritys.put(outputType.name(), priority);
    }

    /**
     * Attach an file path to save the logs. It uses File method.
     * @param path
     */
    public static void attachSavingFolder(String path) {
        Logger.attachedDirectory = path;
    }

    /**
     * Set the priority that make show up logs.
     * @param priority
     */
    public static void setLogPriority(Priorities priority) {
        Logger.logPriority = priority;
    }

    /**
     * Logs an message, comparing the required priority with the current.
     * It saves all messages. 
     * @param outputType
     * @param message
     */
    public static void log(OutputType outputType, String message) {
        Logger.print(outputType, message);
    }

    /**
     * Logs an message, comparing the required priority with the current.
     * @param outputType
     * @param message
     * @param throwable Show in the end of the message for context. (Message and Trace)
     */
    public static void log(OutputType outputType, String message, Throwable throwable) {
        Logger.print(outputType, message+" | Message: "+throwable.getMessage()+" Trace: "+Logger.getStackTraceAsString(throwable));
    }

    /**
     * Use this function to save all logs before an shutdown.
     */
    public static void shutdownHook() {
        Thread saver = new Thread(Logger::saveLogs, "logsaver");
        saver.setDaemon(false);
    }
}
