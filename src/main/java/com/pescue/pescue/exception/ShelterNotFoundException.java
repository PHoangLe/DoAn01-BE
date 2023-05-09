package com.pescue.pescue.exception;

import org.webjars.NotFoundException;

public class ShelterNotFoundException extends NotFoundException {
    public ShelterNotFoundException(String message) {
        super(message);
    }
    public ShelterNotFoundException(){
        super("Không tìm thấy trại cứu trợ bạn cần tìm");
    }
}
