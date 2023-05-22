package com.pescue.pescue.exception;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

public class FundNotFoundException extends NoSuchElementException {
    public FundNotFoundException(String message) {
        super(message);
    }
    public FundNotFoundException(){
        super("Không tìm thấy quỹ cứu trợ bạn cần tìm");
    }
}
