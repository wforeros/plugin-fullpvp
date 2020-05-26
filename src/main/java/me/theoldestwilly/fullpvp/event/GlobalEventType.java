package me.theoldestwilly.fullpvp.event;

import java.util.Arrays;
import java.util.List;

public enum GlobalEventType {
    KOTH, DTC, HOSTED, DROPPER;

    public static GlobalEventType fromString(String s) {
        for (GlobalEventType type : GlobalEventType.values()) {
            if (type.toString().equalsIgnoreCase(s)) return type;
        }
        return null;
    }

    public static List<String > getValuesAsString() {
        return Arrays.asList(KOTH.toString().toLowerCase(), DTC.toString().toLowerCase(),
                DROPPER.toString().toLowerCase());
    }
}
