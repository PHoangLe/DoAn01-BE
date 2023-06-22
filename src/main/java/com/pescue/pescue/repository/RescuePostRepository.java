package com.pescue.pescue.repository;

import com.pescue.pescue.model.RescuePost;
import com.pescue.pescue.model.constant.RescuePostStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RescuePostRepository extends MongoRepository<RescuePost, String> {
    List<RescuePost> findAllByStatus (@Param("status")RescuePostStatus status);
    List<RescuePost> findAllByPoster (@Param("userID") String userID);
    List<RescuePost> findAllByRescuer (@Param("shelterID") String shelterID);
}
