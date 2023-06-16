package com.pescue.pescue.controller;

import com.pescue.pescue.dto.ShelterDTO;
import com.pescue.pescue.dto.StringResponseDTO;
import com.pescue.pescue.exception.ExistedException;
import com.pescue.pescue.model.Shelter;
import com.pescue.pescue.service.ShelterService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/shelter")
@CrossOrigin
@Api
public class ShelterController {

    private final ShelterService shelterService;

    @PostMapping("/registerShelter")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> addShelter(@RequestBody ShelterDTO shelter) throws ExistedException {
        shelterService.registerShelter(new Shelter(shelter));
        return ResponseEntity.status(HttpStatus.CREATED).body(StringResponseDTO.builder()
                .message("Bạn đã gửi yêu cầu làm trại cứu hộ thành công. Vui lòng đợi 3-5 ngày để nhận được kết quả đăng ký")
                .build());
    }

    @GetMapping("/getAllShelter")
    public ResponseEntity<Object> getAllShelter(){
        return ResponseEntity.ok(shelterService.findAllShelter());

    }
    @GetMapping("/getShelterToApprove")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getShelterToApprove(){
        return ResponseEntity.ok(shelterService.findShelterToApprove());
    }

    @PostMapping("/approveShelter/{shelterID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> approveShelter(@PathVariable String shelterID){
        shelterService.approveShelter(shelterID);

        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Đã xác thực yêu cầu làm trại cứu trợ thành công")
                .build());
    }

    @PostMapping("/disapproveShelter/{shelterID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> disapproveShelter(@PathVariable String shelterID){
        shelterService.disapproveShelter(shelterID);

        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Đã từ chối yêu cầu làm trại cứu trợ thành công")
                .build());
    }

    @GetMapping("/getShelterByUserID/{userID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getShelterByUserID(@PathVariable String userID){
        Shelter shelter = shelterService.findShelterByUserID(userID);

        if (shelter == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StringResponseDTO.builder()
                    .message("Không tồn tại trại cứu trợ cần tìm")
                    .build());

        return ResponseEntity.ok(shelter);
    }

    @GetMapping("/getShelterByShelterID/{shelterID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getShelterByShelterID(@PathVariable String shelterID){
        Shelter shelter = shelterService.getShelterByShelterID(shelterID);

        if (shelter == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Không tồn tại trại cứu trợ cần tìm")
                    .build());

        return ResponseEntity.ok(shelter);
    }

    @GetMapping("/getShelterByShelterName/{shelterName}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getShelterByShelterName(@PathVariable String shelterName){
        Shelter shelter = shelterService.findShelterByShelterName(shelterName);

        if (shelter == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Không tồn tại trại cứu trợ cần tìm")
                    .build());

        return ResponseEntity.ok(shelter);
    }
}
