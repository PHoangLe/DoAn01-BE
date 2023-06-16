package com.pescue.pescue.exception;

public class UpdateFundException extends RuntimeException{
    public UpdateFundException(String message){
        super(message);
    }
    public UpdateFundException(){
        super("Đã có lỗi xảy ra khi cập nhật quỹ cứu trợ");
    }
}
