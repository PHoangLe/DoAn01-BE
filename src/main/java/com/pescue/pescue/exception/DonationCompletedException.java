package com.pescue.pescue.exception;

public class DonationCompletedException extends Exception{
    public DonationCompletedException(String message) {
        super(message);
    }
    public DonationCompletedException() {
        super("Ủng hộ đã được chấp thuận rồi");
    }
}
