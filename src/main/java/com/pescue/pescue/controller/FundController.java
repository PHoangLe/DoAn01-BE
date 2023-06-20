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
        try {
            fundService.createFund(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new StringResponseDTO("Đã tạo quỹ cứu trợ thành công"));
        }
        catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }
    }
    @PutMapping("/{fundID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> updateFund(@PathVariable String fundID, FundDTO dto){
        try {
            fundService.updateFund(new Fund(fundID, dto));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new StringResponseDTO("Đã cập nhật quỹ thành công"));
        }
        catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }
    }
    @DeleteMapping("/{fundID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> deleteFund(@PathVariable String fundID){
        try {
            fundService.deleteFund(fundID);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new StringResponseDTO("Đã xóa quỹ thành công"));
        }
        catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }
    }
    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAllFund(){
        try {
            return ResponseEntity.ok(fundService.getAllFund());
        }
        catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }
    }
    @GetMapping("/{fundID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getFundByFundID(@PathVariable String fundID){
        try {
            Fund fund = fundService.getFundByFundID(fundID);
            return ResponseEntity.ok(fund);
        }
        catch (FundNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringResponseDTO(e.getMessage()));
        }
        catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }
    }

    @GetMapping("/Transactions/{fundID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getTransactionByFundID(@PathVariable String fundID){
        try {
            List<FundTransaction> transactions = transactionService.getTransactionByFundID(fundID);
            return ResponseEntity.ok(transactions);
        }
        catch (FundNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StringResponseDTO(e.getMessage()));
        }
        catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }
    }
}
