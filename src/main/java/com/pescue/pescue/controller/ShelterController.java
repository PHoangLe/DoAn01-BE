package com.pescue.pescue.controller;

import com.pescue.pescue.dto.ApproveShelterDTO;
import com.pescue.pescue.dto.ShelterDTO;
import com.pescue.pescue.model.Shelter;
import com.pescue.pescue.service.ShelterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/shelter")
@CrossOrigin
public class ShelterController {
    @Autowired
    private ShelterService shelterService;

    @PostMapping("/registerShelter")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> addShelter(@RequestBody ShelterDTO shelter){
        Shelter tempShelter = shelterService.findShelterByUserID(shelter.getUserID());

        if (tempShelter != null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tài khoản này đã là tài khoản Shelter");

        return shelterService.registerShelter(new Shelter(shelter));
    }

    @GetMapping("/getAllShelter")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAllShelter(){
        return ResponseEntity.ok(shelterService.findAllShelter());
    }

    @PostMapping("/approveShelter")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> approveShelter(@RequestBody ApproveShelterDTO approveShelterDTO){
        return shelterService.approveShelter(approveShelterDTO.getShelterID());
    }

    @GetMapping("/getShelterByUserID/{userID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getShelterByUserID(@PathVariable String userID){
        if (shelterService.findShelterByUserID(userID) == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tồn tại trại cứu trợ cần tìm");
        return ResponseEntity.ok(shelterService.findShelterByUserID(userID));
    }

    @GetMapping("/getShelterByShelterID/{shelterID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getShelterByShelterID(@PathVariable String shelterID){
        if (shelterService.findShelterByShelterID(shelterID) == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tồn tại trại cứu trợ cần tìm");
        return ResponseEntity.ok(shelterService.findShelterByShelterID(shelterID));
    }
}