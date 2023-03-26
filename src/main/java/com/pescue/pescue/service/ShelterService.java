package com.pescue.pescue.service;

import com.pescue.pescue.model.Shelter;
import com.pescue.pescue.repository.ShelterRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ShelterService {
    @Autowired
    ShelterRepository shelterRepository;

    public Shelter findShelterByUserID(String userID){
        return shelterRepository.findByUserID(userID).get();
    }

    public void addShelter(Shelter shelter){
        shelterRepository.insert(shelter);
    }

    public List<Shelter> findAllShelter(){
        return shelterRepository.findAll();
    }
}
