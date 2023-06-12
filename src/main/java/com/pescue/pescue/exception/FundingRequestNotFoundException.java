package com.pescue.pescue.exception;

import java.util.NoSuchElementException;

public class FundingRequestNotFoundException extends NoSuchElementException {
    public FundingRequestNotFoundException(String message) {
        super(message);
    }
    public FundingRequestNotFoundException() {
        super("Không tìm thấy yêu cần phát quỹ bạn cần tìm");
    }
}
