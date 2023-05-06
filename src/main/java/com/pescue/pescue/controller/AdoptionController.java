package com.pescue.pescue.controller;

import com.pescue.pescue.dto.AdoptionApplicationRequestDTO;
import com.pescue.pescue.dto.StringResponseDTO;
import com.pescue.pescue.model.AdoptionApplication;
import com.pescue.pescue.service.AdoptionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/adopt")
@RequiredArgsConstructor
public class AdoptionController {
    private final AdoptionService service;
    @PostMapping("/sendAdoptRequest")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> createAdoptionRequest(AdoptionApplicationRequestDTO dto) {
        if (service.createAdoptionRequest(dto))
            return ResponseEntity.ok(StringResponseDTO.builder()
                    .message("Đã gửi yêu cầu nhận nuôi bé thành công. Bạn vui lòng đợi để trại cứu trợ liên hệ bạn.")
                    .build());
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(StringResponseDTO.builder()
                .message("Đã có lỗi đã xảy ra")
                .build());
    }

    @GetMapping("/getAdoptionApplicationByShelterID/{shelterID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAdoptionApplicationByShelterID(@PathVariable String shelterID) {
        return ResponseEntity.ok(service.findByShelterID(shelterID));
    }

    @PostMapping("/confirmAdoptionRequest/{adoptionApplicationID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> confirmAdoptionRequest(@PathVariable String adoptionApplicationID){
        if (service.confirmAdoptionRequest(adoptionApplicationID))
            return ResponseEntity.ok(StringResponseDTO.builder()
                    .message("Đã xác thực thành công yêu cầu nhận nuôi của bé")
                    .build());
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(StringResponseDTO.builder()
                .message("Đã có lỗi đã xảy ra")
                .build());
    }

    @PostMapping("/declineAdoptionRequest/{adoptionApplicationID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> declineAdoptionRequest(@PathVariable String adoptionApplicationID){
        if (service.confirmAdoptionRequest(adoptionApplicationID))
            return ResponseEntity.ok(StringResponseDTO.builder()
                    .message("Đã từ chối yêu cầu nhận nuôi của bé")
                    .build());
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(StringResponseDTO.builder()
                .message("Đã có lỗi đã xảy ra")
                .build());
    }
}
