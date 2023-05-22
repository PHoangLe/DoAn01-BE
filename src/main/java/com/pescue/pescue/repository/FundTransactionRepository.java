package com.pescue.pescue.repository;

import com.pescue.pescue.model.FundTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FundTransactionRepository extends MongoRepository<FundTransaction, String> {
    Optional<FundTransaction> findBySourceOrDestination(@Param("source") String source, @Param("destination") String destination);
}
