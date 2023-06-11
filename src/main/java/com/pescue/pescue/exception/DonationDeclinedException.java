package com.pescue.pescue.exception;

public class DonationDeclinedException extends Exception{
    public DonationDeclinedException(String message) {
        super(message);
    }
    public DonationDeclinedException() {
        super("Ủng hộ đã được từ chối rồi");
    }
}
