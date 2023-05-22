package com.pescue.pescue.controller;

import com.pescue.pescue.dto.FundDTO;
import com.pescue.pescue.dto.StringResponseDTO;
import com.pescue.pescue.exception.FundNotFoundException;
import com.pescue.pescue.model.Fund;
import com.pescue.pescue.model.FundTransaction;
import com.pescue.pescue.service.FundService;
import com.pescue.pescue.service.FundTransactionService;
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
@RequestMapping("api/v1/fund")
@CrossOrigin
public class FundController {
    private final FundService fundService;
    private final FundTransactionService transactionService;
    @PostMapping("/createFund")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> createFund(FundDTO dto){
        try {
            fundService.createFund(dto.getFundName(), dto.getFundCover(), dto.getFundDescription());
            return ResponseEntity.ok(new StringResponseDTO("Đã tạo quỹ cứu trợ thành công"));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }
    }
    @GetMapping("/getAllFund/")
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
    @GetMapping("/getFundByFundID/{fundID}")
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

    @GetMapping("/getTransactionByFundID/{fundID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getTransactionByFundID(@PathVariable String fundID){
        try {
            List<FundTransaction> transactions = transactionService.getTransactionByFundID(fundID);
            return ResponseEntity.ok(transactions);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StringResponseDTO("Đã có lỗi xảy ra với hệ thống vui lòng thử lại sau"));
        }
    }
}
