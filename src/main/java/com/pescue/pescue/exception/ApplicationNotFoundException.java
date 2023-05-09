package com.pescue.pescue.exception;

import org.webjars.NotFoundException;

public class ApplicationNotFoundException extends NotFoundException {
    public ApplicationNotFoundException(String message) {
        super(message);
    }
    public ApplicationNotFoundException() {
        super("Không tìm thấy yêu cầu bạn cần tìm");
    }
}
