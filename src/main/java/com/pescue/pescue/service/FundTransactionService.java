package com.pescue.pescue.service;

import com.pescue.pescue.exception.UpdateFundException;
import com.pescue.pescue.model.Fund;
import com.pescue.pescue.model.FundTransaction;
import com.pescue.pescue.model.TransactionType;
import com.pescue.pescue.repository.FundRepository;
import com.pescue.pescue.repository.FundTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class FundTransactionService {
    private final FundRepository fundRepository;
    private final FundTransactionRepository transactionRepository;
    private final FundService fundService;
    public void createTransaction(TransactionType type, String source, String destination, BigDecimal value) throws UpdateFundException {
        if (type == TransactionType.FUND_TO_SHELTER) {
            updateFundBalance(source, value, type);
        }
        if (type == TransactionType.USER_TO_FUND){
            updateFundBalance(source, value.negate(), type);
        }

        transactionRepository.insert(new FundTransaction(type, source, destination, new Date(System.currentTimeMillis()), value));
        log.trace("Created transaction: Source: " + source + " Destination: " + destination + " Value: " + value +  " Type: " + type);
    }

    public void createTransaction(TransactionType type, String source, String destination, BigDecimal value, Date date) throws UpdateFundException {
        if (type == TransactionType.FUND_TO_SHELTER) {
            updateFundBalance(source, value, type);
        }
        if (type == TransactionType.USER_TO_FUND){
            updateFundBalance(source, value.negate(), type);
        }

        transactionRepository.insert(new FundTransaction(type, source, destination, date, value));
        log.trace("Created transaction: Source: " + source + " Destination: " + destination + " Value: " + value +  " Type: " + type);
    }

    private void updateFundBalance(String fundID, BigDecimal value, TransactionType type) throws UpdateFundException {
        Fund fund = fundService.getFundByFundID(fundID);

        BigDecimal currentBalance = fund.getFundBalance();
        currentBalance = currentBalance.add(value);

        fund.setFundBalance(currentBalance);
        try {
            fundRepository.save(fund);
            log.trace("Updated Fund balance: " + fundID + " Value: " + value + " Type: " + type);
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new UpdateFundException();
        }
    }
}
