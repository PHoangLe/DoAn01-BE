package com.pescue.pescue.exception;

public class ExpiredOtpException extends InvalidException{
    public ExpiredOtpException(String message) {
        super(message);
    }
    public ExpiredOtpException() {
        super("Mã OTP bạn đã hết hạn");
    }
}
