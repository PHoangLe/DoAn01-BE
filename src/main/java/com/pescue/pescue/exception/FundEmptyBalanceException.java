package com.pescue.pescue.exception;

public class FundEmptyBalanceException extends Exception{
    public FundEmptyBalanceException(String message) {
        super(message);
    }
    public FundEmptyBalanceException() {
        super("Quỹ không đủ để phát tiền cho trại cứu trợ");
    }
}
