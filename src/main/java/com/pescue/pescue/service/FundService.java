package com.pescue.pescue.service;

import com.pescue.pescue.exception.FundNotFoundException;
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
public class FundService {
    private final FundRepository fundRepository;
    public void createFund(String fundName, String fundDescription){
        fundRepository.insert(new Fund(fundName, fundDescription));
    }

    public Fund getFundByFundID(String fundID) throws FundNotFoundException{
        return fundRepository.findByFundID(fundID).orElseThrow(FundNotFoundException::new);
    }
}
