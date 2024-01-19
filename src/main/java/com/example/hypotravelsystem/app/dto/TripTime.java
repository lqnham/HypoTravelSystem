package com.example.hypotravelsystem.app.dto;

import com.example.hypotravelsystem.app.enums.StopStation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class TripTime {
    private String companyId;
    private StopStation stopId;
    private Date time;
    private boolean isTouchOn;
    private String pan;
}
