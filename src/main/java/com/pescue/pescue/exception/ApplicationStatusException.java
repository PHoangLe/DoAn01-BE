package com.pescue.pescue.exception;

public class ApplicationStatusException extends Exception{
    public ApplicationStatusException(String message) {
        super(message);
    }
    public ApplicationStatusException(){
        super("Không tìm thấy quỹ cứu trợ bạn cần tìm");
    }
}
