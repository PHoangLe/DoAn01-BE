package com.pescue.pescue.repository;

import com.pescue.pescue.model.FundingRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FundingRequestRepository extends MongoRepository<FundingRequest, String> {
    List<FundingRequest> findAllByUser(@Param("userID") String userID);
}
