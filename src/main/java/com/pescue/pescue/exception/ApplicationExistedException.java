package com.pescue.pescue.exception;

public class ApplicationExistedException extends Exception{
    public ApplicationExistedException(String message) {
        super(message);
    }
    public ApplicationExistedException(){
        super("Bạn đã gửi yêu cầu nhận nuôi rồi. Bạn vui lòng đợi từ 4-5 ngày để nhận được kết quả");
    }
}
