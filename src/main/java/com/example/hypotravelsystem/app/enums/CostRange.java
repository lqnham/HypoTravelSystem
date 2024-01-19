package com.example.hypotravelsystem.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public enum CostRange {
    AB(4.5),BC(6.25),AC(8.45);
    private final double cost;
}
