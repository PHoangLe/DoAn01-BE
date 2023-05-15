package com.pescue.pescue.exception;

public class ApplicationExistedException extends Exception{
    public ApplicationExistedException(String message) {
        super(message);
    }
    public ApplicationExistedException(){
        super("Đã ồn tại yêu cầu nhận nuôi bạn vui lòng đợi từ 4-5 ngày để nhận được kết quả");
    }
}
