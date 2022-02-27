package com.twilio.sendsmsbytwilio.payload;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequestDto {
    private String phoneNumber;
    private String username;
    private String oneTimePassword;
}
