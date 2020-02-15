package com.bcp.falcon.reto.backendservices.notification.client;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */

@Component
public class TwilioClient {

    @Value("${notification.sms.account.sid}")
    private String ACCOUNT_SID;

    @Value("${notification.sms.auth.token}")
    private String AUTH_TOKEN;

    @Value("${notification.sms.twilio.number}")
    private String TWILIO_NUMBER;

    public   Message sendSms(String body, String receiptPhoneNumber) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = new MessageCreator(
                new PhoneNumber(receiptPhoneNumber),
                new PhoneNumber(TWILIO_NUMBER),
                body
        ).create();

        return message;
    }




}