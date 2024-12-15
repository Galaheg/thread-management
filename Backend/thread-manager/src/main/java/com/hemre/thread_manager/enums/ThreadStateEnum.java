package com.hemre.thread_manager.enums;

public enum ThreadStateEnum {

    RUNNING("Running"),
    STOPPED("Stopped"),
    WAITING("Waiting");

    private final String displayName;

    ThreadStateEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

}
