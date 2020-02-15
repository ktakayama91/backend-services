package com.bcp.falcon.reto.backendservices.notification.business;

import com.bcp.falcon.reto.backendservices.notification.expose.web.Request.SmsPaymentRequest;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */
public interface SmsService {

    void sendSms(String username);

    void sendSms(SmsPaymentRequest smsPaymentRequest);
}
