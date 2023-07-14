package com.pescue.pescue.controller;

import com.pescue.pescue.dto.FundDTO;
import com.pescue.pescue.dto.StringResponseDTO;
import com.pescue.pescue.exception.FundNotFoundException;
import com.pescue.pescue.model.Fund;
import com.pescue.pescue.model.FundTransaction;
import com.pescue.pescue.service.FundService;
import com.pescue.pescue.service.FundTransactionService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("api/v1/funds")
@CrossOrigin
@Api
public class FundController {
    private final FundService fundService;
    private final FundTransactionService transactionService;
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> createFund(@RequestBody FundDTO dto){
        fundService.createFund(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new StringResponseDTO("Đã tạo quỹ cứu trợ thành công"));
    }
    @PutMapping("/{fundID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> updateFund(@PathVariable String fundID,@RequestBody FundDTO dto){
        fundService.updateFund(new Fund(fundID, dto));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new StringResponseDTO("Đã cập nhật quỹ thành công"));
    }
    @DeleteMapping("/{fundID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> deleteFund(@PathVariable String fundID){
        fundService.deleteFund(fundID);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new StringResponseDTO("Đã ẩn quỹ thành công"));
    }
    @PutMapping("/restoreFund/{fundID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> restoreFund(@PathVariable String fundID){
        fundService.restoreFund(fundID);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new StringResponseDTO("Đã hồi phục quỹ thành công"));
    }
    @GetMapping("")
    public ResponseEntity<Object> getAllFund(){
        return ResponseEntity.ok(fundService.getAllFund());
    }
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAllFundToCRUD(){
        return ResponseEntity.ok(fundService.getAllFundToCRUD());
    }
    @GetMapping("/{fundID}")
    public ResponseEntity<Object> getFundByFundID(@PathVariable String fundID){
        return ResponseEntity.ok(fundService.getFundByFundID(fundID));
    }

    @GetMapping("/transactions/fund/{fundID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getTransactionByFundID(@PathVariable String fundID){
        return ResponseEntity.ok(transactionService.getTransactionByFundID(fundID));
    }
    @GetMapping("/transactions/user/{userID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getTransactionByUserID(@PathVariable String userID){
        return ResponseEntity.ok(transactionService.getTransactionByUserID(userID));
    }
}
