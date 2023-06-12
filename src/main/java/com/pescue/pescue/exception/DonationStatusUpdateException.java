package com.pescue.pescue.exception;

public class DonationStatusUpdateException extends StatusUpdateException{
    public DonationStatusUpdateException(String message) {
        super(message);
    }
    public DonationStatusUpdateException() {
        super("Ủng hộ đã được từ chối hoặc chấp thuận rồi");
    }
}
