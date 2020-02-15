package com.bcp.falcon.reto.backendservices.notification.client;

import com.twilio.rest.api.v2010.account.Message;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */

public interface TwilioClient {
    Message sendSms(String body, String receiptPhoneNumber);
}
