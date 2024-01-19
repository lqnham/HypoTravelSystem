package com.example.hypotravelsystem.app.service.helper;

import com.example.hypotravelsystem.app.dto.TripTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ValidationService {
    public boolean isTouchSameStopId(TripTime touchOn, TripTime touchOff) {
        return touchOn.getStopId() == touchOff.getStopId();
    }

    public boolean isNotDigit(String hash){
        String regex = "\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(hash);
        return !matcher.matches();
    }
}
