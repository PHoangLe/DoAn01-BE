package com.pescue.pescue.exception;

import org.webjars.NotFoundException;

public class AnimalNotFoundException extends NotFoundException {
    public AnimalNotFoundException(String message) {
        super(message);
    }
    public AnimalNotFoundException(){
        super("Không tìm thấy bé bạn cần tìm");
    }
}
