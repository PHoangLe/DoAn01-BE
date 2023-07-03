package com.pescue.pescue.service;

import com.pescue.pescue.exception.UpdateFundException;
import com.pescue.pescue.model.Fund;
import com.pescue.pescue.model.FundTransaction;
import com.pescue.pescue.model.constant.TransactionType;
import com.pescue.pescue.model.User;
import com.pescue.pescue.repository.FundTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FundTransactionService {
    private final FundTransactionRepository transactionRepository;
    private final FundService fundService;
    public void createTransaction(TransactionType type, String fundID, User user, BigDecimal value) throws UpdateFundException {
        Fund fund = fundService.getFundByFundID(fundID);

        updateFundBalance(fund, value, type);

        FundTransaction transaction = new FundTransaction(type, fund, user, new Date(System.currentTimeMillis()), value);
        transactionRepository.insert(transaction);
        log.trace("New transaction: " + transaction);
    }

    public void createTransaction(TransactionType type, Fund fund, User user, BigDecimal value, Date date) throws UpdateFundException {
        updateFundBalance(fund, value, type);

        FundTransaction transaction = new FundTransaction(type, fund, user, date, value);
        transactionRepository.insert(transaction);
        log.trace("New transaction: " + transaction);
    }

    public List<FundTransaction> getTransactionByFundID(String fundID){
        return transactionRepository.findAllByFund(fundID);
    }

    public List<FundTransaction> getTransactionByUserID(String userID){
        return transactionRepository.findAllByUser(userID);
    }

    private void updateFundBalance(Fund fund, BigDecimal value, TransactionType type) throws UpdateFundException {
        BigDecimal currentBalance = fund.getFundBalance();
        currentBalance = type == TransactionType.USER_TO_FUND ? currentBalance.add(value) : currentBalance.subtract(value);

        fund.setFundBalance(currentBalance);
        try {
            fundService.updateFund(fund);
            log.trace("Updated Fund balance: " + fund.getFundID() + " Value: " + value + " Type: " + type);
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new UpdateFundException();
        }
    }
    public List<FundTransaction> getAllTransaction() {
        return transactionRepository.findAll();
    }
}
