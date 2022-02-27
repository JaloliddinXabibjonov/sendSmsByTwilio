package com.twilio.sendsmsbytwilio.service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.sendsmsbytwilio.config.TwilioConfig;
import com.twilio.sendsmsbytwilio.payload.OtpStatus;
import com.twilio.sendsmsbytwilio.payload.PasswordResetRequestDto;
import com.twilio.sendsmsbytwilio.payload.PasswordResetResponseDto;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class TwilioService {
    @Autowired
    private TwilioConfig twilioConfig;

    Map<String, String> otpMap = new HashMap<>();

    public Mono<PasswordResetResponseDto> sendOTP(PasswordResetRequestDto passwordResetRequestDto) {
        PasswordResetResponseDto passwordResetResponseDto = null;
        try {
            PhoneNumber to = new PhoneNumber(passwordResetRequestDto.getPhoneNumber());
            PhoneNumber from = new PhoneNumber(twilioConfig.getTrialNumber());
            String otp = generateOTP();
            String otpMessage = "Hurmatli foydalanuvchi! Sizning tasdiqlovchi kodingiz:" + otp +". Kodni begonalarga bermang!";
            Message message = Message
                    .creator(to, from,
                            otpMessage)
                    .create();
            otpMap.put(passwordResetRequestDto.getUsername(), otp);
            passwordResetResponseDto = new PasswordResetResponseDto(OtpStatus.YUBORILDI, otpMessage);
        } catch (Exception e) {
            passwordResetResponseDto = new PasswordResetResponseDto(OtpStatus.XATOLIK, e.getMessage());
        }
        return Mono.just(passwordResetResponseDto);
    }

    private String generateOTP() {
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }

    public Mono<String> validateOTP(String userInputOtp, String username) {
        if (userInputOtp.equals(otpMap.get(username))) {
            return Mono.just("Tekshiruv muvaffaqiyatli bajarildi");
        }
        else {
            return Mono.error(new IllegalArgumentException("Kiritilgan kod noto`g'ri, qaytadan urinib ko`ring"));
        }
    }
}
