package com.pescue.pescue.exception;

public class InvalidEmailException extends InvalidException{
    public InvalidEmailException(String message) {
        super(message);
    }
    public InvalidEmailException(){
        super("Email không trùng khớp");
    }
}
