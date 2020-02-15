package com.bcp.falcon.reto.backendservices.notification.expose.web;

import com.bcp.falcon.reto.backendservices.notification.business.EmailService;
import com.bcp.falcon.reto.backendservices.notification.business.SmsService;
import com.bcp.falcon.reto.backendservices.notification.business.SmsServiceImpl;
import com.bcp.falcon.reto.backendservices.notification.expose.web.Request.SmsPaymentRequest;
import com.bcp.falcon.reto.backendservices.notification.util.constants.EmailTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */

@RestController
@RequestMapping("/api/falcon/services/notification-services/v1")
public class NotificationRestService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    @RequestMapping(value = "/email/welcome", method = RequestMethod.GET)
    public String sendWelcomeEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return emailService.sendEmail(username, EmailTypes.WELCOME.getCode());
    }

    @RequestMapping(value = "/email/payment", method = RequestMethod.GET)
    public String sendPaymentEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return emailService.sendEmail(username, EmailTypes.PAYMENT.getCode());
    }

    @RequestMapping(value = "/retrieve/{code}", method = RequestMethod.GET)
    public String retrieveEmail (@PathVariable String code) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return emailService.retrieveEmail(username, code);
    }

    @RequestMapping(value = "/sms/otp", method = RequestMethod.GET)
    public void sendOtpSms() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        smsService.sendSms(username);
    }

    @RequestMapping(value = "/sms/payment", method = RequestMethod.POST)
    public void sendPaymentSms(@RequestBody SmsPaymentRequest smsPaymentRequest) {
        smsService.sendSms(smsPaymentRequest);
    }
}