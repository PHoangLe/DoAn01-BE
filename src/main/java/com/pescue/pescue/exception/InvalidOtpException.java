package com.pescue.pescue.exception;

public class InvalidOtpException extends InvalidException{
    public InvalidOtpException(String message) {
        super(message);
    }
    public InvalidOtpException(){
        super("Mã OTP không hợp lệ");
    }
}
