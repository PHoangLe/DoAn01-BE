package com.pescue.pescue.controller;

import com.pescue.pescue.dto.AdoptionApplicationDTO;
import com.pescue.pescue.dto.AdoptionApplicationRequestDTO;
import com.pescue.pescue.dto.StringResponseDTO;
import com.pescue.pescue.exception.*;
import com.pescue.pescue.model.AdoptionApplication;
import com.pescue.pescue.model.OnlineAdoptionApplication;
import com.pescue.pescue.service.AdoptionService;
import io.swagger.v3.core.util.Json;
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
public class AdoptionController {
    private final AdoptionService service;
    // Offline Adoption
    @PostMapping("/sendAdoptRequest")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> createAdoptionRequest(@RequestBody AdoptionApplicationRequestDTO dto) {
        try{
            service.createAdoptionRequest(dto);
        }
        catch (UserNotFoundException | ShelterNotFoundException | AnimalNotFoundException | ApplicationExistedException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringResponseDTO(e.getMessage()));
        }
        catch (Exception e){
            log.trace(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }

        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Đã gửi yêu cầu nhận nuôi bé thành công. Bạn vui lòng đợi để trại cứu trợ liên hệ bạn.")
                .build());
    }

    @GetMapping("/getAdoptionApplicationByShelterID/{shelterID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAdoptionApplicationByShelterID(@PathVariable String shelterID) {
        List<AdoptionApplication> applicationByShelterID;
        try {
            applicationByShelterID = service.findApplicationByShelterID(shelterID);
        }
        catch (UserNotFoundException | ShelterNotFoundException | AnimalNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringResponseDTO(e.getMessage()));
        }
        catch (Exception e){
            log.trace(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }

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

    @PostMapping("/confirmAdoptionRequest/{adoptionApplicationID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> confirmAdoptionRequest(@PathVariable String adoptionApplicationID){
        try{
            service.confirmAdoptionRequest(adoptionApplicationID);
        }
        catch (UserNotFoundException | ShelterNotFoundException | AnimalNotFoundException | ApplicationNotFoundException | SendMailFailedException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringResponseDTO(e.getMessage()));
        }
        catch (Exception e){
            log.trace(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }

        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Đã xác thực thành công yêu cầu nhận nuôi của bé")
                .build());
    }

    @PostMapping("/declineAdoptionRequest/{adoptionApplicationID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> declineAdoptionRequest(@PathVariable String adoptionApplicationID){
        try {
            service.declineAdoptionRequest(adoptionApplicationID);
        }
        catch (UserNotFoundException | ShelterNotFoundException | AnimalNotFoundException | ApplicationNotFoundException | SendMailFailedException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringResponseDTO(e.getMessage()));
        }
        catch (Exception e){
            log.trace(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }

        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Đã từ chối yêu cầu nhận nuôi của bé")
                .build());
    }

    //Online Adoption
    @PostMapping("/sendOnlineAdoptRequest")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> createOnlineAdoptionRequest(@RequestBody AdoptionApplicationRequestDTO dto) {
        try{
            service.createOnlineAdoptionRequest(dto);
        }
        catch (UserNotFoundException | ShelterNotFoundException | AnimalNotFoundException | ApplicationNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringResponseDTO(e.getMessage()));
        }
        catch (Exception e){
            log.trace(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }

        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Đã gửi yêu cầu nhận nuôi bé thành công. Bạn vui lòng đợi 1-2 ngày để chúng tôi xử lý yêu cầu của bạn")
                .build());
    }
    @GetMapping("/getAllOnlineApplication")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAllOnlineApplication() {
        List<OnlineAdoptionApplication> allOnlineApplication;
        try {
            allOnlineApplication = service.getAllOnlineApplication();
        }
        catch (Exception e){
            log.trace(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }

        return ResponseEntity.ok(allOnlineApplication);
    }
    @PostMapping("/confirmOnlineAdoptionRequest/{adoptionApplicationID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> confirmOnlineAdoptionRequest(@PathVariable String adoptionApplicationID){
        try {
            service.confirmOnlineAdoptionRequest(adoptionApplicationID);
        }
        catch (UserNotFoundException | ShelterNotFoundException | AnimalNotFoundException | ApplicationNotFoundException | SendMailFailedException | UpdateFundException | ApplicationStatusException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringResponseDTO(e.getMessage()));
        }
        catch (Exception e){
            log.trace(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }

        return ResponseEntity.ok(StringResponseDTO.builder()
            .message("Đã xác thực thành công yêu cầu nhận nuôi của bé")
            .build());
    }

    @PostMapping("/declineOnlineAdoptionRequest/{adoptionApplicationID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> declineOnlineAdoptionRequest(@PathVariable String adoptionApplicationID){
        try{
            service.declineOnlineAdoptionRequest(adoptionApplicationID);
        }
        catch (UserNotFoundException | ShelterNotFoundException | AnimalNotFoundException | ApplicationNotFoundException | SendMailFailedException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringResponseDTO(e.getMessage()));
        }
        catch (Exception e){
            log.trace(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }

        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Đã từ chối yêu cầu nhận nuôi của bé")
                .build());
    }
    @GetMapping("/getOnlineAdoptionApplicationByUserIDAndAnimalID/{userID}/{animalID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getOnlineAdoptionApplicationByUserIDAndAnimalID(@PathVariable String userID, @PathVariable String animalID) {
        try {
            OnlineAdoptionApplication application = service.findOnlineApplicationByUserIDAndAnimalID(userID, animalID);

            if (application != null)
                ResponseEntity.ok(application);
        }
        catch (Exception e){
            log.trace(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringResponseDTO("Chưa tồn tại yêu cầu nhận nuôi"));
    }
}
