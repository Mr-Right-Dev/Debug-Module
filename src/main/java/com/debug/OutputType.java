package com.debug;

public enum OutputType {
    log(ConsoleColors.Blue),
    success(ConsoleColors.Green),
    warn(ConsoleColors.Cyan),
    error(ConsoleColors.Yellow),
    critical(ConsoleColors.Red);

    private final ConsoleColors color;
    
    private OutputType(ConsoleColors color) {
        this.color = color;
    }

    public ConsoleColors getColor() {
        return this.color;
    }
}
