package com.example.hypotravelsystem.app.service.helper;

import com.example.hypotravelsystem.app.dto.BusRecord;
import com.example.hypotravelsystem.app.enums.StopStation;
import com.example.hypotravelsystem.app.enums.TouchType;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReadService {
    private final static String pathFile = "src/main/resources/touchData.csv";
    public List<BusRecord> fetchBusRecords(){
        return readCsvFile();
    }
    private static List<BusRecord> readCsvFile() {
        List<BusRecord> busRecords= new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(ReadService.pathFile))) {
            List<String[]> records = reader.readAll();
            records.remove(0);
            for (String[] record : records) {
                BusRecord busRecord = parseBusRecord(record);
                busRecords.add(busRecord);
            }
        } catch (CsvException| ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        return busRecords;
    }

    private static BusRecord parseBusRecord(String[] record) throws ParseException {
        int id = Integer.parseInt(record[0].trim());
        String dateTimeUTC = record[1];
        TouchType touchType = TouchType.valueOf(record[2].trim());
        StopStation stopID = StopStation.fromString(record[3].trim());
        String companyID = record[4].trim();
        String busID = record[5].trim();
        String pan = record[6].trim();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return new BusRecord(id, dateFormat.parse(dateTimeUTC), touchType, stopID, companyID, busID, pan);
    }

}
