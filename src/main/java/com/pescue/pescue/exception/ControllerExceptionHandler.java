package com.pescue.pescue.exception;

import com.pescue.pescue.dto.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
//    @ExceptionHandler(FundingRequestNotFoundException.class)
//    public ResponseEntity<ErrorMessage> requestFundingNotFoundException(Exception ex, WebRequest request) {
//        ErrorMessage message = new ErrorMessage(
//                HttpStatus.NOT_FOUND.value(),
//                new Date(),
//                ex.getMessage(),
//                request.getDescription(false));
//        log.error(String.valueOf(message));
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
//    }
    @ExceptionHandler(DonationStatusUpdateException.class)
    public ResponseEntity<ErrorMessage> donationStatusUpdateException(Exception ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        log.error(String.valueOf(message));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
    @ExceptionHandler(FundEmptyBalanceException.class)
    public ResponseEntity<ErrorMessage> fundEmptyBalanceException(Exception ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        log.error(String.valueOf(message));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
    @ExceptionHandler(UpdateFundException.class)
    public ResponseEntity<ErrorMessage> updateFundException(Exception ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        log.error(String.valueOf(message));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorMessage> noSuchElementException(Exception ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        log.error(String.valueOf(message));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        log.error(String.valueOf(message));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }
}
