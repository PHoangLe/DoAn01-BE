package com.pescue.pescue.controller;

import com.pescue.pescue.dto.DonationDTO;
import com.pescue.pescue.dto.StringResponseDTO;
import com.pescue.pescue.exception.DonationCompletedException;
import com.pescue.pescue.exception.DonationDeclinedException;
import com.pescue.pescue.exception.DonationNotFoundException;
import com.pescue.pescue.exception.UpdateFundException;
import com.pescue.pescue.model.Donation;
import com.pescue.pescue.service.DonationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/donations")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class DonationController {
    private final DonationService donationService;
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> createDonation(DonationDTO dto){
        try {
            Donation donation = donationService.createDonation(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(donation);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }
    }
    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAllDonations(){
        try {
            List<Donation> donations = donationService.getAllDonation();
            return ResponseEntity.status(HttpStatus.OK).body(donations);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }
    }
    @GetMapping("/{donationID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getDonationByID(@PathVariable String donationID){
        try {
            Donation donation = donationService.getDonationByID(donationID);
            return ResponseEntity.status(HttpStatus.OK).body(donation);
        }
        catch (DonationNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringResponseDTO(e.getMessage()));
        }
        catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }
    }
    @PutMapping("/confirm/{donationID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> confirmDonation(@PathVariable String donationID){
        try {
            donationService.confirmDonation(donationID);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new StringResponseDTO("Đã chấp thuận ủng hộ"));
        }
        catch (UpdateFundException | DonationCompletedException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StringResponseDTO(e.getMessage()));
        }
        catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }
    }
    @PutMapping("/reject/{donationID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> rejectDonation(@PathVariable String donationID){
        try {
            donationService.rejectDonation(donationID);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new StringResponseDTO("Đã từ chối ủng hộ"));
        }
        catch (DonationDeclinedException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StringResponseDTO(e.getMessage()));
        }
        catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }
    }
}
