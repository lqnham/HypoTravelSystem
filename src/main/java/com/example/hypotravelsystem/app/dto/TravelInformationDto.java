package com.example.hypotravelsystem.app.dto;

import com.example.hypotravelsystem.app.enums.StopStation;
import com.example.hypotravelsystem.app.enums.TripStatus;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@Builder
public class TravelInformationDto {
    private Date started;
    private Date finished;
    private long durationSec;
    private StopStation fromStopId;
    private StopStation toStopId;
    private double chargeAmount;
    private String companyId;
    private String busId;
    private String hashedPan;
    private String status;
    private TripStatus finalTripStatus;
}
