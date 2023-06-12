package com.pescue.pescue.service;

import com.pescue.pescue.dto.FundDTO;
import com.pescue.pescue.exception.FundNotFoundException;
import com.pescue.pescue.model.Fund;
import com.pescue.pescue.repository.FundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FundService {
    private final FundRepository fundRepository;
    public void createFund(FundDTO dto){
        fundRepository.insert(new Fund(dto));
    }

    public Fund getFundByFundID(String fundID) throws FundNotFoundException{
        log.trace("Finding fund with ID: " + fundID);
        return fundRepository.findByFundID(fundID).orElseThrow(FundNotFoundException::new);
    }

    public List<Fund> getAllFund(){
        return fundRepository.findAll();
    }

    public void updateFund(Fund fund) {
        fundRepository.save(fund);
    }

    public void deleteFund(String fundID) {
        fundRepository.deleteById(fundID);
    }
}
