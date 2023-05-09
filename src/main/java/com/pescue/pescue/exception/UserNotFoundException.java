package com.pescue.pescue.exception;

import org.webjars.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException(){
        super("Không tìm thấy người dùng bạn cần tìm");
    }
}
