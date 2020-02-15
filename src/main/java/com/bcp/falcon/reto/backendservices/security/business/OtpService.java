package com.bcp.falcon.reto.backendservices.security.business;

/**
 * @author Kei Takayama
 * Created on 2/14/20.
 */
public interface OtpService {

    String generateOtp(String username);

    boolean validateOtp(String name, String otp);
}
