package com.twilio.sendsmsbytwilio.resource;

import com.twilio.sendsmsbytwilio.payload.PasswordResetRequestDto;
import com.twilio.sendsmsbytwilio.service.TwilioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TwilioOTPHandler {
    @Autowired
    private TwilioService twilioService;

    public Mono<ServerResponse> sendOTP(ServerRequest serverRequest){
       return  serverRequest.bodyToMono(PasswordResetRequestDto.class).flatMap(dto -> twilioService.sendOTP(dto))
               .flatMap(passwordResetResponseDto -> ServerResponse.status(HttpStatus.OK)
                       .body(BodyInserters.fromValue(passwordResetResponseDto)));

    }

    public Mono<ServerResponse> validateOTP(ServerRequest serverRequest){
        return  serverRequest.bodyToMono(PasswordResetRequestDto.class).flatMap(dto -> twilioService.validateOTP(dto.getOneTimePassword(), dto.getUsername()))
                .flatMap(passwordResetResponseDto -> ServerResponse.status(HttpStatus.OK)
                        .bodyValue(passwordResetResponseDto));

    }
}
