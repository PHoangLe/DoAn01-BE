package com.pescue.pescue.exception;


import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException {
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException(){
        super("Không tìm thấy người dùng bạn cần tìm");
    }
}
