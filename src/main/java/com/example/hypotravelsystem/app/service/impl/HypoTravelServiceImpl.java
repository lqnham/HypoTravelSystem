package com.example.hypotravelsystem.app.service.impl;

import com.example.hypotravelsystem.app.dto.BusRecord;
import com.example.hypotravelsystem.app.dto.TravelInformationDto;
import com.example.hypotravelsystem.app.dto.TripTime;
import com.example.hypotravelsystem.app.enums.TouchType;
import com.example.hypotravelsystem.app.enums.TripStatus;
import com.example.hypotravelsystem.app.service.HypoTravelService;
import com.example.hypotravelsystem.app.service.helper.ProcessDataService;
import com.example.hypotravelsystem.app.service.helper.ReadService;
import com.example.hypotravelsystem.app.service.helper.ValidationService;
import com.example.hypotravelsystem.app.service.helper.WriteCSVService;
import com.example.hypotravelsystem.constant.HeaderConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.hypotravelsystem.constant.MessageConstant.*;
import static com.example.hypotravelsystem.constant.PathConstant.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class HypoTravelServiceImpl implements HypoTravelService {
    private final ReadService readBusRecordService;
    private final WriteCSVService writeCSVService;
    private final ProcessDataService processDataService;
    private final ValidationService validationService;
    @Override
    public boolean processCSV() {
        List<TravelInformationDto> dataTrips = new ArrayList<>();
        List<TravelInformationDto> unProcessTrips = new ArrayList<>();
        processDetail(dataTrips,unProcessTrips);
        writeCSVService.writeTravelInfoFile(unProcessTrips, UN_PROCESS_FILE_PATH, HeaderConstant.getUnProcessableHeader());
        writeCSVService.writeTravelInfoFile(dataTrips, TRIPS_FILE_PATH, HeaderConstant.getTripsHeader());
        writeCSVService.writeSummaryFile(processDataService.buildSummaries(dataTrips, unProcessTrips),
                SUMMARY_FILE_PATH, HeaderConstant.getSummaryHeader());
        return true;
    }



    private void processDetail(List<TravelInformationDto> dataTrips, List<TravelInformationDto> unProcessTrips) {
        List<BusRecord>  busRecords = readBusRecordService.fetchBusRecords();
        Map<String, List<TripTime>> busTrips = buildInformationSplitByBusId(busRecords);
        for (Map.Entry<String, List<TripTime>> entry : busTrips.entrySet()) {
            List<TripTime> trips = entry.getValue();
            for (int i = 0; i < trips.size(); i += 2) {
                TripTime touchOn = trips.get(i);
                TripTime touchOff = i + 1 < trips.size() ? trips.get(i + 1) : null;
                if (touchOff != null) {
                    if(validationService.isTouchSameStopId(touchOn, touchOff)) {
                        processDataService.buildForTrips(touchOn, touchOff, entry.getKey(), TOUCH_ON_AND_TOUCH_OFF_IS_THE_SAME_MESSAGE, unProcessTrips, TripStatus.CANCELED);
                    }else if(StringUtils.isEmpty(touchOn.getPan())) {
                        processDataService.buildForTrips(touchOn, touchOff, entry.getKey(), TOUCH_WAS_MISSING_PAN_MESSAGE, unProcessTrips, TripStatus.COMPLETE);
                    }else if(validationService.isNotDigit(touchOn.getPan()) || validationService.isNotDigit(touchOff.getPan())){
                        processDataService.buildForTrips(touchOn, touchOff, entry.getKey(), TOUCH_WAS_INVALID_PAN_MESSAGE, unProcessTrips,TripStatus.COMPLETE);
                    }else{
                        processDataService.buildForTrips(touchOn, touchOff, entry.getKey(), TripStatus.COMPLETE.toString(), dataTrips, TripStatus.COMPLETE);
                    }
                } else{
                    processDataService.handleMissingTouchOff(touchOn, entry.getKey(), unProcessTrips,TripStatus.UNCOMPLETED);
                }

            }
        }
    }

    private static Map<String, List<TripTime>> buildInformationSplitByBusId(List<BusRecord> busRecords) {
        Map<String, List<TripTime>> busTrips = new HashMap<>();
        for (BusRecord record : busRecords) {
            String busId = record.getBusId();
            List<TripTime> trips = busTrips.computeIfAbsent(busId, k -> new ArrayList<>());
            if (TouchType.ON == record.getTouchType()) {
                TripTime tripTime = TripTime.builder()
                        .stopId(record.getStopStation()).companyId(record.getCompanyId())
                        .time(record.getDateTime()).isTouchOn(true).pan(record.getPan())
                        .build();
                trips.add(tripTime);
            } else if (TouchType.OFF == record.getTouchType()) {
                TripTime tripTime = TripTime.builder()
                        .stopId(record.getStopStation()).companyId(record.getCompanyId())
                        .time(record.getDateTime()).isTouchOn(false).pan(record.getPan())
                        .build();
                trips.add(tripTime);
            }
        }
        return busTrips;
    }
}
