package com.debug;

public enum ConsoleColors {
    Reset("\u001B[0m"),
    Blue("\u001B[34m"),
    Green("\u001B[32m"),
    Cyan("\u001B[36m"),
    Yellow("\u001B[33m"),
    Red("\u001B[31m");

    private final String colorCode;

    private ConsoleColors(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColor() {
        return this.colorCode;
    }
}
