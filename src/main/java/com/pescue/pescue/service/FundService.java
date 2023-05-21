package com.pescue.pescue.service;

import com.pescue.pescue.exception.UpdateFundException;
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
public class FundService {
    private final FundRepository fundRepository;
    private final FundTransactionRepository transactionRepository;

    public void createTransaction(String source, String destination, TransactionType type){
        try{
            if (type == TransactionType.FUND_TO_SHELTER) {

            }
            if (type == TransactionType.USER_TO_FUND){

            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public void createTransaction(String source, String destination, TransactionType type, Date date){

    }

    private void updateFundBalance(String transactionID, String fundID, BigDecimal value, TransactionType type) throws UpdateFundException {

    }
}
