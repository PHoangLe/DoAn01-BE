package com.pescue.pescue.repository;

import com.pescue.pescue.model.FundingRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FundingRequestRepository extends MongoRepository<FundingRequest, String> {
}
