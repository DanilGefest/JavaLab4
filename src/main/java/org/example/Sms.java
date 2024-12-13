package org.example;

import lombok.Data;

@Data
public class Sms {
    private String phoneNumber;
    private String message;

    public Sms(String phone, String text) {
        phoneNumber = phone;
        message = text;
    }
}
