package com.example.hypotravelsystem.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class SummaryDto {
    private Date date;
    private String companyId;
    private String busId;
    private SummaryCountDto summaryCountDto;
}
