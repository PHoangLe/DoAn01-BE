package com.pescue.pescue.exception;

public class InvalidPasswordException extends InvalidException{
    public InvalidPasswordException(String message) {
        super(message);
    }
    public InvalidPasswordException(){
        super("Mật khẩu không trùng khớp");
    }
}
