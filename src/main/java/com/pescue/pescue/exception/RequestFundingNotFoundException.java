package com.pescue.pescue.exception;

import java.util.NoSuchElementException;

public class RequestFundingNotFoundException extends NoSuchElementException {
    public RequestFundingNotFoundException(String message) {
        super(message);
    }
    public RequestFundingNotFoundException() {
        super("Không tìm thấy yêu cần phát quỹ bạn cần tìm");
    }
}
