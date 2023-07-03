package com.pescue.pescue.service;

import com.pescue.pescue.dto.AdminDashboardDTO;
import com.pescue.pescue.dto.LandingPageStatisticDTO;
import com.pescue.pescue.model.*;
import com.pescue.pescue.model.constant.ApplicationStatus;
import com.pescue.pescue.model.constant.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticService {
    private final AnimalService animalService;
    private final ShelterService shelterService;
    private final FundTransactionService fundTransactionService;
    private final AdoptionService adoptionService;
    private final FundService fundService;
    private final UserService userService;
    public LandingPageStatisticDTO getLandingPageStatistic(){
        long[] animalCount = animalService.countAdoptedAndNotAdopted();

        long adoptedAnimalCount = animalCount[0];
        long notAdoptedAnimalCount = animalCount[1];
        long shelterCount = shelterService.countExistingShelter();

        return new LandingPageStatisticDTO(shelterCount, adoptedAnimalCount, notAdoptedAnimalCount);
    }

    public AdminDashboardDTO getAdminDashboardStatistic() {
        List<FundTransaction> fundTransactions = fundTransactionService.getAllTransaction();
        List<Shelter> shelters = shelterService.findAllShelter();
        List<AdoptionApplication> adoptionApplications = adoptionService.getAllApplication();
        List<Fund> funds = fundService.getAllFund();
        List<User> users = userService.getAllUser();
        List<Animal> animals = animalService.getAllAnimals();

        Map<Integer, IntSummaryStatistics> totalFundReceivedByMonth = fundTransactions.stream()
                .filter(transaction -> transaction.getTransactionType() == TransactionType.USER_TO_FUND)
                .collect(Collectors.groupingBy(transaction ->
                                transaction.getDate().toInstant().atZone(ZoneId.systemDefault()).getMonthValue(),
                        Collectors.summarizingInt(transaction ->
                                transaction.getValue().intValue())));

        Map<Integer, IntSummaryStatistics> totalFundSentByMonth = fundTransactions.stream()
                .filter(transaction -> transaction.getTransactionType() == TransactionType.FUND_TO_USER)
                .collect(Collectors.groupingBy(transaction ->
                                transaction.getDate().toInstant().atZone(ZoneId.systemDefault()).getMonthValue(),
                        Collectors.summarizingInt(transaction ->
                                transaction.getValue().intValue())));

        Map<Object, Long> totalOfShelterApprovedByMonth = shelters.stream()
                .filter(Shelter::getIsApproved)
                .collect(Collectors.groupingBy(shelter ->
                                shelter.getDate().toInstant().atZone(ZoneId.systemDefault()).getMonthValue(),
                        Collectors.counting()));

        Map<Object, Long> adoptedAnimalByMonth = adoptionApplications.stream()
                .filter(application -> application.getApplicationStatus() == ApplicationStatus.COMPLETED)
                .collect(Collectors.groupingBy(application ->
                                application.getDate().toInstant().atZone(ZoneId.systemDefault()).getMonthValue(),
                        Collectors.counting()));

        BigDecimal totalFundBalance = funds.stream()
                .map(Fund::getFundBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalShelter = shelters.stream()
                .filter(Shelter::getIsApproved)
                .count();

        long totalUser = users.stream()
                .filter(u -> !u.isLocked() && !u.isDeleted())
                .count();

        long totalAnimal = animalService.countAdoptedAndNotAdopted()[1];

        return AdminDashboardDTO.builder()
                .totalOfFundReceivedByMonth(totalFundReceivedByMonth)
                .totalOfFundSentByMonth(totalFundSentByMonth)
                .totalOfShelterApprovedByMonth(totalOfShelterApprovedByMonth)
                .adoptedAnimalByMonth(adoptedAnimalByMonth)
                .totalFundBalance(totalFundBalance)
                .totalNumbersOfShelter(totalShelter)
                .totalNumbersOfUser(totalUser)
                .totalNumbersOfAnimal(totalAnimal)
                .build();

    }
}
