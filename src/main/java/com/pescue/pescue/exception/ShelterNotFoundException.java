package com.pescue.pescue.exception;

import java.util.NoSuchElementException;

public class ShelterNotFoundException extends NoSuchElementException {
    public ShelterNotFoundException(String message) {
        super(message);
    }
    public ShelterNotFoundException(){
        super("Không tìm thấy trại cứu trợ bạn cần tìm");
    }
}
