package com.pescue.pescue.exception;

public class FundingStatusUpdateException extends StatusUpdateException{
    public FundingStatusUpdateException(String message) {
        super(message);
    }
    public FundingStatusUpdateException() {
        super("Yêu cầu đã được từ chối hoặc chấp thuận rồi");
    }
}
