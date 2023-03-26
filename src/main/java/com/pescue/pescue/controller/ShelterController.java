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

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/shelter")
@CrossOrigin(origins = "**")
public class ShelterController {
    @Autowired
    private ShelterService shelterService;

    @PostMapping("/addShelter")
    public Object addShelter(ShelterDTO shelter){
        Shelter tempShelter = shelterService.findShelterByUserID(shelter.getUserID());

        if (tempShelter != null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tài khoản này đã là tài khoản Shelter");

        try {
            shelterService.addShelter(new Shelter(shelter));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Đã có lỗi xảy ra. Vui lòng thử lại sau");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Tài khoản của bạn đã trở thành tài khoản của trại cứu trợ");
    }

    @GetMapping("/getAllShelter")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public Object getAllShelter(){
        return ResponseEntity.ok(shelterService.findAllShelter());
    }
}
