package com.pescue.pescue.controller;

import com.pescue.pescue.service.StatisticService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/statistic")
@CrossOrigin
@Api
public class StatisticController {
    private final StatisticService statisticService;
    @GetMapping("/landing-page")
    public ResponseEntity<Object> landingPage(){
        return ResponseEntity.ok(statisticService.getLandingPageStatistic());
    }
}
