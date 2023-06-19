package com.pescue.pescue.controller;

import com.pescue.pescue.dto.FundingRequestDTO;
import com.pescue.pescue.dto.StringResponseDTO;
import com.pescue.pescue.exception.*;
import com.pescue.pescue.model.FundingRequest;
import com.pescue.pescue.service.FundingRequestService;
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
@RequestMapping("/api/v1/funding-requests")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
@Api
public class FundingRequestController {
    private final FundingRequestService fundingRequestService;
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> createFundingRequest(FundingRequestDTO dto){
        FundingRequest request = fundingRequestService.createFundingRequest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }
    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAllFundingRequest(){
        List<FundingRequest> requests = fundingRequestService.getAllFundingRequest();
        return ResponseEntity.status(HttpStatus.OK).body(requests);
    }
    @GetMapping("/{requestID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getFundingRequestByID(@PathVariable String requestID){
        FundingRequest request = fundingRequestService.getFundingRequestByID(requestID);
        return ResponseEntity.status(HttpStatus.OK).body(request);
    }
    @GetMapping("/confirm/{requestID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> confirmFundingRequest(@PathVariable String requestID) throws FundingStatusUpdateException, FundEmptyBalanceException, UpdateFundException {
        fundingRequestService.confirmFundingRequest(requestID);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new StringResponseDTO("Đã chấp thuận yêu cầu phát quỹ"));
    }
    @GetMapping("/reject/{requestID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> rejectFundingRequest(@PathVariable String requestID) throws FundingStatusUpdateException {
        fundingRequestService.rejectFundingRequest(requestID);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new StringResponseDTO("Đã từ chối yêu cầu phát quỹ"));
    }
}
