package com.pescue.pescue.exception;

import org.webjars.NotFoundException;

public class RescuePostNotFoundException extends NotFoundException {
    public RescuePostNotFoundException(String message) {
        super(message);
    }
    public RescuePostNotFoundException() {
        super("Không tìm thấy bài đăng cứu trợ bạn cần tìm");
    }
}
