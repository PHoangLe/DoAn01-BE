package com.pescue.pescue.exception;

public class ApplicationStatusUpdateException extends StatusUpdateException{
    public ApplicationStatusUpdateException(String message) {
        super(message);
    }
    public ApplicationStatusUpdateException(){
        super("Yêu cầu đã được chấp thuận hoặc từ chối rồi");
    }
}
