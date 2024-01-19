package com.example.hypotravelsystem.app.controller;

import com.example.hypotravelsystem.app.service.HypoTravelService;
import com.example.hypotravelsystem.constant.ApiVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/hypotravel")
@RequiredArgsConstructor
public class HypoTravelController {
    private final HypoTravelService service;

    @GetMapping("/app")
    public String processCsv() {
        if(service.processCSV()){
            return "Success";
        }else{
            return "Fail";
        }

    }

}
