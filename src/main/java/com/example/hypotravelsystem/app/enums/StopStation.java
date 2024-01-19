package com.example.hypotravelsystem.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum StopStation {
    STOP_A("StopA"), STOP_B("StopB"), STOP_C("StopC");
    private final String stopStation;

    public static StopStation fromString(String value) {
        for (StopStation stopType : StopStation.values()) {
            if (stopType.stopStation.equalsIgnoreCase(value.trim())) {
                return stopType;
            }
        }
        throw new IllegalArgumentException("Unknown StopType: " + value);
    }
}
