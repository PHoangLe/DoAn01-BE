package com.pescue.pescue.controller;

import com.pescue.pescue.dto.AdoptionApplicationRequestDTO;
import com.pescue.pescue.dto.StringResponseDTO;
import com.pescue.pescue.exception.*;
import com.pescue.pescue.model.AdoptionApplication;
import com.pescue.pescue.model.OnlineAdoptionApplication;
import com.pescue.pescue.service.AdoptionService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/adopt")
@RequiredArgsConstructor
@Slf4j
@Api
public class AdoptionController {
    private final AdoptionService service;
    // Offline Adoption
    @PostMapping("/sendAdoptRequest")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> createAdoptionRequest(@RequestBody AdoptionApplicationRequestDTO dto) throws ExistedException {
        service.createAdoptionRequest(dto);

        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Đã gửi yêu cầu nhận nuôi bé thành công. Bạn vui lòng đợi để trại cứu trợ liên hệ bạn.")
                .build());
    }

    @GetMapping("/getAdoptionApplicationByShelterID/{shelterID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAdoptionApplicationByShelterID(@PathVariable String shelterID) {
        List<AdoptionApplication> applicationByShelterID = service.findApplicationByShelterID(shelterID);
        return ResponseEntity.ok(applicationByShelterID);
    }

    @GetMapping("/getAdoptionApplicationByUserIDAndAnimalID/{userID}/{animalID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAdoptionApplicationByUserIDAndAnimalID(@PathVariable String userID, @PathVariable String animalID) {
        try {
            AdoptionApplication application = service.findApplicationByUserIDAndAnimalID(userID, animalID);

            if (application != null)
                return ResponseEntity.ok(application);
        }
        catch (Exception e){
            log.trace(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringResponseDTO("Chưa tồn tại yêu cầu nhận nuôi"));
    }

    @PutMapping("/confirmAdoptionRequest/{adoptionApplicationID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> confirmAdoptionRequest(@PathVariable String adoptionApplicationID) throws ExistedException {
        service.confirmAdoptionRequest(adoptionApplicationID);
        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Đã xác thực thành công yêu cầu nhận nuôi của bé")
                .build());
    }

    @PutMapping("/declineAdoptionRequest/{adoptionApplicationID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> declineAdoptionRequest(@PathVariable String adoptionApplicationID){
        service.declineAdoptionRequest(adoptionApplicationID);
        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Đã từ chối yêu cầu nhận nuôi của bé")
                .build());
    }

    //Online Adoption
    @PostMapping("/sendOnlineAdoptRequest")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> createOnlineAdoptionRequest(@RequestBody AdoptionApplicationRequestDTO dto) throws ExistedException {
        service.createOnlineAdoptionRequest(dto);
        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Đã gửi yêu cầu nhận nuôi bé thành công. Bạn vui lòng đợi 1-2 ngày để chúng tôi xử lý yêu cầu của bạn")
                .build());
    }
    @GetMapping("/getAllOnlineApplication")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAllOnlineApplication() {
        List<OnlineAdoptionApplication> allOnlineApplication = service.getAllOnlineApplication();
        return ResponseEntity.ok(allOnlineApplication);
    }

    @GetMapping("/getOnlineAdoptionsByUserID/{userID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getOnlineAdoptionsByUserID(@PathVariable String userID) {
        List<OnlineAdoptionApplication> onlineAdoptionApplicationsByUserID = service.getOnlineAdoptionsByUserID(userID);
        return ResponseEntity.ok(onlineAdoptionApplicationsByUserID);
    }
    @PutMapping("/confirmOnlineAdoptionRequest/{adoptionApplicationID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> confirmOnlineAdoptionRequest(@PathVariable String adoptionApplicationID) throws ApplicationStatusUpdateException, UpdateFundException, ExistedException {
        service.confirmOnlineAdoptionRequest(adoptionApplicationID);
        return ResponseEntity.ok(StringResponseDTO.builder()
            .message("Đã xác thực thành công yêu cầu nhận nuôi của bé")
            .build());
    }

    @PutMapping("/declineOnlineAdoptionRequest/{adoptionApplicationID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> declineOnlineAdoptionRequest(@PathVariable String adoptionApplicationID){
        service.declineOnlineAdoptionRequest(adoptionApplicationID);
        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Đã từ chối yêu cầu nhận nuôi của bé")
                .build());
    }
    @GetMapping("/getOnlineAdoptionApplicationByUserIDAndAnimalID/{userID}/{animalID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getOnlineAdoptionApplicationByUserIDAndAnimalID(@PathVariable String userID, @PathVariable String animalID) {
        OnlineAdoptionApplication application = service.getOnlineApplicationByUserIDAndAnimalID(userID, animalID);

        if (application != null) {
            return ResponseEntity.ok(application);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringResponseDTO("Chưa tồn tại yêu cầu nhận nuôi"));
    }
}
