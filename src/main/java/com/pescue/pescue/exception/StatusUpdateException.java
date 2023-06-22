package com.pescue.pescue.exception;

public class StatusUpdateException extends RuntimeException{
    public StatusUpdateException(String message) {
        super(message);
    }
    public StatusUpdateException(){
        super("Cập nhật trạng thái không hợp lệ");
    }
}
