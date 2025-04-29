package com.debug;

public enum Priorities {
    Critical(4),
    High(3),
    Medium(2),
    Low(1),
    All(0);

    private final int level;

    private Priorities(int level) {
        this.level = level;
    }

    public final int getLevel() {
        return this.level;
    }
}
