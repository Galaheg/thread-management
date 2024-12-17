package com.hemre.receiverthread.enums;

public enum ThreadTypeEnum {

    RECEIVER("Receiver"),
    SENDER("Sender");

    private final String displayName;

    ThreadTypeEnum(String displayName){
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
