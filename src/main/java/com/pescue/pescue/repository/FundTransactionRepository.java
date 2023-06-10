package com.pescue.pescue.repository;

import com.pescue.pescue.model.FundTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FundTransactionRepository extends MongoRepository<FundTransaction, String> {
    List<FundTransaction> findAllByFund(@Param("fundID") String fundID);
}
