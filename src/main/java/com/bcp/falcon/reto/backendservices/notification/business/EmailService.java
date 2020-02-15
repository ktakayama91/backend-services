package com.bcp.falcon.reto.backendservices.notification.business;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */
public interface EmailService {

    String sendEmail(String username, String emailType);

    String retrieveEmail(String username, String code);
}
