package com.example.hypotravelsystem.app.dto;

import com.example.hypotravelsystem.app.enums.TripStatus;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class UnProcessableDto {
    private Date started;
    private Date finished;
    private long durationSec;
    private String fromStopId;
    private String toStopId;
    private long ChargeAmount;
    private String CompanyId;
    private String BusId;
    private long HashedPan;
    private TripStatus status;
}
