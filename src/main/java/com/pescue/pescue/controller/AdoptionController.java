package com.pescue.pescue.controller;

import com.pescue.pescue.dto.AdoptionApplicationRequestDTO;
import com.pescue.pescue.dto.StringResponseDTO;
import com.pescue.pescue.exception.AnimalNotFoundException;
import com.pescue.pescue.exception.ShelterNotFoundException;
import com.pescue.pescue.exception.UserNotFoundException;
import com.pescue.pescue.service.AdoptionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/adopt")
@RequiredArgsConstructor
public class AdoptionController {
    private final AdoptionService service;
    // Offline Adoption
    @PostMapping("/sendAdoptRequest")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> createAdoptionRequest(AdoptionApplicationRequestDTO dto) {
        try{
            service.createAdoptionRequest(dto);
        }
        catch (UserNotFoundException | ShelterNotFoundException | AnimalNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringResponseDTO(e.getMessage()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống"));
        }

        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Đã gửi yêu cầu nhận nuôi bé thành công. Bạn vui lòng đợi để trại cứu trợ liên hệ bạn.")
                .build());
    }

    @GetMapping("/getAdoptionApplicationByShelterID/{shelterID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAdoptionApplicationByShelterID(@PathVariable String shelterID) {
        return ResponseEntity.ok(service.getApplicationByShelterID(shelterID));
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
        if (service.declineAdoptionRequest(adoptionApplicationID))
            return ResponseEntity.ok(StringResponseDTO.builder()
                    .message("Đã từ chối yêu cầu nhận nuôi của bé")
                    .build());
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(StringResponseDTO.builder()
                .message("Đã có lỗi đã xảy ra")
                .build());
    }

    //Online Adoption
    @PostMapping("/sendOnlineAdoptRequest")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> createOnlineAdoptionRequest(AdoptionApplicationRequestDTO dto) {
        if (service.createOnlineAdoptionRequest(dto))
            return ResponseEntity.ok(StringResponseDTO.builder()
                    .message("Đã gửi yêu cầu nhận nuôi bé thành công. Bạn vui lòng đợi 1-2 ngày để chúng tôi xử lý yêu cầu của bạn")
                    .build());
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(StringResponseDTO.builder()
                .message("Đã có lỗi đã xảy ra")
                .build());
    }
    @GetMapping("/getAllOnlineApplication")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAllOnlineApplication() {
        return ResponseEntity.ok(service.getAllOnlineApplication());
    }
    @PostMapping("/confirmOnlineAdoptionRequest/{adoptionApplicationID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> confirmOnlineAdoptionRequest(@PathVariable String adoptionApplicationID){
        if (service.confirmOnlineAdoptionRequest(adoptionApplicationID))
            return ResponseEntity.ok(StringResponseDTO.builder()
                    .message("Đã xác thực thành công yêu cầu nhận nuôi của bé")
                    .build());
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(StringResponseDTO.builder()
                .message("Đã có lỗi đã xảy ra")
                .build());
    }

    @PostMapping("/declineOnlineAdoptionRequest/{adoptionApplicationID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> declineOnlineAdoptionRequest(@PathVariable String adoptionApplicationID){
        if (service.declineOnlineAdoptionRequest(adoptionApplicationID))
            return ResponseEntity.ok(StringResponseDTO.builder()
                    .message("Đã từ chối yêu cầu nhận nuôi của bé")
                    .build());
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(StringResponseDTO.builder()
                .message("Đã có lỗi đã xảy ra")
                .build());
    }
}
