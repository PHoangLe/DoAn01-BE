package com.pescue.pescue.exception;

public class RescuePostStatusUpdateException extends StatusUpdateException{
    public RescuePostStatusUpdateException(String message) {
        super(message);
    }
    public RescuePostStatusUpdateException(){
        super("Cập nhật trạng thái bài đăng không hợp lệ");
    }
}
