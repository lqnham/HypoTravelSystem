package com.example.hypotravelsystem.app.service.helper;

import com.example.hypotravelsystem.app.dto.TripTime;
import com.example.hypotravelsystem.app.enums.StopStation;
import org.springframework.stereotype.Service;

@Service
public class ChargeAmountCalculationService {

    public double calculationChargeAmount(TripTime touchOn, TripTime touchOff) {
        double chargeAmounts = 0;
        if(touchOn.getStopId() == StopStation.STOP_A && touchOff.getStopId() == StopStation.STOP_B){
            chargeAmounts = 4.5;
        } else if (touchOn.getStopId() == StopStation.STOP_A && touchOff.getStopId() == StopStation.STOP_C) {
            chargeAmounts = 8.45;
        } else if (touchOn.getStopId() == StopStation.STOP_B && touchOff.getStopId() == StopStation.STOP_C) {
            chargeAmounts = 6.25;
        }
        return chargeAmounts;
    }

    public double calculationChargeAmountMissingTouchOff(TripTime touchOn) {
        double chargeAmounts = 0;
        if(touchOn.getStopId() == StopStation.STOP_A){
            // Maybe to AB(4.5) or AC(8.45)
            chargeAmounts = Math.max(4.50 , 8.45);
        } else if (touchOn.getStopId() == StopStation.STOP_B ) {
            // Maybe to BA(4.5) or BC(6.45)
            chargeAmounts = Math.max(4.50 , 6.25);
        } else if (touchOn.getStopId() == StopStation.STOP_C ) {
            // Maybe to CB(6.25) or CA(8.45)
            chargeAmounts = Math.max(6.20 , 8.45);
        }
        return chargeAmounts;
    }
}
