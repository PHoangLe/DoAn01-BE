package com.pescue.pescue.service;


import com.pescue.pescue.dto.FundingRequestDTO;
import com.pescue.pescue.exception.*;
import com.pescue.pescue.model.*;
import com.pescue.pescue.model.constant.FundingRequestStatus;
import com.pescue.pescue.model.constant.TransactionType;
import com.pescue.pescue.repository.FundingRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FundingRequestService {
    private final FundingRequestRepository fundingRequestRepository;
    private final FundService fundService;
    private final UserService userService;
    private final ShelterService shelterService;
    private final FundTransactionService fundTransactionService;
    public FundingRequest createFundingRequest(FundingRequestDTO dto){
        Fund fund = fundService.getFundByFundID(dto.getFundID());
        Shelter shelter = shelterService.getShelterByShelterID(dto.getShelterID());
        User user = userService.getUserByID(shelter.getUserID());

        FundingRequest fundingRequest = new FundingRequest(fund, user, shelter, dto.getReason(), dto.getValue());
        return fundingRequestRepository.insert(fundingRequest);
    }
    public FundingRequest getFundingRequestByID(String requestID){
        return fundingRequestRepository.findById(requestID).orElseThrow(FundingRequestNotFoundException::new);
    }
    public List<FundingRequest> getAllFundingRequest(){
        return fundingRequestRepository.findAll();
    }
    public void confirmFundingRequest(String requestID) throws UpdateFundException, FundingStatusUpdateException, FundEmptyBalanceException {
        FundingRequest request = getFundingRequestByID(requestID);

        if (request.getRequestStatus() != FundingRequestStatus.PENDING)
            throw new FundingStatusUpdateException();

        Fund fund = request.getFund();
        BigDecimal value = request.getValue();

        if (fund.getFundBalance().compareTo(value) < 0)
            throw new FundEmptyBalanceException();

        fundTransactionService.createTransaction(TransactionType.FUND_TO_USER, fund.getFundID(), request.getUser(), value);
        request.setRequestStatus(FundingRequestStatus.COMPLETED);
        fundingRequestRepository.save(request);
    }
    public void rejectFundingRequest(String requestID) throws FundingStatusUpdateException {
        FundingRequest request = getFundingRequestByID(requestID);

        if (request.getRequestStatus() != FundingRequestStatus.PENDING)
            throw new FundingStatusUpdateException();

        request.setRequestStatus(FundingRequestStatus.REJECTED);
        fundingRequestRepository.save(request);
    }
}
