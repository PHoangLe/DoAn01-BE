package com.pescue.pescue.controller;

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
@CrossOrigin(origins = "**")
public class ShelterController {
    @Autowired
    private ShelterService shelterService;

    @PostMapping("/registerShelter")
    public ResponseEntity<Object> addShelter(ShelterDTO shelter){
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
    public ResponseEntity<Object> approveShelter(@RequestBody String shelterID){
        return shelterService.approveShelter(shelterID);
    }
}
