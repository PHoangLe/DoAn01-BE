package com.pescue.pescue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LongSummaryStatistics;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShelterDashboardDTO {
    private long totalNumbersOfAnimal;
    private long totalNumberOfAdoptedAnimal;
    private long totalNumberOfRescuePostCompleted;
    private Map<Integer, Long> adoptedAnimalByMonth;
    private Map<Integer, Long> adoptionRequestByMonth;
    private Map<Object, Long> totalNumberOfRescuePostCompletedByMonth;
    private Map<Integer, LongSummaryStatistics> totalOfFundReceivedByMonth;
}
