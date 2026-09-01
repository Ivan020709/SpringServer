package com.fastcam.springserver.service;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SMSService {
    private final DefaultMessageService messageService;

    @Value("${coolsms.sender-phone}")
    private String senderPhone;

    private static int number;

    public int sendSMS(String toPhone){
        number =(int)(Math.random() * (90000))+100000;

        Message message = new Message();
        message.setFrom(senderPhone);
        message.setTo(toPhone);
        message.setText(number+"");

        SingleMessageSentResponse response =
                this.messageService.sendOne(new SingleMessageSendingRequest(message));

        System.out.print("");
        return number;
    }
}