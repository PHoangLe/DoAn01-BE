package com.pescue.pescue.exception;

public class InvalidException extends RuntimeException{
    public InvalidException(String message) {
        super(message);
    }
    public InvalidException(){
        super("Tài nguyên bạn cung cấp không trùng khớp với hệ thống");
    }
}
