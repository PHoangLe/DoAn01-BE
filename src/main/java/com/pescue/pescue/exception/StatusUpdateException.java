package com.pescue.pescue.exception;

public class StatusUpdateException extends RuntimeException{
    public StatusUpdateException(String message) {
        super(message);
    }
    public StatusUpdateException(){
        super("Không tìm thấy trại cứu trợ bạn cần tìm");
    }
}
