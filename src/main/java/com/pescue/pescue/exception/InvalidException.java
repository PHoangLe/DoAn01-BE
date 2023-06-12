package com.pescue.pescue.exception;

public class InvalidException extends Exception{
    public InvalidException(String message) {
        super(message);
    }
    public InvalidException(){
        super("Tài nguyên bạn cung cấp không trùng khớp với hệ thống");
    }
}
