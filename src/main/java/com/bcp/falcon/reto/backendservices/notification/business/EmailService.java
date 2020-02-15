package com.bcp.falcon.reto.backendservices.notification.business;

import javax.mail.MessagingException;

import com.bcp.falcon.reto.backendservices.notification.expose.web.Request.EmailWelcomeRequest;
import com.bcp.falcon.reto.backendservices.payment.repository.model.PaymentModel;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */
public interface EmailService {

    String sendPaymentEmail(String username);

    String sendWelcomeEmail(String username, EmailWelcomeRequest emailWelcomeRequest);

    String retrieveEmail(String username, String code);
}
