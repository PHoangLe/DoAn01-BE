package com.pescue.pescue.controller;

import com.pescue.pescue.dto.StringResponseDTO;
import com.pescue.pescue.service.StatisticService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/statistic")
@CrossOrigin
public class StatisticController {
    private final StatisticService statisticService;
    @PutMapping("/landing-page")
    public ResponseEntity<Object> landingPage(){
        return ResponseEntity.ok(statisticService.getLandingPageStatistc());
    }
}
