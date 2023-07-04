package com.pescue.pescue.service;

import com.pescue.pescue.dto.AdminDashboardDTO;
import com.pescue.pescue.dto.LandingPageStatisticDTO;
import com.pescue.pescue.dto.ShelterDashboardDTO;
import com.pescue.pescue.model.*;
import com.pescue.pescue.model.constant.ApplicationStatus;
import com.pescue.pescue.model.constant.RescuePostStatus;
import com.pescue.pescue.model.constant.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.LongSummaryStatistics;
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
    private final RescuePostService rescuePostService;
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

    public ShelterDashboardDTO getShelterDashboardStatistic(String shelterID) {
        List<Animal> animals = animalService.getAnimalsByShelterID(shelterID);
        Shelter shelter = shelterService.getShelterByShelterID(shelterID);
        List<AdoptionApplication> adoptionApplications = adoptionService.getApplicationByShelterID(shelterID);
        List<FundTransaction> transactions = fundTransactionService.getTransactionByUserID(shelter.getUserID());
        List<RescuePost> rescuePosts = rescuePostService.getAllByShelterID(shelterID);

        Map<Integer, Long> adoptedAnimalByMonth = adoptionApplications.stream()
                .filter(application -> application.getApplicationStatus() == ApplicationStatus.COMPLETED)
                .collect(Collectors.groupingBy(application ->
                                application.getDate().toInstant().atZone(ZoneId.systemDefault()).getMonthValue(),
                        Collectors.counting()));

        Map<Integer, Long> adoptionRequestByMonth = adoptionApplications.stream()
                .collect(Collectors.groupingBy(application ->
                                application.getDate().toInstant().atZone(ZoneId.systemDefault()).getMonthValue(),
                        Collectors.counting()));

        Map<Integer, LongSummaryStatistics> totalOfFundReceivedByMonth = transactions.stream()
                .filter(fundTransaction -> fundTransaction.getTransactionType() == TransactionType.FUND_TO_USER)
                .collect(Collectors.groupingBy(transaction ->
                                transaction.getDate().toInstant().atZone(ZoneId.systemDefault()).getMonthValue(),
                        Collectors.summarizingLong(transaction ->
                                transaction.getValue().intValue())));

        Map<Object, Long> totalNumberOfRescuePostCompletedByMonth = rescuePosts.stream()
                .filter((post -> post.getStatus() == RescuePostStatus.COMPLETED))
                .collect(Collectors.groupingBy(post ->
                                post.getDate().toInstant().atZone(ZoneId.systemDefault()).getMonthValue(),
                        Collectors.counting()));

        Map<Boolean, Long> adoptedAndNotAdoptedAnimal = animals.stream()
                .collect(Collectors.groupingBy(Animal::isAdopted, Collectors.counting()));

        System.out.println(adoptedAndNotAdoptedAnimal);

        List<RescuePost> totalNumberOfRescuePostCompleted = rescuePosts.stream()
                .filter(post -> post.getStatus() == RescuePostStatus.COMPLETED)
                .toList();

        return ShelterDashboardDTO.builder()
                .adoptionRequestByMonth(adoptionRequestByMonth)
                .totalNumberOfAdoptedAnimal(adoptedAndNotAdoptedAnimal.getOrDefault(true, 0L))
                .totalNumberOfRescuePostCompleted(totalNumberOfRescuePostCompleted.size())
                .totalOfFundReceivedByMonth(totalOfFundReceivedByMonth)
                .totalNumberOfRescuePostCompletedByMonth(totalNumberOfRescuePostCompletedByMonth)
                .adoptedAnimalByMonth(adoptedAnimalByMonth)
                .totalNumbersOfAnimal(animals.size())
                .build();
    }
}
