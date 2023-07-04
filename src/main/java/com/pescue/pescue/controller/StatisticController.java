package com.pescue.pescue.controller;

import com.pescue.pescue.service.StatisticService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @GetMapping("/admin-dashboard")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> adminDashboard(){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(statisticService.getAdminDashboardStatistic());
    }

    @GetMapping("/shelter-dashboard/{shelterID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> shelterDashboard(@PathVariable String shelterID){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(statisticService.getShelterDashboardStatistic(shelterID));
    }
}
