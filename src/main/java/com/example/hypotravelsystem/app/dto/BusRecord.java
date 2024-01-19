package com.example.hypotravelsystem.app.dto;

import com.example.hypotravelsystem.app.enums.StopStation;
import com.example.hypotravelsystem.app.enums.TouchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
public class BusRecord {
    private long id;
    private Date dateTime;
    private TouchType touchType;
    private StopStation stopStation;
    private String companyId;
    private String busId;
    private String pan;
}
