package com.example.hypotravelsystem.app.service.helper;

import com.example.hypotravelsystem.app.dto.SummaryCountDto;
import com.example.hypotravelsystem.app.dto.SummaryDto;
import com.example.hypotravelsystem.app.dto.TravelInformationDto;
import com.example.hypotravelsystem.app.dto.TripTime;
import com.example.hypotravelsystem.app.enums.TripStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcessDataService {
    private final HashService hashService;
    private final ChargeAmountCalculationService chargeAmountService;

    public void buildForTrips(TripTime touchOn, TripTime touchOff, String busId, String status, List<TravelInformationDto> dataTrips, TripStatus finalTripStatus) {
        double chargeAmounts = chargeAmountService.calculationChargeAmount(touchOn, touchOff);
        Duration duration = buildDuration(touchOn, touchOff);
        TravelInformationDto dataTrip = TravelInformationDto.builder()
                .started(touchOn.getTime()).finished(touchOff.getTime())
                .durationSec(duration.getSeconds())
                .fromStopId(touchOn.getStopId()).toStopId(touchOff.getStopId())
                .companyId(touchOn.getCompanyId()).busId(busId)
                .chargeAmount(chargeAmounts)
                .hashedPan(hashService.sha256Hash(touchOn.getPan())).status(status)
                .finalTripStatus(finalTripStatus)
                .build();
        dataTrips.add(dataTrip);
    }

    public void handleMissingTouchOff(TripTime touchOn, String busId, List<TravelInformationDto> unProcessTrips,TripStatus finalTripStatus) {
        double chargeAmounts = chargeAmountService.calculationChargeAmountMissingTouchOff(touchOn);
        TravelInformationDto dataTrip = TravelInformationDto.builder()
                .started(touchOn.getTime()).finished(touchOn.getTime())
                .durationSec(0)
                .fromStopId(touchOn.getStopId()).toStopId(null)
                .companyId(touchOn.getCompanyId()).busId(busId)
                .chargeAmount(chargeAmounts)
                .hashedPan(hashService.sha256Hash(touchOn.getPan())).status("Touch was missing OFF")
                .finalTripStatus(finalTripStatus)
                .build();
        unProcessTrips.add(dataTrip);
    }

    public List<SummaryDto> buildSummaries(List<TravelInformationDto> dataTrips, List<TravelInformationDto> unProcessTrips) {
        List<TravelInformationDto> combinedTrips = new ArrayList<>();
        combinedTrips.addAll(dataTrips);
        combinedTrips.addAll(unProcessTrips);
        return new ArrayList<>(combinedTrips.stream()
                .sorted(Comparator.comparing(TravelInformationDto::getStarted)
                        .thenComparing(TravelInformationDto::getCompanyId)
                        .thenComparing(TravelInformationDto::getBusId))
                .collect(Collectors.groupingBy(
                        dto -> dto.getStarted() + "-" + dto.getCompanyId() + "-" + dto.getBusId(),
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    SummaryCountDto summaryCountDto = new SummaryCountDto(
                                            (int) list.stream().filter(dto -> dto.getFinalTripStatus() == TripStatus.COMPLETE).count(),
                                            (int) list.stream().filter(dto -> dto.getFinalTripStatus() == TripStatus.UNCOMPLETED).count(),
                                            (int) list.stream().filter(dto -> dto.getFinalTripStatus() == TripStatus.CANCELED).count(),
                                            list.stream().mapToDouble(TravelInformationDto::getChargeAmount).sum()
                                    );
                                    SummaryDto summaryDto = SummaryDto.builder()
                                            .date(list.get(0).getStarted())
                                            .companyId(list.get(0).getCompanyId())
                                            .busId(list.get(0).getBusId())
                                            .build();
                                    summaryDto.setSummaryCountDto(summaryCountDto);
                                    return summaryDto;
                                }
                        )
                ))
                .values());
    }

    private static Duration buildDuration(TripTime touchOn, TripTime touchOff) {
        LocalDateTime startTime = convertToDateViaInstant(touchOn.getTime())
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        LocalDateTime endTime = convertToDateViaInstant(touchOff.getTime())
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        return Duration.between(startTime, endTime);
    }

    private static LocalDateTime convertToDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
