package com.pescue.pescue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardDTO {
    private long totalNumbersOfAnimal;
    private long totalNumbersOfShelter;
    private long totalNumbersOfUser;
    private BigDecimal totalFundBalance;
    private Map<Object, Long> adoptedAnimalByMonth;
    private Map<Integer, IntSummaryStatistics> totalOfFundReceivedByMonth;
    private Map<Integer, IntSummaryStatistics>  totalOfFundSentByMonth;
    private Map<Object, Long> totalOfShelterApprovedByMonth;
}
