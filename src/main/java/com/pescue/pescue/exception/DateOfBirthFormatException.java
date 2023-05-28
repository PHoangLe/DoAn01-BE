package com.pescue.pescue.exception;

import java.text.ParseException;

public class DateOfBirthFormatException extends ParseException {
    private final static String message = "Ngày sinh không đúng định dạng";
    public DateOfBirthFormatException(String s, int errorOffset) {
        super(s, errorOffset);
    }
    public String getMessage(){
        return message;
    }
}
