package com.pescue.pescue.exception;

import org.springframework.mail.MailException;

public class SendMailFailedException extends MailException {
    public SendMailFailedException(String msg) {
        super(msg);
    }

    public SendMailFailedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SendMailFailedException(){
        super("Có lỗi xảy ra khi gửi mail");
    }
}
