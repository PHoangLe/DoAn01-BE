package com.pescue.pescue.repository;

import com.pescue.pescue.model.Donation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DonationRepository extends MongoRepository<Donation, String> {
}
