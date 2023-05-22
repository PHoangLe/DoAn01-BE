package com.pescue.pescue.repository;

import com.pescue.pescue.model.Fund;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FundRepository extends MongoRepository<Fund, String> {
    Optional<Fund> findByFundID(@Param("fundID") String fundID);
}
