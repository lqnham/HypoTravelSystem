package com.example.hypotravelsystem.constant;

import lombok.Getter;

public class HeaderConstant {
    @Getter
    private static final String[] tripsHeader = {"started", "finished", "DurationSec", "fromStopId", "toStopId", "ChargeAmount",
            "CompanyId", "BusId", "HashedPan", "Status"};

    @Getter
    public static String[] unProcessableHeader = {"started", "finished", "DurationSec", "fromStopId", "toStopId", "ChargeAmount",
            "CompanyId", "BusId", "HashedPan", "Status"};
    @Getter
    public static String[] summaryHeader = {"date", "CompanyId", "BusId", "CompleteTripCount", "IncompleteTripCount",
            "CancelledTripCount", "TotalCharges"};


}
