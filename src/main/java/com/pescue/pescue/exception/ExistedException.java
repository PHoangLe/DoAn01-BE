package com.pescue.pescue.exception;

public class ExistedException extends RuntimeException{
    public ExistedException(String message) {
        super(message);
    }
    public ExistedException(){
        super("Tài nguyên bạn muốn thêm đã tồn tại trong hệ thống");
    }
}
