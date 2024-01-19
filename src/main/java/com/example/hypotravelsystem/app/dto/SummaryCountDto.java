package com.example.hypotravelsystem.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SummaryCountDto {
    private int completeTripCount;
    private int inCompleteTripCount;
    private int cancelledTripCount;
    private double totalCharge;
}
