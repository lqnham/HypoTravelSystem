package com.example.hypotravelsystem.app.service.helper;

import com.example.hypotravelsystem.app.dto.SummaryDto;
import com.example.hypotravelsystem.app.dto.TravelInformationDto;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Slf4j
public class WriteCSVService {
    public void writeTravelInfoFile(List<TravelInformationDto> travelInformationDto, String outputFilePath, String[] headers) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath))) {
            writer.writeNext(headers);
            for (TravelInformationDto dto : travelInformationDto) {
                String[] data = {
                        dto.getStarted().toString(),
                        dto.getFinished().toString(),
                        String.valueOf(dto.getDurationSec()),
                        dto.getFromStopId() != null ? dto.getFromStopId().toString(): "",
                        dto.getToStopId() != null ? dto.getToStopId().toString(): "",
                        String.valueOf(dto.getChargeAmount()),
                        dto.getCompanyId(),
                        dto.getBusId(),
                        dto.getHashedPan(),
                        dto.getStatus()
                };
                writer.writeNext(data);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file", e);
        }
    }

    public void writeSummaryFile(List<SummaryDto> summaries, String outputFilePath, String[] headers) {
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        try (CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath))) {
            writer.writeNext(headers);
            for (SummaryDto dto : summaries) {
                String formattedDate = outputFormat.format(dto.getDate());
                String[] data = {
                        formattedDate,
                        dto.getCompanyId(),
                        dto.getBusId(),
                        String.valueOf(dto.getSummaryCountDto().getCompleteTripCount()),
                        String.valueOf(dto.getSummaryCountDto().getInCompleteTripCount()),
                        String.valueOf(dto.getSummaryCountDto().getCancelledTripCount()),
                        String.valueOf(dto.getSummaryCountDto().getTotalCharge())
                };
                writer.writeNext(data);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file", e);
        }
    }
}
