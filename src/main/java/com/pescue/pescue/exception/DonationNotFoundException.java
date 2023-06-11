package com.pescue.pescue.exception;

import java.util.NoSuchElementException;

public class DonationNotFoundException extends NoSuchElementException {
    public DonationNotFoundException(String message) {
        super(message);
    }
    public DonationNotFoundException() {
        super("Không tìm thấy ủng hộ bạn cần tìm");
    }
}
