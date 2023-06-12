package com.pescue.pescue.exception;

import java.util.NoSuchElementException;

public class AnimalNotFoundException extends NoSuchElementException {
    public AnimalNotFoundException(String message) {
        super(message);
    }
    public AnimalNotFoundException(){
        super("Không tìm thấy bé bạn cần tìm");
    }
}
